(ns nitrogentd.game.roachernest
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.roacher :only [spawn]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.pool :as pool]))

(def time-between-spawns 10000)
(def spawn-at-a-time 1)

(deftype RoacherNest
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (let [[x y] (first path)]
      (letfn [(spawn-individual [] (spawn x y path))
              (spawn-creeps [] (pool/spawn-n spawn-at-a-time n spawn-individual))
              (new-pool-fn [creep-left path] (RoacherNest. creep-left path time))]
        (pool/timed-spawn this spawn-creeps  new-pool-fn last-spawn time-between-spawns))))
  (get-path [this] path))

(defn construct
  [n path]
  (let [last-spawn time]
    (RoacherNest. n path last-spawn)))
