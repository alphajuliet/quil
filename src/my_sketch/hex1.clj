(ns my-sketch.hex1
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]
            [my-sketch.hexutil :as hex]))

;; Overall dimensions
(def sketch-size [500 500])
(def grid-spacing 50)
(def render? false)

;;----------------
(defn initial-state
  "Define the initial state"
  []
  (let [[w h] sketch-size
        d (/ grid-spacing 2)]
    {:rho 0 ; overall rotation
     :centre (u/v2scale 0.5 [w h]) ; overall centre
     :shapes (for [q (range (- (quot w d)) (quot w d))
                   r (range (- (quot h d)) (quot h d))]
               (let [[x y] (hex/hex->coord [q r])]
                 {:x (* x d)
                  :y (* y d)
                  :hue (-> 3 rand-int (* 85) (+ 25))
                  :brightness 255
                  :theta 0}))}))

(defn update-shape
  [shape]
  (if (< (rand-int 64) 1)
    (-> shape
        (update :hue #(u/mod256 (inc %)))
        (update :theta #(u/mod2pi (+ % (/ q/PI 3)))))
    ;; else
    shape))

(defn update-state
  [state]
  (-> state
      (update :rho #(+ % 0.01))
      (update :shapes #(map update-shape %))))

;;----------------
(defn tile
  "Draw a hex tile centred at [x y] with side d and rotated by theta"
  [x y d theta]
  (let [d' (quot d 2)
        dx (* d' (q/cos (/ q/PI 3)))
        dy (* d' (q/sin (/ q/PI 3)))]
    (q/stroke-weight 4)
    (q/with-rotation [theta]
      (q/line (- x d') y (+ x d') y)
      ;; (q/line (- x dx) (+ y dy) (+ x dx) (- y dy))
      (q/line x y (+ x dx) (- y dy))
      ;; (q/line (- x dx) (- y dy) (+ x dx) (+ y dy))
      (q/line x y (+ x dx) (+ y dy))
      )))

(defn render-shape
  [{:keys [x y theta hue brightness]}]
  (q/stroke hue 255 brightness)
  (q/with-translation [x y]
    (tile 0 0 (- grid-spacing 2) theta)
    (when render?
      (q/save-frame "frames/f####.png"))))

(defn render-state
  [{:keys [rho centre] :as state}]
  (q/background 30)
  (u/rotate-around
   [rho centre]
   (q/with-translation [25 25]
     (doseq [shape (:shapes state)]
       (render-shape shape)))))

(defn snapshot
  [state _]
  (q/save-frame "image/snapshot.png")
  state)

;;----------------
(defn setup
  []
  (q/frame-rate 24) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch hex1
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked snapshot
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
