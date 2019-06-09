(ns nz.co.arachnid.scorpion.matrixnative
  (:use [uncomplicate.neanderthal core native]))

(def x (dv 1 2 3))
(def y (dv 10 20 30))
(def dotresult (dot x y))


;; 10 * 10 Matrix
(def m10by10 (dge 10 10))

(defn large-square-matrix-mult
  [n]
  (let [cnt        n
        matrix-a   (dge cnt cnt (range (* cnt cnt)))
        matrix-b   (copy matrix-a)
        matrix-c   (copy matrix-a)]
    (time (do (mm! 3 matrix-a matrix-b 2 matrix-c)
              matrix-c))))

(large-square-matrix-mult 8)
(large-square-matrix-mult 16)
(large-square-matrix-mult 32)
(large-square-matrix-mult 64)
(large-square-matrix-mult 128)
(large-square-matrix-mult 256)
(large-square-matrix-mult 1024)
(large-square-matrix-mult 2048)
;; 4096^2.3 is 203,436,033 operations
(large-square-matrix-mult 4096)


