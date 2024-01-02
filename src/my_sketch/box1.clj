(ns my-sketch.template
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as u]))

;; Overall dimensions
(def sketch-size [500 500])
(def grid-spacing 50)

;;----------------
(defn initial-state
  "Define the initial state"
  []
  )

(defn update-shape
  [{:keys [] :as shape}]
  )

(defn update-state
  [state]
  (-> state
      ))

;;----------------
(defn render-shape
  [{:keys []}]
  )

(defn render-state
  [{:keys [] :as state}]
  (q/background 30)
  )

;;----------------
(defn setup
  []
  (q/frame-rate 30) ;fps
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
