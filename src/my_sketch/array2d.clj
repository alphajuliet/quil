(ns my-sketch.array2d
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]))

;;----------------
(defn setup
  []
  (let [width 640
        height 340
        x0 (/ width 2)
        y0 (/ height 2)]
    (q/frame-rate 24) ;fps
    (q/color-mode :hsb)
    {:width width
     :height height
     :centre-x x0
     :centre-y y0
     :delta -2
     :maxDistance (q/dist x0 y0 width height)}))

;;----------------
(defn update-state
  [{:keys [delta centre-x centre-y width height] :as state}]
  (let [w (/ width 2)]
    (-> state
        (assoc :centre-x (+ w (u/sin-wave w 5)))
        (assoc :maxDistance (q/dist centre-x centre-y width height)))))

;;----------------
(defn render-state
  [state]
  (q/background 0)
  (q/stroke-weight 4)
  (let [{:keys [centre-x centre-y maxDistance]} state]
    (doseq [x (range 0 640 10)
            y (range 0 340 10)]
      (q/stroke (* 256 (/ (q/dist centre-x centre-y x y) maxDistance)))
      (q/point x y))))

(defn snapshot
  [state _]
  (q/save-frame "image/snapshot.png")
  state)

;;----------------
(q/defsketch array2d
  :title ""
  :size [640 360]
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked snapshot
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
