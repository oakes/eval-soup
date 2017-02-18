(set-env!
  :source-paths #{"src"}
  :resource-paths #{"src" "resources"}
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.9.473" :scope "provided"]
                  [org.clojure/core.async "0.2.395"]]
  :repositories (conj (get-env :repositories)
                  ["clojars" {:url "https://clojars.org/repo/"
                              :username (System/getenv "CLOJARS_USER")
                              :password (System/getenv "CLOJARS_PASS")}]))

(task-options!
  pom {:project 'eval-soup
       :version "1.2.1-SNAPSHOT"
       :description "A nice eval wrapper for Clojure and ClojureScript"
       :url "https://github.com/oakes/eval-soup"
       :license {"Public Domain" "http://unlicense.org/UNLICENSE"}}
  push {:repo "clojars"})

(deftask local []
  (comp (pom) (jar) (install)))

(deftask deploy []
  (comp (pom) (jar) (push)))

