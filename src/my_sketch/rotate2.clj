(ns my-sketch.rotate2
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as util]))

;; Overall dimensions
(def sketch-size [500 500])

;;----------------
(defn setup
  []
  (q/frame-rate 24) ;fps
  (q/color-mode :hsb)
  (let [[w h] sketch-size]
    {:render? false
    :theta 0
    :centre (util/v2scale 0.5 [w h])
    :shapes (for [i (range 0 w 50)
                  j (range 0 h 50)]
              {:x i :y j
               :hue (+ 25 (* 85 (rand-int 3)))
               :brightness 255
               :theta (* (util/d2r 90) (rand-int 4))})}))

;;----------------
(defn update-shape
  [shape]
  (if (< (rand-int 48) 1)
    (-> shape
        (update :theta #(mod (+ % (util/d2r 90))
                             (* 2 q/PI)))
      (update :hue #(mod (+ % 1) 256)))
    ;; else
    shape))

(defn update-state
  [state]
  (-> state
      (update :theta #(mod (+ 0.01 %) (* 2 q/PI)))
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
  [{:keys [theta centre] :as state}]
  (q/background 30)
  (q/stroke-weight 8)
  (util/rotate-around [theta centre]
                      (q/with-translation [12 12]
                        (doseq [{:keys [x y hue brightness theta]} (:shapes state)]
                          (q/stroke hue 255 brightness)
                          (q/with-translation [x y]
                            (tile 0 0 48 theta)
                            (when (:render? state)
                              (q/save-frame "frames/f####.png")))))))

(defn snapshot
  [state _]
  (q/save-frame "image/snapshot.png")
  state)

;;----------------
(q/defsketch rotate2
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked snapshot
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
