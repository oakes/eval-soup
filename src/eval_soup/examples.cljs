(ns eval-soup.examples
  (:require-macros [dynadoc.example :refer [defexamples]]))

(defexamples eval-soup.core/code->results
  [{:doc "You can reference vars you previously made."
    :with-callback callback}
   (code->results ['(def n 4) '(conj [1 2 3] n)] callback)]
  [{:doc "You can pass the code as strings too."
    :with-callback callback}
   (code->results ["(def n 4)" "(conj [1 2 3] n)"] callback)]
  [{:doc "If your code exceeds the timeout, you'll see an exception.
   
   You can turn off timeout protection by passing `:disable-timeout? true`
   in the options map."
    :with-callback callback}
   (code->results ['(while true)] callback {:timeout 1000})])

