(ns nitrogentd.test.test-lasertower
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.creep :only [Creep]]
        [nitrogentd.point :only [Point]]
        )
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.lasertower :as lasertower]))

(deftype MockCreep [x y]
  Creep
  (draw [this] nil)
  (move [this] this)
  (damage [this force] this)
  Point
  (get-point [this] [x y]))

(deftest basic-in-range
  (is (lasertower/in-attack-range?
       (lasertower/construct-lasertower 0 0)
       (MockCreep. 0 0))))

(deftest horizontal-in-range
  (is (let [cx    5
            cy    7
            tx    (+ cx lasertower/attack-range -1)
            ty    cy
            tower (lasertower/construct-lasertower tx ty)
            creep (MockCreep. cx cy)
            ]
        (lasertower/in-attack-range? tower creep))))
