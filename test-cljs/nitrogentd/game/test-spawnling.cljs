(ns nitrogentd.game.test-spawnling
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.numberanimation :only [NumberAnimation]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.creep :as c]
            [nitrogentd.game.spawnling :as s]
            [nitrogentd.game.point :as p]))

(deftest value-type
  (is (= (s/spawn-spawnling 1 2 nil) (s/spawn-spawnling 1 2 nil)))
  (is (not= (s/spawn-spawnling 1 2 nil) (s/spawn-spawnling 1 3 nil))))

(deftest point
  (is (= [5 7] (p/get-point (s/spawn-spawnling 5 7 nil)))))

(deftest damage
  (let [creep (s/spawn-spawnling 1 2 nil)
        force 7 ;; Not enough to kill a spawnling
        {:keys [creeps animations]} (c/damage creep force)]
    (is (= 1 (count creeps)))
    (is (= 1 (count animations)))
    (is (= force (- (:health creep) (:health (first creeps)))))
    (is (instance? NumberAnimation (first animations))))
  (let [creep (s/spawn-spawnling 1 2 nil)
        force (:health s/stats)
        {:keys [creeps]} (c/damage creep force)]
    (is (empty? creeps))))
