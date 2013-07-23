(ns nitrogentd.test.lasertower
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.point :only [Point]]
        )
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.lasertower :as lasertower]))

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

