(ns user
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer :all]
            [clojure.repl :refer :all]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer :all]))
            [clojure.tools.namespace.repl :refer :all]
            [com.walmartlabs.datascope :as ds]
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

(defn view-data-structure
  [data-structure]
  (ds/view data-structure))

(defn rebl-start
  []
  (rebl/ui))

(defn rebl-send
  [expression]
  (rebl/inspect expression))
