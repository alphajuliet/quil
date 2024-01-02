(ns my-sketch.bpm2
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]))

;; Overall dimensions
(def sketch-size [500 500])

(def frame-rate 12)
(def bpm 90)
(def sec-per-beat (/ 60. bpm))

;;----------------
(defn initial-state
  "Define the initial state"
  ;; initial-state :: State
  []
  {:r 100})

(defn update-state
  [state]
  (-> state
      (assoc :r (u/sin-wave 0 100 sec-per-beat))))

;;----------------
(defn render-shape
  ;; render-shape :: Float -> IO
  [r]
  (q/with-translation [250 250]
    (q/line r 0 (- r) 0)
    (u/line (u/rθ->xy [r (u/pi-on 1.5)]) (u/rθ->xy [r (u/pi-on 0.6)]))
    (u/line (u/rθ->xy [r (u/pi-on 3)]) (u/rθ->xy [r (u/pi-on 0.75)]))))

(defn render-state
  ;; render-state :: State -> IO
  [{:keys [r] :as state}]
  (q/background 30)
  (q/stroke 127 127 255)
  (q/stroke-weight 2)
  (render-shape r))

;;----------------
(defn setup
  []
  (q/frame-rate frame-rate) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch bpm2
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
