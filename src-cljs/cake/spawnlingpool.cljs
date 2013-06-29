(ns cake.spawnlingpool
  (:use [cake.pool :only [Pool]]
        [cake.spawnling :only [spawn-spawnling]]))

;; Spawns n spawnlings at the start of the path. Each time called has a probability of prob to spawn a creep
(deftype SpawnlingPool
    [n prob path]
  Pool
  (spawn-creep [this]
    (let [want-to-spawn (if (< (rand-int 100) prob) 1 0)
          num-spawned (min want-to-spawn n)
          creep-left (- n num-spawned)
          new-creep (repeat num-spawned
                            (let [[[cx cy]] path]
                              (spawn-spawnling. cx cy path)))
          ]
      (if (zero? creep-left)
        {:creep new-creep}
        {:creep new-creep
         :pool (SpawnlingPool. creep-left prob path)})
      )
    ))
