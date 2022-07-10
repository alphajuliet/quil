(ns my-sketch.util)

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

;; The End
