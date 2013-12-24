(ns nitrogentd.game.spawnlingpool
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.spawnling :only [spawn-spawnling]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.pool :as pool]))

(def time-between-spawns 1000)
(def spawn-at-a-time 1)

;; Spawns n spawnlings at the start of the path. Mostly even spawning.
(deftype SpawnlingPool
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (if-not (time-passed? last-spawn time-between-spawns)
      {:pool this :creep []}
      (let [[x y] (first path)

            {:keys [creep creep-left]}
            (pool/spawn-n spawn-at-a-time n #(spawn-spawnling x y path))]
        (if (zero? creep-left)
          {:creep creep}
          {:creep creep
           :pool (SpawnlingPool. creep-left path time)})))))

(defn construct
  [n path]
  (let [last-spawn time]
    (SpawnlingPool. n path last-spawn)))
