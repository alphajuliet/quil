(ns my-sketch.hex2
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
                  :r 0.75}))}))

(defn update-shape
  [shape]
  (if (< (rand-int 200) 1)
    (-> shape
        (update :hue #(u/mod256 (inc %)))
        (update :r identity)
        (update :theta #(u/mod2pi (+ % (/ q/PI 3)))))
    ;; else
    shape))

(defn update-state
  [state]
  (-> state
      (update :rho #(+ % 0.005))
      (update :shapes #(map update-shape %))))

;;----------------
(defn tile
  "Draw a hex tile centred at [x y] with side d, rotated by theta, and a factor r."
  [x y d theta r]
  (let [d' (quot d 2)
        dx (* d' (q/cos (/ q/PI 3)))
        dy (* d' (q/sin (/ q/PI 3)))]
    (q/stroke-weight 2)
    (q/with-translation [x y]
      (q/with-rotation [theta]
        (q/line d' 0 dx dy)
        (q/line dx dy (- dx) dy)
        (q/line (- dx) dy (- d') 0)
        (q/line 0 0 d' 0)
        (q/line 0 0 (- dx) (- dy))
        (q/line (- dx) (- dy) dx (- dy))

        (q/line (- d') 0 (- (* r d')) 0)
        (q/line dx dy (* r dx) (* r dy))
        (q/line dx (- dy) (* r dx) (- (* r dy)))
        ))))

(defn render-shape
  [{:keys [x y theta hue brightness sat r]}]
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
  (q/frame-rate 24) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch hex2
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked snapshot
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
