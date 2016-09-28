[![Clojars Project](https://img.shields.io/clojars/v/eval-soup.svg)](https://clojars.org/eval-soup)

## Introduction

A nice eval wrapper for Clojure and ClojureScript. It even protects you from infinite loops!

Clojure example:

```clojure
(code->results ["(def n 4)" "(conj [1 2 3] n)" "(nil)" "(while true)"])
; => [#'clj.user/n, [1 2 3 4], #error {:message "Can't call nil"}, #error {:message "Execution timed out.”}]
```

ClojureScript example:

```clojure
(code->results ["(def n 4)" "(conj [1 2 3] n)" "(nil)" "(while true)"]
  (fn [results]
    ; => [#'cljs.user/n, [1 2 3 4], #error {:message "Can't call nil"}, #error {:message "Execution timed out."}]
    ))
```

## Licensing

All files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that they are also dedicated to the public domain.
