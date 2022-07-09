(ns my-sketch.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn- rand-int-range
  "Integer range"
  [a b]
  (+ a (rand-int (- b a))))

(defn- rand-range
  "Floating point range"
  [a b]
  (+ a (rand (- b a))))

;;----------------
(defn new-circle
  "New random circle"
  []
  (let [w (q/width)
        h (q/height)
        d (rand-int-range 5 30)
        cx (int (rand-range (* w 0.25) (* w 0.75)))
        cy (int (rand-range (* h 0.25) (* h 0.75)))]
    {:hue (rand-int 255)
     :sat (rand-int-range 50 255)
     :size d
     :centre [cx cy]
     :radius (rand-int-range 25 (int (* w 0.23)))
     :angle (rand 360.0)
     :speed (/ 1.0 d)}))

(defn setup
  []
  (q/frame-rate 30) ;fps
  (q/color-mode :hsb)
  (let [n 20]
    {:background 0
     :circles (for [_ (range n)]
                (new-circle))}))

;;----------------
(defn update-circle
  [{:keys [speed] :as circle}]
  (-> circle
      (update :hue #(mod (+ % 0.2) 255))
      (update :angle #(+ % speed))))

(defn update-state
  [state]
  (update state :circles #(map update-circle %)))

;;----------------
(defn draw-circle
  [{:keys [angle centre size radius hue sat]}]
  (q/fill hue sat 255)
  (let [x (* radius (q/cos angle))
        y (* radius (q/sin angle))]
    (q/with-translation centre
      (q/ellipse x y size size))))

(defn draw-state
  [state]
  (q/background 0)
  (doseq [c (:circles state)]
    (draw-circle c)))

;;----------------
(q/defsketch my-sketch
  :title "You spin my circles right round"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ;; This sketch uses functional-mode middleware.
  ;; Check quil wiki for more info about middlewares and particularly
  ;; fun-mode.
  :middleware [m/fun-mode])
