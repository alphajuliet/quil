(ns my-sketch.monads
  (:require [clojure.algo.monads :as m]))

(defn inc-s [x]
  (fn [state]
    [(inc x) (conj state :inc)]))

(defn double-s [x]
  (fn [state]
    [(* 2 x) (conj state :double)]))

(defn dec-s [x]
  (fn [state]
    [(dec x) (conj state :dec)]))

(defn do-things [x]
  "Monadic state"
  (m/domonad m/state-m
             [a (inc-s x)
              b (double-s a)
              c (dec-s b)
              d (dec-s c)]
             d))

;; Same as do-things but using m-chain
(m/with-monad m/state-m
  (def do-things-1
    (m/m-chain [inc-s double-s dec-s dec-s])))

;; The End
