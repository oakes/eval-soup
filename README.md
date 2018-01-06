[![Clojars Project](https://img.shields.io/clojars/v/eval-soup.svg)](https://clojars.org/eval-soup)

## Introduction

A nice eval wrapper for Clojure and ClojureScript. It even protects you from infinite loops!

Clojure example:

```clojure
(code->results ['(def n 4) '(conj [1 2 3] n) '(nil) '(while true)])
; => [#'clj.user/n, [1 2 3 4], #error {:message "Can't call nil"}, #error {:message "Execution timed out.”}]
```

ClojureScript example:

```clojure
(code->results ['(def n 4) '(conj [1 2 3] n) '(nil) '(while true)"]
  (fn [results]
    ; => [#'cljs.user/n, [1 2 3 4], #error {:message "Can't call nil"}, #error {:message "Execution timed out."}]
    ))
```

## Usage

You can include this library in your project dependencies using the version number in the badge above.

To experiment with this library in a REPL, you can use [the Clojure CLI tool](https://clojure.org/guides/getting_started#_clojure_installer_and_cli_tools). In this directory, run `clj` to start a Clojure REPL, or `clj -R:cljs -m cljs.repl.node` to start a ClojureScript REPL. When the REPL is up, enter the main namespace with `(require 'eval-soup.core) (in-ns 'eval-soup.core)`.

## Licensing

All files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that they are also dedicated to the public domain.
