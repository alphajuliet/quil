(ns my-sketch.yextor
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [my-sketch.util :as util]))

;;----------------
;; Define a circle

(defn new-circle
  "New circle"
  [centre]
  (let []
    {:hue 40
     :sat 255
     :age 255
     :centre centre}))

(defn setup
  "Set up the initial state"
  []
  (q/frame-rate 30) ;fps
  (q/color-mode :hsb)
  (let [start-pos [0 (/ (q/height) 4)]]
    {:background 0
     :xy start-pos
     :dxdy [4 2]
     :circles (list (new-circle start-pos))}))

;;----------------
(defn update-circle
  [circle]
  (let [decay 0.85]
    (-> circle
        (update :age #(int (* % decay))))))

(defn dead?
  [c]
  (<= (:age c) 1))

(defn bounce
  "Update dxdy based on the bounding rectangle"
  [[x y] [dx dy]]
  (let [dx' (if (< 0 x (q/width)) dx (- dx))
        dy' (if (< 0 y (q/height)) dy (- dy))]
    [dx' dy']))

(defn update-state
  [{:keys [xy dxdy] :as state}]
  (let [fw (/ (q/width) 2)
        fh (/ (q/height) 2)
        new-pos (util/v2mod+ xy dxdy [fw fh])]
    (-> state
        (assoc :xy new-pos)
        (assoc :dxdy (bounce new-pos dxdy))
        (update :circles #(conj % (new-circle new-pos)))
        (update :circles #(map update-circle %))
        (update :circles #(remove dead? %)))))

;;----------------
(defn render-circle
  [{:keys [hue sat centre age]}]
  (q/fill hue sat age)
  (let [[x y] centre
        size 40]
    (q/with-translation centre
      (q/ellipse x y size size))))

(defn render-state
  [state]
  (q/background (:background state))
  (doseq [c (:circles state)]
    (render-circle c)))

;;----------------
(q/defsketch yextor
  :title "Circle trail"
  :size [500 500]
  :setup setup
  :update update-state
  :draw render-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
