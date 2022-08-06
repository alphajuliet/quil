(ns my-sketch.util
  (:require [quil.core :as q]))

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
  (+ a (rand (- b a))))

(defn v2+
  "Vector add"
  [[x y] [u v]]
  [(+ x u) (+ y v)])

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
        (q/with-translation (util/v2scale -1 v#)
          ~@body)))))

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

(defn d2r
  [r]
  (* r (/ q/PI 180.)))

(defn r2d
  [d]
  (/ d (/ q/PI 180.)))

;; The End
