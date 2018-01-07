(ns eval-soup.core
  (:require [clojure.java.io :as io]
            [eval-soup.clojail :refer [thunk-timeout]]
            [dynadoc.example :refer [defexamples]])
  (:import [java.io File StringWriter]))

(defn wrap-security
  "Returns a function that wraps the given function in a sandbox.
  It uses eval_soup/java.policy to define permissions. By default,
  it only disallows exiting via System/exit."
  [thunk]
  (fn []
    (System/setProperty "java.security.policy"
      (-> "eval_soup/java.policy" io/resource .toString))
    (System/setSecurityManager
      (proxy [SecurityManager] []
        (checkExit [status#]
          (throw (SecurityException. "Exit not allowed.")))))
    (try (thunk)
      (finally (System/setSecurityManager nil)))))

(defmacro with-security
  "Convenience macro that wraps the body with wrap-security
  and then immediately executes it."
  [& body]
  `(apply (wrap-security (fn [] ~@body)) []))

(defn wrap-timeout
  "Returns a function that wraps the given function in a timeout checker.
  The timeout is specified in milliseconds. If the timeout is reached,
  an exceptino will be thrown."
  [thunk timeout]
  (fn []
    (thunk-timeout thunk timeout)))

(defn ^:private str->form [nspace s]
  (binding [*read-eval* false]
    (read-string s)))

(defn ^:private eval-form [form nspace {:keys [timeout
                                               disable-timeout?
                                               disable-security?]}]
  (try
    (cond-> (fn []
              (binding [*ns* nspace
                        *out* (StringWriter.)]
                (let [form (if (string? form)
                             (str->form nspace form)
                             form)]
                  (refer-clojure)
                  [(eval form)
                   (if (and (coll? form) (= 'ns (first form)))
                     (-> form second create-ns)
                     *ns*)])))
            (not disable-timeout?) (wrap-timeout timeout)
            (not disable-security?) (wrap-security)
            true (apply []))
    (catch Exception e [e nspace])))

(defn code->results
  "Returns a vector of the evaluated result of each of the given forms.
  If any of the forms are strings, it will read them first."
  ([forms]
   (code->results forms {}))
  ([forms {:keys [timeout
                  disable-timeout?
                  disable-security?]
           :or {timeout 4000
                disable-timeout? false
                disable-security? false}
           :as opts}]
   (let [opts {:timeout timeout
               :disable-timeout? disable-timeout?
               :disable-security? disable-security?}]
     (loop [forms forms
            results []
            nspace (create-ns 'clj.user)]
       (if-let [form (first forms)]
         (let [[result current-ns] (eval-form form nspace opts)]
           (recur (rest forms) (conj results result) current-ns))
         results)))))

(defexamples code->results
  ["Define a var and then use it."
   (code->results ['(def n 4) '(conj [1 2 3] n)])]
  ["You can use strings too."
   (code->results ["(def n 4)" "(conj [1 2 3] n)"])]
  ["Timeout after two seconds.
   
   You can turn off timeout protection by passing `:disable-timeout? true`
   in the options map."
   (code->results ['(while true)] {:timeout 1000})]
  ["Don't allow the system to exit.
   
   You can turn off exit protection by passing `:disable-security? true`
   in the options map."
   (code->results ['(System/exit 0)])])

