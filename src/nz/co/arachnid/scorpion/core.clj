(ns nz.co.arachnid.scorpion.core
  (:require [uncomplicate.clojurecl.core :refer :all])
  (:require [uncomplicate.clojurecl.info :refer :all])
  (:require [uncomplicate.commons.core :refer :all]))

(init)
(map info (map device (range (device-count))))