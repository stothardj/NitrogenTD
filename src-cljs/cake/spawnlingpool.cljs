(ns cake.spawnlingpool
  (:use [cake.spawner :only [Spawner]
         cake.spawnling :only [Spawnling]
         ])
  )

;; Spawns n spawnlings near position x, y. Produces 1 spawnling per freq. Start-time is when this pool started spawning.
(deftype SpawnlingPool
    [x y n freq start-time]
  Spawner
  (spawn-creep [this time]
    (let [time-diff (- time start-time)
          spawn-in-time (inc (/ time-diff freq)) ;; todo: rounding
          num-spawned (min spawn-in-time n)
          creep-left (- n num-spawned)
          new-creep (repeat num-spawned
                            (.Spawnling (x y nil)))
          ]
      (if (zero? creep-left)
        {:creep new-creep}
        {:creep new-creep
         :pool (.SpawnlingPool x y creep-left freq time)})
      )
    ))
