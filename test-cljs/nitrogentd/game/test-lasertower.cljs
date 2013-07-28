(ns nitrogentd.game.test-lasertower
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.testing.asserts :only [eq-any-order?]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.lasertower :as l]
            [nitrogentd.game.spawnling :as s]))

(defn simple-creep
  "Construct a simple creep for testing"
  [x y]
  (s/spawn-spawnling x y nil))

;; Otherwise change other tests too
(deftest max-targets
  (is (= 3 l/max-targets)))

(deftest in-attack-range
  (is (l/in-attack-range?
       (l/construct-lasertower 0 0)
       (s/spawn-spawnling 0 0 nil)))
  (is (let [cx    5
            cy    7
            tx    (+ cx l/attack-range -1)
            ty    cy
            tower (l/construct-lasertower tx ty)
            creep (simple-creep cx cy)
            ]
        (l/in-attack-range? tower creep)))
  (is (not (let [cx    5
                 cy    7
                 tx    (+ cx l/attack-range 1)
                 ty    cy
                 tower (l/construct-lasertower tx ty)
                 creep (simple-creep cx cy)]
             (l/in-attack-range? tower creep)))))

(deftest choose-targets
  (let [tower (l/construct-lasertower 0 0)
        creeps [(simple-creep 0 0)]
        [attacked safe] (l/choose-targets tower creeps)]
    (is (= creeps attacked))
    (is (empty? safe)))
  (let [tower (l/construct-lasertower 0 0)
        creeps [(simple-creep 0 0)
                (simple-creep 1 0)
                (simple-creep 0 1)
                (simple-creep l/attack-range 1)]
        valid-creeps [(simple-creep 0 0)
                      (simple-creep 1 0)
                      (simple-creep 0 1)]
        invalid-creeps [(simple-creep l/attack-range 1)]
        [attacked safe] (l/choose-targets tower creeps)]
    (is (eq-any-order? attacked valid-creeps))
    (is (eq-any-order? safe invalid-creeps)))
  (let [tower (l/construct-lasertower 0 0)
        creeps (repeat (+ l/max-targets 7) (simple-creep 0 0))
        [attacked safe] (l/choose-targets tower creeps)]
    (is (= l/max-targets (count attacked)))
    (is (= 7 (count safe)))))