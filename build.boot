(set-env!
  :resource-paths #{"src" "resources"}
  :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  [dynadoc "RELEASE" :scope "test" :exclusions [eval-soup]]
                  [seancorfield/boot-tools-deps "0.1.4" :scope "test"]]
  :repositories (conj (get-env :repositories)
                  ["clojars" {:url "https://clojars.org/repo/"
                              :username (System/getenv "CLOJARS_USER")
                              :password (System/getenv "CLOJARS_PASS")}]))

(require
  '[clojure.edn :as edn]
  '[dynadoc.boot :refer [dynadoc]]
  '[boot-tools-deps.core :refer [deps]]
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]])

(task-options!
  pom {:project 'eval-soup
       :version "1.4.1-SNAPSHOT"
       :description "A nice eval wrapper for Clojure and ClojureScript"
       :url "https://github.com/oakes/eval-soup"
       :license {"Public Domain" "http://unlicense.org/UNLICENSE"}
       :dependencies (->> "deps.edn"
                          slurp
                          edn/read-string
                          :deps
                          (reduce
                            (fn [deps [artifact info]]
                              (if-let [version (:mvn/version info)]
                                (conj deps
                                  (transduce cat conj [artifact version]
                                    (select-keys info [:scope :exclusions])))
                                deps))
                            []))}
  push {:repo "clojars"})

(deftask run-docs []
  (set-env! :resource-paths #{"src" "resources" "dev-resources"})
  (comp
    (deps :aliases [:cljs])
    (watch)
    (reload :asset-path "dynadoc-extend")
    (cljs
      :optimizations :none ; replace :none with :simple to enable exporting
      :compiler-options {:asset-path "/main.out"})
    (dynadoc :port 5000)))

(deftask local []
  (comp (pom) (jar) (install)))

(deftask deploy []
  (comp (pom) (jar) (push)))

