(ns cake.spawnlingpool
  (:use [cake.spawner :only [Spawner]]
        [cake.spawnling :only [Spawnling]]
        )
  )

;; Spawns n spawnlings near position x, y. Each time called has a probability of prob to spawn a creep. Start-time is when this pool started spawning.
(deftype SpawnlingPool
    [x y n prob path]
  Spawner
  (spawn-creep [this time]
    (let [want-to-spawn (if (< (rand-int 100) prob) 1 0)
          num-spawned (min want-to-spawn n)
          creep-left (- n num-spawned)
          new-creep (repeat num-spawned
                            (Spawnling. x y path))
          ]
      (if (zero? creep-left)
        {:creep new-creep}
        {:creep new-creep
         :pool (SpawnlingPool. x y creep-left prob)})
      )
    ))
