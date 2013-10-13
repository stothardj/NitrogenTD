(ns nitrogentd.game.splitterpool
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.splitter :only [spawn-splitter]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.pool :as pool]))

(def time-between-spawns 3000)

(deftype SplitterPool
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (if-not (time-passed? last-spawn time-between-spawns)
      {:pool this :creep []}
      (let [[x y] (first path)
            {:keys [creep creep-left]} (pool/spawn-n 1 n #(spawn-splitter x y path))]
        (if (zero? creep-left)
          {:creep creep}
          {:creep creep
           :pool (SplitterPool. creep-left path time)})))))

(defn create-splitter-pool
  [n path]
  (let [last-spawn time]
    (SplitterPool. n path last-spawn)))
