(ns my-sketch.rotate
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as util]))


;;----------------
(defn setup
  []
  (q/frame-rate 30) ;fps
  (q/color-mode :hsb)
  {:shapes (for [i (range 0 500 100)
                  j (range 0 500 100)]
              {:x i :y j
               :hue (util/rand-int-range 0 255)
               :theta (* (/ q/PI 8) (rand-int 8))})})

;;----------------
(defn update-shape
  [shape]
  (if (< (rand-int 30) 1)
    (-> shape
      (update :theta #(+ % (/ q/PI 8)))
      (update :hue #(mod (+ % 1) 256)))
    ;; else
    shape))

(defn update-state
  [state]
  (-> state
      (update :shapes #(map update-shape %))))

;;----------------

(defn cross
  "Draw a cross centred at [x y] with side d and rotated by theta"
  [x y d theta]
  (let [d' (/ d 2)]
    (q/with-rotation [theta]
      (q/line (- x d') y (+ x d') y)
      (q/line x (- y d') x (+ y d')))))

(defn render-state
  [state]
  (let [c (q/frame-count)]
    (q/background 30)
    (q/stroke-weight 16)
    (q/with-translation [50 50]
      (doseq [{:keys [x y hue theta]} (:shapes state)]
        (q/stroke hue 255 255)
        (q/with-translation [x y]
          (cross 0 0 92 theta))))))

;;----------------
(q/defsketch rotate
  :title ""
  :size [500 500]
  :setup setup
  :update update-state
  :draw render-state
  ;; :mouse-clicked func
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
