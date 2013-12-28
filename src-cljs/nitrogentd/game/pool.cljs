(ns nitrogentd.game.pool
  (:use [nitrogentd.game.gamestate :only [time-passed?]]))

(defprotocol Pool
  "Spawns a creeps its position"
  (spawn-creep [this] "Returns:
       {:creep => [some number, possibly zero, creep.]
        :pool => new pool. nil if pool is exhaused}")
  (get-path [this] "Returns the path this pool is on"))

(defn spawn-n
  "Spawn up to num-spawned creep from a pool with num-left creep left
   Returns
    {:creep [spawned creep]
     :creep-left the number of creep left to spawn}"
  [num-spawned num-left spawn-fn]
  (let [actually-spawned (min num-spawned num-left)
        creep-left (- num-left actually-spawned)
        creep (repeatedly actually-spawned spawn-fn)]
    {:creep creep
     :creep-left creep-left}))

(defn timed-spawn
  "Spawns creep if a set amount of time passed.
   Returns
   {:creep [spawned creep]
    :pool pool to use}"
  [pool spawn-fn new-pool-fn last-spawn time-between-spawns]
  (if-not (time-passed? last-spawn time-between-spawns)
    {:pool pool :creep []}
    (let [{:keys [creep creep-left]} (spawn-fn)
          path (get-path pool)]
      (if (zero? creep-left)
        {:creep creep}
        {:creep creep
         :pool (new-pool-fn creep-left)}))))
