## Introduction

A nice eval wrapper for Clojure and ClojureScript. It even protects you from infinite loops!

Clojure example:

```clojure
(code->results ["(+ 1 1)" "(conj [1 2 3] 4)" "(nil)" "(while true)"])
; => [2, [1 2 3 4], #error {:message "Can't call nil"}, #error{:message "Execution timed out.”}]
```

ClojureScript example:

```clojure
(code->results ["(+ 1 1)" "(conj [1 2 3] 4)" "(nil)" "(while true)"]
  (fn [results]
    ; => [2, [1 2 3 4], #error {:message "Can't call nil"}, #error {:message "Execution timed out."}]
    ))
```

## Licensing

All files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that they are also dedicated to the public domain.
