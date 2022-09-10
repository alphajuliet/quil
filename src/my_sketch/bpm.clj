(ns my-sketch.bpm
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]))

;; Overall dimensions
(def sketch-size [500 500])

(def frame-rate 24)
(def bpm 126)
(def sec-per-beat (/ 60. bpm))

;;----------------
(defn initial-state
  "Define the initial state"
  ;; initial-state :: State
  []
  {:size 250 :hue 64})

(defn update-shape
  [{:keys [] :as shape}]
  )

(defn update-state
  [state]
  (-> state
      (assoc :size (u/saw-wave 125 250 sec-per-beat))
      (update :hue (comp u/mod256 #(+ % 0.5)))))

;;----------------
(defn render-shape
  [{:keys [size hue]}]
  (q/fill hue 127 255)
  (q/ellipse 250 250 size size))

(defn render-state
  [{:keys [] :as state}]
  (q/background 30)
  (render-shape state))

;;----------------
(defn setup
  []
  (q/frame-rate frame-rate) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch template
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
