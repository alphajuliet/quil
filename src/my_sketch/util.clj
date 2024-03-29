(ns my-sketch.util
  (:require [quil.core :as q]))

;; --------------------------------
;; Numeric utilities

(defn clamp
  [x x-min x-max]
  (-> x
      (max x-min)
      (min x-max)))

(defn rand-int-range
  "Integer range"
  [a b]
  (+ a (rand-int (- b a))))

(defn rand-range
  "Floating point range"
  [a b]
  (q/random a b))

(defn one-on [x] (/ 1. x))
(defn pi-on [x] (/ q/PI x))

;; --------------------------------
;; Conversions

(def mod256 #(mod % 256))
(def mod2pi #(mod % (* 2 q/PI)))

(def d2r #(* % (/ q/PI 180.)))
(def r2d #(/ % (/ q/PI 180.)))



(defn rθ->xy
  [[r θ]]
  [(* r (q/cos θ)) (* r (q/sin θ))])

;; --------------------------------
;; 2D vector operations

(defn v2+
  "Vector add"
  [u v]
  (mapv + u v))

(defn v2mod+
  "Vector add with modulo"
  [[x y] [u v] [a b]]
  [(mod (+ x u) a) (mod (+ y v) b)])

(defn v2scale
  "Scale [x y] by k"
  [k [x y]]
  [(* k x) (* k y)])

(defmacro rotate-around
  "Rotate the body by theta around a given 2D vertex.
  e.g. (rotate-around [(* 2 q/PI) [50 75]] (q/triangle 10 10 10))"
  [[theta vertex] & body]
  `(let [th# ~theta
         v# ~vertex]
     (q/with-translation v#
      (q/with-rotation [th#]
        (q/with-translation (v2scale -1 v#)
          ~@body)))))

;; --------------------------------
;; Time-based waveforms

(defn sin-wave
  "Return a sine wave with amplitude around zero, or min to max, and period in seconds"
  ([ampl period]
   (sin-wave 0 ampl period))
  ([min max period]
   (+ min (* (- max min) (q/sin (/ (q/frame-count) (* period (q/current-frame-rate))))))))

(defn saw-wave
  "An up-ramp wave from zero to amplitude and period, or from min to max."
  ([ampl period]
   (saw-wave 0 ampl period))
  ([min max period]
   (let [d (* period (q/current-frame-rate))]
     (+ min (* (- max min) (/ (mod (q/frame-count) d) d))))))

;; --------------------------------
;; Additional shapes

(defn line
  [[x1 y1] [x2 y2]]
  (q/line x1 y1 x2 y2))

(defn square
  "Draw a square centred at [x y] with side d and rotated by theta"
  [x y d theta]
  (let [d' (/ d 2)
        x1 (- x d')
        y1 (- x d')
        x2 (+ x d')
        y2 (+ y d')]
    (q/with-rotation [theta]
      (q/line x1 y1 x2 y1)
      (q/line x2 y1 x2 y2)
      (q/line x2 y2 x1 y2)
      (q/line x1 y2 x1 y1))))

;; The End
