(ns my-sketch.hex4
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
        d (/ grid-spacing (q/sqrt 3))
        offset (rand-int 128)]
    {:rho (/ q/PI 3) ; overall rotation
     :centre (u/v2scale 0.5 [w h]) ; overall centre
     :shapes (for [q (range (- (quot w d)) (quot w d))
                   r (range (- (quot h d)) (quot h d))]
               (let [[x y] (hex/hex->coord [q r])]
                 {:x (* x d)
                  :y (* y d)
                  :type (rand-int 2)
                  :hue (-> 2 rand-int (* 127) (+ offset))
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
  [x y d theta r type]
  (let [d' (quot d 2)
        dx (* d' (q/cos (u/pi-on 3)))
        dy (* d' (q/sin (u/pi-on 3)))]
    (q/stroke-weight 3)
    (q/no-fill)
    (q/with-translation [x y]
      (q/with-rotation [theta]
        (if (zero? type)
          (do
            (q/line (- dx) (- dy) dx dy)
            (q/bezier d' 0 0 0 0 0 dx (- dy))
            (q/bezier (- d') 0 0 0 0 0 (- dx) (+ dy)))
          ;; else type 1
          (do
            (q/line (- dx) (- dy) dx dy)
            (q/bezier d' 0 0 0 0 0 (- dx) dy)
            (q/bezier (- d') 0 0 0 0 0 (+ dx) (- dy))))))))

(defn render-shape
        [{:keys [x y theta hue brightness r type]}]
        ;; Slowly cycle the saturation of the tiles
        (q/stroke hue (+ 127 (u/sin-wave 127 10)) brightness)
        (q/with-translation [x y]
          (tile 0 0 (- grid-spacing 0) theta r type)))

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
