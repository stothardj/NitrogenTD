(ns cake.spawner)

(defprotocol Spawner
  "Spawns a creeps its position"
  (spawn-creep [this time] "Returns a map. {:creep => [some number, possibly zero, creep.], :spawner => new spawner}. :creep will always be present. :pool will not be present once the spawner is exhaused (no creeps left)")
  )
