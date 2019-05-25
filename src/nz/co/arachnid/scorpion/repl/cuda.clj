(ns nz.co.arachnid.scorpion.repl.cuda
  (:require [uncomplicate.clojurecuda.core :refer :all])
  (:require [uncomplicate.commons.core     :refer :all])
  (:require [uncomplicate.clojurecuda.info :refer :all]))

(init)

(map info
     (map device (range (device-count))))
