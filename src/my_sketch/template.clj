(ns my-sketch.box1
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [my-sketch.util :as u]))

;; Overall dimensions
(def sketch-size [500 500])
(def grid-spacing 50)

;;----------------
(defn initial-state
  "Define the initial state"
  [])

(defn update-state
  [state]
  (-> state
      ))

;;----------------
(defn render-state
  [{:keys [] :as state}]
  (q/background 30)
                                        ; set camera in point [200, 200, 200]
                                        ; the camera looks in direction of point [0, 0, 0]
                                        ; \"up\" vector is [0, 0, -1]
  (q/camera 200 200 200 0 0 0 0 0 -1)
                                        ; draw a box of size 100 at the [0, 0, 0] point
  (q/stroke-weight 2)
  (q/stroke 255)
  (q/fill 127)
  (q/box 100)
  (q/ellipse 250 250 100 100)
                                        ; draw red X axis
  (q/stroke 255 0 0)
  (q/line 0 0 0 100 0 0)
                                        ; draw green Y axis
  (q/stroke 0 255 0)
  (q/line 0 0 0 0 100 0)
                                        ; draw blue Z axis
  (q/stroke 0 0 255)
  (q/line 0 0 0 0 0 100)

  #_(q/with-translation [250 250 0]
    (q/box 100)
    ;; draw red X axis
    (q/stroke 255 0 0)
    (q/line 0 0 0 100 0 0)
    ;; draw green Y axis
    (q/stroke 0 255 0)
    (q/line 0 0 0 0 100 0)
    ;; draw blue Z axis
    (q/stroke 0 0 255)
    (q/line 0 0 0 0 0 100)))

;;----------------
(defn setup
  []
  (q/frame-rate 30) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch box1
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
