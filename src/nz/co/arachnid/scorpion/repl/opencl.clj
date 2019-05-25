(ns nz.co.arachnid.scorpion.repl.opencl
  (:require [uncomplicate.clojurecl.core :refer :all])
  (:require [uncomplicate.clojurecl.info :refer :all])
  (:require [uncomplicate.commons.core :refer :all]))

;; ============================================
;; The Entire platform, can be multiple devices
;; ============================================

(map info (platforms))

(map info (devices (first (platforms))))

(def nvidia-platform (first (platforms)))

(map name-info (devices nvidia-platform))

;; ============================================
;; Bind My Device
;; ============================================
(def my-nvidia-gpu (first (devices nvidia-platform)))

;; ============================================
;; Get a Context off that Device
;; ============================================
(def ctx (context [my-nvidia-gpu]))

(def gpu-array (cl-buffer ctx 1024 :read-write))

(def main-array (float-array (range 256)))

(take 10 main-array)

;; ============================================
;; Create the command queue
;; ============================================
(def queue (command-queue ctx my-nvidia-gpu))

;; Transfer it in
(enq-write! queue gpu-array main-array)

;; Transfer it out
(def roundtrip-array (float-array 256))
(enq-read! queue gpu-array roundtrip-array)
(take 12 roundtrip-array)

;; ============================================
;; Prepare a Kernel
;; ============================================
(def kernel-source
  "__kernel void mul10(__global float *a) {
     int i = get_global_id(0);
     a[i] = a[i] * 10.0f;
   };")

;; ============================================
;; Compile That Kernel
;; ============================================
(def hello-program (build-program! (program-with-source ctx [kernel-source])))
(def mul10 (kernel hello-program "mul10"))
(def result (float-array 256))

;; ============================================
;; Send it off for processing
;; ============================================
(set-arg! mul10 0 gpu-array)
(enq-kernel! queue mul10 (work-size-1d 256))
(enq-read! queue gpu-array result)
(take 12 result)


;; ============================================
;; Free up the resources
;; ============================================
(release gpu-array)
(release hello-program)
(release queue)
(release ctx)

