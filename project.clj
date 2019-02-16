(defproject eval-soup "1.5.1-SNAPSHOT"
  :description "A nice eval wrapper for Clojure and ClojureScript"
  :url "https://github.com/oakes/eval-soup"
  :license {:name "Public Domain"
            :url "http://unlicense.org/UNLICENSE"}
  :repositories [["clojars" {:url "https://clojars.org/repo"
                             :sign-releases false}]]
  :profiles {:dev {:main eval-soup.core}})
