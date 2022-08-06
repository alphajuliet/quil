(ns my-sketch.hexutil
  (:require [quil.core :as q]))
;; Ref. https://www.redblobgames.com/grids/hexagons/

(defn xy->rθ
  [[x y]]
  [(q/sqrt (+ (* x x) (* y y))) (q/atan2 y x)])

(defn rθ->xy
  [[r θ]]
  [(* r (q/cos θ)) (* r (q/sin θ))])

(defn axial->oddr
  [[q r]]
  (let [col (+ q (/ (- r (bit-and r 1)) 2))
        row r]
    [col row]))

(defn oddr->axial
  [[col row]]
  (let [q (- col (/ (- row (bit-and row 1)) 2))
        r row]
    [q r]))

(defn hex->coord
  [[q r]]
  (let [sqrt3 (q/sqrt 3)]
    [(+ (* sqrt3 q) (* (/ sqrt3 2) r))
     (* 1.5 r)]))

;; function pointy_hex_to_pixel(hex):
    ;; var x = size * (sqrt(3) * hex.q  +  sqrt(3)/2 * hex.r)
    ;; var y = size * (                         3./2 * hex.r)
    ;; return Point(x, y)
;; The End
