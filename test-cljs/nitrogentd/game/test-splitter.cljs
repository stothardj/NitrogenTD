(ns nitrogentd.game.test-splitter
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.numberanimation :only [NumberAnimation]]
        [nitrogentd.game.slow :only [Slow]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.creep :as c]
            [nitrogentd.game.splitter :as s]
            [nitrogentd.game.line :as l]
            [nitrogentd.game.point :as p]))

(deftest value-type
  (is (= (s/spawn-splitter 1 2 nil) (s/spawn-splitter 1 2 nil)))
  (is (not= (s/spawn-splitter 1 2 nil) (s/spawn-splitter 1 3 nil))))

(deftest get-point
  (is (= [5 7] (p/get-point (s/spawn-splitter 5 7 nil)))))

(deftest move
  (let [path '((5 7) (1 3))
        creep (s/spawn-splitter 1 2 path)
        begin-sq-dist (l/sq-point-to-point-dist (first path)
                                                (p/get-point creep))
        moved (c/move creep)
        end-sq-dist (l/sq-point-to-point-dist (first path)
                                              (p/get-point moved))]
    (is (< end-sq-dist begin-sq-dist))))

(deftest damage
  (let [creep (s/spawn-splitter 1 2 nil)
        force 7 ;; Not enough to kill a splitter
        {:keys [creeps animations]} (c/damage creep force)]
    (is (= 1 (count creeps)))
    (is (= 1 (count animations)))
    (is (= force (- (:health creep) (:health (first creeps)))))
    (is (instance? NumberAnimation (first animations))))
  (let [creep (s/spawn-splitter 1 2 nil)
        force (:health s/stats)
        {:keys [creeps]} (c/damage creep force)]
    (is (= 2 (count creeps)))))

(deftest add-effect
  (let [creep (s/spawn-splitter 1 2 nil)
        effect (Slow. 0)
        {:keys [creeps]} (c/add-effect creep effect)]
    (is (= 1 (count creeps)))
    (let [new-creep (first creeps)
          new-effects (:status-effects new-creep)]
      (is (= 1 (count new-effects)))
      (is (= effect (first new-effects))))))
