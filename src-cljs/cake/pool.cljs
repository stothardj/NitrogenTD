(ns cake.pool)

(defprotocol Pool
  "Spawns a creeps its position"
  (spawn-creep [this] "Returns a map. {:creep => [some number, possibly zero, creep.], :pool => new pool}. :creep will always be present. :pool will not be present once the pool is exhaused (no creeps left)")
  )
