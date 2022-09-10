(ns my-sketch.grid1
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.spec.alpha :as s]
            [my-sketch.util :as u]))

;;----------------
;; Overall dimensions
(def sketch-size [500 500])

;;----------------
(defn make-horizontal-lines
  "Make the horizontal lines"
  ;; make-horizontal-lines :: Int -> Vector Int
  [n]
  (let [total (* n n)]
    (for [a (range total)
          :let [b (inc a)]
          :when (and (< b total)
                     (< (mod a n) (mod b n)))]
      [a b])))

(defn make-vertical-lines
  "Make the vertical grid lines"
  ;; make-vertical-lines :: Int -> Vector Int
  [n]
  (let [total (* n n)]
    (for [a (range total)
          :let [b (+ a n)]
          :when (< b total)]
      [a b])))


(defn make-grid
  "Make an n x n rectangular grid"
  ;; make-grid :: Int -> {:nodes Vec2 :edges Vec2}
  [n]
  (let [[w h] sketch-size
        dw (/ w n)
        dh (/ h n)]
    {:nodes (for [x (range 0 w dw)
                  y (range 0 h dh)]
              [x y])
     :edges (concat (make-horizontal-lines n)
                    (make-vertical-lines n))}))

(defn initial-state
  "Define the initial state"
  ;; initial-state :: State
  []
  {:post [#(s/valid? ::state %)]}
  (into {:rho 0.
         :centre (u/v2scale 0.5 sketch-size)}
        (make-grid 10)))

(defn update-node
  ;; update-node :: Vec2 -> Vec2
  [[x y]]
  [(+ x (q/random -0.5 0.5)) (+ y (q/random -0.5 0.5))])

(defn update-state
  ;; update-state :: State -> State
  [state]
  (-> state
      #_(update :rho (partial + 0.005))
      (update :nodes #(map update-node %))))

;;----------------
(defn render-state
  ;; render-state :: State -> IO
  [{:keys [rho centre nodes edges] :as state}]
  (q/background 30)
  (q/fill 128 255 128)
  (q/stroke 180 128 255)
  (u/rotate-around
   [rho centre]
   (q/with-translation [20 20]
     (doseq [[a b] edges]
       (let [[x1 y1] (nth nodes a)
             [x2 y2] (nth nodes b)]
         (q/line x1 y1 x2 y2))))))

;;----------------
(defn setup
  ;; setup :: State
  []
  (q/frame-rate 10) ;fps
  (q/color-mode :hsb)
  (initial-state))

;;----------------
(q/defsketch grid1
  :title ""
  :size sketch-size
  :setup setup
  :update update-state
  :draw render-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

;; The End
