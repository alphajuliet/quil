(ns my-sketch.hex3
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]
            [my-sketch.hexutil :as hex]))

;; Overall dimensions
(def sketch-size [500 500])
(def grid-spacing 50)
(def frame-rate 30)

;;----------------
(defn initial-state
  "Define the initial state"
  []
  (let [[w h] sketch-size
        d (/ grid-spacing (q/sqrt 3))]
    {:rho (/ q/PI 3) ; overall rotation
     :centre (u/v2scale 0.5 [w h]) ; overall centre
     :shapes (for [q (range (- (quot w d)) (quot w d))
                   r (range (- (quot h d)) (quot h d))]
               (let [[x y] (hex/hex->coord [q r])]
                 {:x (* x d)
                  :y (* y d)
                  :hue (-> 2 rand-int (* 127) (+ 25))
                  :brightness 255
                  :sat 0
                  :theta 0
                  :theta-target 0
                  :r 0.75}))}))

(defn update-shape
  [{:keys [theta-target] :as shape}]
  (if (< (rand-int 250) 1)
    (-> shape
        (update :hue #(u/mod256 (+ 2 %)))
        (update :r identity)
        (update :theta-target (partial + (u/pi-on 3.))))
    ;; else
    (-> shape
        (update :theta #(if (< % theta-target) (+ % (u/pi-on (* 3. 25))) %)))))

(defn update-state
  [state]
  (-> state
      (update :rho #(+ % (u/pi-on (* (u/one-on (u/sin-wave 0.04 20)) frame-rate))))
      (update :shapes #(map update-shape %))))

;;----------------
(defn tile
  "Draw a hex tile centred at [x y] with side d, rotated by theta, and a factor r."
  [x y d theta r]
  (let [d' (quot d 2)
        dx (* d' (q/cos (u/pi-on 3)))
        dy (* d' (q/sin (u/pi-on 3)))]
    (q/stroke-weight 2)
    (q/with-translation [x y]
      (q/with-rotation [theta]
        (q/line dx dy (- dx) dy)
        (q/line d' 0 (- d') 0)
        (q/line (- dx) (- dy) dx (- dy))))))

(defn render-shape
  [{:keys [x y theta hue brightness r]}]
  ;; Slowly cycle the saturation of the tiles
  (q/stroke hue (+ 127 (u/sin-wave 127 10)) brightness)
  (q/with-translation [x y]
    (tile 0 0 (- grid-spacing 0) theta r)))

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
  (q/frame-rate frame-rate) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch hex3
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked snapshot
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
