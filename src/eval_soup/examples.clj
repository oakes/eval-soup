(ns eval-soup.examples
  (:require [dynadoc.example :refer [defexamples]]))

(defexamples eval-soup.core/code->results
  ["You can reference vars you previously made."
   (code->results ['(def n 4) '(conj [1 2 3] n)])]
  ["You can pass the code as strings too."
   (code->results ["(def n 4)" "(conj [1 2 3] n)"])]
  ["If your code exceeds the timeout, you'll see an exception.
   
   You can turn off timeout protection by passing `:disable-timeout? true`
   in the options map."
   (code->results ['(while true)] {:timeout 1000})]
  ["If your code tries to exit, you'll see an exception.
   
   You can turn off exit protection by passing `:disable-security? true`
   in the options map."
   (code->results ['(System/exit 0)])])

