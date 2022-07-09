(ns my-sketch.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.spec.alpha :as s]
            [spec-dict :refer [dict dict*]]))

(defn- rand-int-range
  "Integer range"
  [a b]
  (+ a (rand-int (- b a))))

(defn- rand-range
  "Floating point range"
  [a b]
  (+ a (rand (- b a))))

;;----------------
;; Define a circle
(s/def ::byte (s/and int? #(<= 0 % 255)))
(s/def ::circle (dict {:hue ::byte
                       :sat (s/and nat-int? #(<= 50 % 255))
                       :size (s/and nat-int? #(<= 5 % 30))
                       :centre (s/tuple nat-int? nat-int?)
                       :radius (s/and nat-int? #(>= % 25))
                       :angle (s/and float? #(<= 0. % 360.))
                       :speed (s/and float? #(<= 0.02 % 0.12))
                       :age ::byte
                       :rate (s/and float? #(<= 0.10 % 0.5))}))

(defn new-circle
  "New random circle"
  []
  (let [w (q/width)
        h (q/height)
        d (rand-int-range 4 30)
        cx (int (rand-range (* w 0.25) (* w 0.75)))
        cy (int (rand-range (* h 0.25) (* h 0.75)))]
    {:hue (rand-int 255)
     :sat (rand-int-range 60 255)
     :size d
     :centre [cx cy]
     :radius (rand-int-range 25 (int (* w 0.23)))
     :angle (rand 360.0)
     :speed (/ 0.6 d)
     :age 255.
     :rate (rand-range 0.10 0.25)}))

(defn setup
  "Set up the initial state"
  []
  (q/frame-rate 30) ;fps
  (q/color-mode :hsb)
  (let [n 50]
    {:background 0
     :circles (for [_ (range n)]
                (new-circle))}))

;;----------------
(defn update-circle
  [{:keys [speed rate] :as circle}]
  (-> circle
      (update :hue #(mod (+ % 0.2) 255))
      (update :angle #(+ % speed))
      (update :age #(- % rate))))

(defn add-circle
  [state _]
  (update state :circles #(conj % (new-circle))))

(defn add-random-circle
  [circles]
  (if (< (rand) 0.020)
    (conj circles (new-circle))
    circles))

(defn dead?
  [c]
  (neg? (:age c)))

(defn update-state
  [state]
  (-> state
      (update :circles #(map update-circle %))
      (update :circles #(remove dead? %))
      (update :circles #(add-random-circle %))))

;;----------------
(defn render-circle
  [{:keys [angle centre size radius hue sat age]}]
  (q/fill hue sat age)
  (let [x (* radius (q/cos angle))
        y (* radius (q/sin angle))]
    (q/with-translation centre
      (q/ellipse x y size size))))

(defn render-state
  [state]
  (q/background 0)
  (doseq [c (:circles state)]
    (render-circle c)))

;;----------------
(q/defsketch my-sketch
  :title "You spin my circles right round"
  :size [500 500]
  :setup setup
  :update update-state
  :draw render-state
  :mouse-clicked add-circle
  :features [:keep-on-top]
  :middleware [m/fun-mode])
