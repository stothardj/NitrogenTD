(ns nitrogentd.game.pool)

(defprotocol Pool
  "Spawns a creeps its position"
  (spawn-creep [this] "Returns:
       {:creep => [some number, possibly zero, creep.]
        :pool => new pool. nil if pool is exhaused}"))

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
