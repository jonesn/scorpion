(ns user
  (:require [clojure.pprint :refer :all]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer :all]
            [cognitect.rebl :as rebl]))

(defn list-namespaces
  "Lists all the namespaces on the classpath"
  []
  (sort
    (map
      (fn [namespace] (.getName namespace))
      (all-ns))))

(defn list-public-symbols
  "Lists all the public symbols for a namespace
   Should be called with a symbol argument.

   ```
   (list-public-symbols 'taoensso.timbre)
   ```"
  [sym-namespace]
  (sort (ns-publics (find-ns sym-namespace))))

(defn print-map-table
  [col-m]
  (print-table col-m))

(defn repl-reload
  []
  (refresh))

(defn rebl-start
  "Start a new REBL UI"
  []
  (rebl/ui))

(defn rebl-send
  "Pipe off the given expression to a running REBL session."
  [expression]
  (rebl/inspect expression))
