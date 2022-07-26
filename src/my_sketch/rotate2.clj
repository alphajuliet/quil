(ns my-sketch.rotate2
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as util]))


;;----------------
(defn setup
  []
  (q/frame-rate 10) ;fps
  (q/color-mode :hsb)
  {:shapes (for [i (range 0 500 25)
                 j (range 0 500 25)]
             {:x i :y j
              :hue (+ 25 (* 85 (rand-int 3)))
              :brightness 255
              :theta (* (util/d2r 90) (rand-int 4))})})

;;----------------
(defn update-shape
  [shape]
  (if (< (rand-int 30) 1)
    (-> shape
        (update :theta #(mod (+ % (util/d2r 90))
                             (* 2 q/PI)))
      (update :hue #(mod (+ % 1) 256)))
    ;; else
    shape))

(defn update-state
  [state]
  (-> state
      (update :shapes #(map update-shape %))))

;;----------------
(defn tile
  "Draw a tile centred at [x y] with side d and rotated by theta"
  [x y d theta]
  (let [d' (q/floor (/ d 2))]
    (q/with-rotation [theta]
      (q/line (- x d') y (+ x d') y)
      (q/line x y x (- y d')))))

(defn render-state
  [state]
  (let [c (q/frame-count)]
    (q/background 30)
    (q/stroke-weight 3)
    (q/with-translation [12 12]
      (doseq [{:keys [x y hue brightness theta]} (:shapes state)]
        (q/stroke hue 255 brightness)
        (q/with-translation [x y]
          (tile 0 0 24 theta))))))

(defn snapshot
  [state _]
  (q/save-frame "snapshot.png")
  state)

;;----------------
(q/defsketch rotate2
  :title ""
  :size [500 500]
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked snapshot
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
