(ns nitrogentd.game.test-spideree
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.numberanimation :only [NumberAnimation]]
        [nitrogentd.game.slow :only [Slow]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.creep :as c]
            [nitrogentd.game.spideree :as s]
            [nitrogentd.game.line :as l]
            [nitrogentd.game.gamestate :as g]
            [nitrogentd.game.point :as p]))

;; A time after spawn the spideree would move
(def move-time 10)
;; A time after spawn the spideree would stand still
(def stand-time 1100)

(defn spawn-at-time
  "Spawns a spideree at a given spawn time. Eliminates randomness from construction."
  [x y path spawn-time]
  (s/Spideree. x y (:health s/stats) path spawn-time []))

(deftest value-type
  (is (= (spawn-at-time 1 2 nil 0) (spawn-at-time 1 2 nil 0)))
  (is (not= (spawn-at-time 1 2 nil 0) (spawn-at-time 1 3 nil 0))))

(deftest scuttle-now?
  (is (s/scuttle-now? move-time))
  (is (not (s/scuttle-now? stand-time))))

(deftest get-point
  (is (= [5 7] (p/get-point (s/spawn-spideree 5 7 nil)))))

(deftest move
  (binding [g/time (+ move-time 700)]
    (let [path '((5 7) (1 3))
          creep (spawn-at-time 1 2 path 700)
          begin-sq-dist (l/sq-point-to-point-dist (first path)
                                                  (p/get-point creep))
          moved (c/move creep)
          end-sq-dist (l/sq-point-to-point-dist (first path)
                                                (p/get-point moved))]
      (is (< end-sq-dist begin-sq-dist))))
  (binding [g/time (+ stand-time 700)]
    (let [path '((5 7) (1 3))
          creep (spawn-at-time 1 2 path 700)
          old-pos (p/get-point creep)
          not-moved (c/move creep)
          new-pos (p/get-point not-moved)]
      (is (= old-pos new-pos)))))

(deftest damage
  (let [creep (s/spawn-spideree 1 2 nil)
        force 7 ;; Not enough to kill a spideree
        {:keys [creeps animations]} (c/damage creep force)]
    (is (= 1 (count creeps)))
    (is (= 1 (count animations)))
    (is (= force (- (:health creep) (:health (first creeps)))))
    (is (instance? NumberAnimation (first animations))))
  (let [creep (s/spawn-spideree 1 2 nil)
        force (:health s/stats)
        {:keys [creeps]} (c/damage creep force)]
    (is (= 1 (count creeps)))
    (is (= s/max-damage (- (:health creep) (:health (first creeps)))))))

(deftest add-effect
  (let [creep (s/spawn-spideree 1 2 nil)
        effect (Slow. 0)
        {:keys [creeps]} (c/add-effect creep effect)]
    (is (= 1 (count creeps)))
    (let [new-creep (first creeps)
          new-effects (:status-effects new-creep)]
      (is (= 1 (count new-effects)))
      (is (= effect (first new-effects))))))
