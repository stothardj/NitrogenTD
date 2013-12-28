(ns nitrogentd.game.singlepool
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.pool :as pool]))

;; Spawn n creeps at the start of the path. Mostly even spawning.
(defrecord SinglePool
    [n path last-spawn time-between-spawns spawn-at-a-time spawn-fn]
  Pool
  (spawn-creep [this]
    (let [[x y] (first path)]
      (letfn [(spawn-individual [] (spawn-fn x y path))
              (spawn-creeps [] (pool/spawn-n spawn-at-a-time n spawn-individual))
              (new-pool-fn [creep-left] (assoc this :n creep-left :last-spawn time))]
        (pool/timed-spawn this spawn-creeps  new-pool-fn last-spawn time-between-spawns))))
  (get-path [this] path))
