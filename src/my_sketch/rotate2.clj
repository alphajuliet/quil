(ns my-sketch.rotate2
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]))

;; Overall dimensions
(def sketch-size [500 500])
(def render? false)

;;----------------
(defn setup
  []
  (q/frame-rate 24) ;fps
  (q/color-mode :hsb)
  (let [[w h] sketch-size]
    {:rho 0
    :centre (u/v2scale 0.5 [w h])
    :shapes (for [i (range 0 w 50)
                  j (range 0 h 50)]
              {:x i :y j
               :hue (-> 3 rand-int (* 85) (+ 25))
               :brightness 255
               :theta (-> 4 rand-int (* (u/d2r 90)))})}))

;;----------------
(defn update-shape
  [shape]
  (if (< (rand-int 48) 1)
    (-> shape
        (update :theta (comp u/mod2pi (partial + (u/d2r 90))))
        (update :hue (comp u/mod256 inc)))
    ;; else
    shape))

(defn update-state
  [state]
  (-> state
      (update :rho (comp u/mod2pi (partial + 0.01)))
      (update :shapes (partial map update-shape))))

;;----------------
(defn tile
  "Draw a tile centred at [x y] with side d and rotated by theta"
  [x y d theta]
  (let [d' (q/floor (/ d 2))]
    (q/with-rotation [theta]
      (q/line (- x d') y (+ x d') y)
      (q/line x y x (- y d')))))

(defn render-shape
  [{:keys [x y theta hue brightness]}]
  (q/stroke hue 255 brightness)
  (q/with-translation [x y]
    (tile 0 0 48 theta)
    (when render?
      (q/save-frame "frames/f####.png"))))

(defn render-state
  [{:keys [rho centre] :as state}]
  (q/background 30)
  (q/stroke-weight 8)
  (u/rotate-around
   [rho centre]
   (q/with-translation [12 12]
     (doseq [shape (:shapes state)]
       (render-shape shape)))))

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
