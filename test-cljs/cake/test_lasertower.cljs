(ns cake.test.test-lasertower
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [cake.creep :only [Creep]]
        [cake.point :only [Point]]
        [cake.lasertower :only [LaserTower]])
  (:require [cemerick.cljs.test :as t]
            [cake.lasertower :as lasertower]))

(deftype MockCreep [x y]
  Creep
  (draw [this] nil)
  (move [this] this)
  (damage [this force] this)
  Point
  (get-point [this] [x y]))

(deftest basic-in-range
  (is (lasertower/in-range?
       (LaserTower. 0 0)
       (MockCreep. 0 0))))

(deftest horizontal-in-range
  (is (let [cx    5
            cy    7
            tx    (+ cx lasertower/range -1)
            ty    cy
            tower (lasertower/LaserTower. tx ty)
            creep (MockCreep. cx cy)
            ]
        (lasertower/in-range? tower creep))))
