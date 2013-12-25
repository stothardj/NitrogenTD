(ns nitrogentd.game.spawnlingpool
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.spawnling :only [spawn]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.pool :as pool]))

(def time-between-spawns 1000)
(def spawn-at-a-time 1)

;; Spawns n spawnlings at the start of the path. Mostly even spawning.
(deftype SpawnlingPool
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (let [[x y] (first path)]
      (letfn [(spawn-individual [] (spawn x y path))
              (spawn-creeps [] (pool/spawn-n spawn-at-a-time n spawn-individual))
              (new-pool-fn [creep-left path] (SpawnlingPool. creep-left path time))]
        (pool/timed-spawn this spawn-creeps  new-pool-fn last-spawn time-between-spawns))))
  (get-path [this] path))

(defn construct
  [n path]
  (let [last-spawn time]
    (SpawnlingPool. n path last-spawn)))
