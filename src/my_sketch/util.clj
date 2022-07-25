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
