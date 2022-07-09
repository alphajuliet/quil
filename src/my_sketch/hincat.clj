(ns my-sketch.hincat
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.spec.alpha :as s]
            [spec-dict :refer [dict dict*]]))

(defn- v2+
  "Vector add"
  [[x y] [u v]]
  [(+ x u) (+ y v)])

;;----------------
(defn new-rect
  [i]
  (let [height 50]
    {:centre [0 (- (* i (* height 2)))]
     :height height
     :hue (mod (* i 10) 256)}))

(defn init-state
  []
  (let [nrects 100]
    {:rects (for [i (range nrects)]
              (new-rect i))}))

(defn setup
  []
  (q/frame-rate 30) ;fps
  (q/color-mode :hsb)
  (init-state))

;;----------------
(defn update-rect
  [rect]
  (-> rect
      (update :centre #(v2+ % [0 1.2]))))

(defn dead?
  [{:keys [centre]}]
  (let [[x y] centre]
    (and (> x (q/width)) (> y (q/height)))))

(defn update-state
  [state]
  (-> state
      (update :rects #(map update-rect %))
      (update :rects #(remove dead? %))))

;;----------------
(defn rect-centred
  "Render a rectangle with centre at [x y]"
  [x y w h]
  (q/rect (- x (/ w 2)) (- y (/ h 2)) w h))

(defn render-rect
  [{:keys [centre hue height]}]
  (let [frame-w (q/width)
        frame-h (q/height)
        width (q/sqrt (+ (q/sq frame-w) (q/sq frame-h)))]
    (q/fill hue 0xff 0xff)
    (q/with-translation centre
      (rect-centred 0 0
                    width height))))

(defn render-state
  [state]
  (let [angle (- (/ Math/PI 4))]
    (q/background 0)
    (q/with-rotation [angle]
      (doseq [r (:rects state)]
       (render-rect r)))))

;;----------------
(q/defsketch hincat
  :title "hincat"
  :size [500 500]
  :setup setup
  :update update-state
  :draw render-state
  :features [:keep-on-top]
  ;; This sketch uses functional-mode middleware.
  ;; Check quil wiki for more info about middlewares and particularly
  ;; fun-mode.
  :middleware [m/fun-mode])

;; The End
