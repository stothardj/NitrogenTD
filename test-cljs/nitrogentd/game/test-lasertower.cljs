(ns nitrogentd.game.test-lasertower
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.point :only [Point]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.lasertower :as lasertower]))

(defn eq-any-order?
  "Returns true if sequences a and b contain equal elements. Order does not matter."
  [a b]
  (cond (every? empty? [a b]) true
        (empty? a) false
        (empty? b) false
        :else
        (let [[h & r] a
              [front back] (split-with #(not= h %) b)]
           (when back
             (eq-any-order? r (concat front (rest back)))))))

(deftest eq-any-order
  (is (eq-any-order? [] []))
  (is (not (eq-any-order? [] [1])))
  (is (not (eq-any-order? [1] [])))
  (is (eq-any-order? [1 2 3] [1 2 3]))
  (is (eq-any-order? [1 2 3] [3 1 2]))
  (is (not (eq-any-order? [1 2 4] [3 1 2])))
  (is (not (eq-any-order? [1 1 2 3] [3 2 1 2]))))

(deftype MockCreep [x y]
  Creep
  (draw [this] nil)
  (move [this] this)
  (damage [this force] this)
  Point
  (get-point [this] [x y]))

(deftest in-attack-range
  (is (lasertower/in-attack-range?
       (lasertower/construct-lasertower 0 0)
       (MockCreep. 0 0)))
  (is (let [cx    5
            cy    7
            tx    (+ cx lasertower/attack-range -1)
            ty    cy
            tower (lasertower/construct-lasertower tx ty)
            creep (MockCreep. cx cy)
            ]
        (lasertower/in-attack-range? tower creep))))

