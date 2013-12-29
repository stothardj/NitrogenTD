(ns nitrogentd.game.tower.test-lasertower
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.testing.asserts :only [eq-any-order?]]
        [nitrogentd.game.animation.laseranimation :only [LaserAnimation]]
        [nitrogentd.game.animation.numberanimation :only [NumberAnimation]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.tower.lasertower :as l]
            [nitrogentd.game.tower.tower :as tow]
            [nitrogentd.game.creep.creep :as creep]
            [nitrogentd.game.creep.spawnling :as s]))

(defn simple-creep
  "Construct a simple creep for testing"
  [x y]
  (s/spawn x y nil))

;; Otherwise change other tests too
(deftest max-targets
  (is (= 3 (:max-targets l/stats))))

(deftest in-attack-range
  (is (l/in-attack-range?
       (l/construct 0 0)
       (simple-creep 0 0)))
  (is (let [cx    5
            cy    7
            tx    (+ cx (:attack-range l/stats) -1)
            ty    cy
            tower (l/construct tx ty)
            creep (simple-creep cx cy)
            ]
        (l/in-attack-range? tower creep)))
  (is (not (let [cx    5
                 cy    7
                 tx    (+ cx l/attack-range 1)
                 ty    cy
                 tower (l/construct tx ty)
                 creep (simple-creep cx cy)]
             (l/in-attack-range? tower creep)))))

(deftest choose-targets
  (let [tower (l/construct 0 0)
        creeps [(simple-creep 0 0)]
        [attacked safe] (l/choose-targets tower creeps)]
    (is (= creeps attacked))
    (is (empty? safe)))
  (let [tower (l/construct 0 0)
        creeps [(simple-creep 0 0)
                (simple-creep 1 0)
                (simple-creep 0 1)
                (simple-creep (:attack-range l/stats) 1)]
        valid-creeps [(simple-creep 0 0)
                      (simple-creep 1 0)
                      (simple-creep 0 1)]
        invalid-creeps [(simple-creep (:attack-range l/stats) 1)]
        [attacked safe] (l/choose-targets tower creeps)]
    (is (eq-any-order? attacked valid-creeps))
    (is (eq-any-order? safe invalid-creeps)))
  (let [tower (l/construct 0 0)
        creeps (repeat (+ (:max-targets l/stats) 7) (simple-creep 0 0))
        [attacked safe] (l/choose-targets tower creeps)]
    (is (= (:max-targets l/stats) (count attacked)))
    (is (= 7 (count safe)))))

(deftest attack-creep
  (let [tower (l/construct 0 0)
        creep (simple-creep 0 0)
        {:keys [creeps animations]} (l/attack-creep tower creep)]
    (is (= 1 (count creeps)))
    (is (= 2 (count animations)))
    (is (not= creep (first creeps)))
    (is (some (partial instance? NumberAnimation) animations))
    (is (some (partial instance? LaserAnimation) animations))))

(deftest attack
  (let [tower (l/construct 0 0)]
    (is (= (tow/map->AttackResult
            {:damage-result
             (creep/map->DamageResult
              {:creeps []
               :animations []
               :reward 0})
             :tower tower})
           (tow/attack tower [])))))
