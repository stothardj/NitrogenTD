(ns cake.spawnlingpool
  (:use [cake.pool :only [Pool]]
        [cake.spawnling :only [Spawnling]]
        [cake.gamestate :only [time]]
        )
  )

;; Spawns n spawnlings near position x, y. Each time called has a probability of prob to spawn a creep. Start-time is when this pool started spawning.
(deftype SpawnlingPool
    [x y n prob path]
  Pool
  (spawn-creep [this]
    (let [want-to-spawn (if (< (rand-int 100) prob) 1 0)
          num-spawned (min want-to-spawn n)
          creep-left (- n num-spawned)
          new-creep (repeat num-spawned
                            (let [cx (+ x (rand-int 20) -10)
                                  cy (+ y (rand-int 20) -10)]
                              (Spawnling. cx cy path)))
          ]
      (if (zero? creep-left)
        {:creep new-creep}
        {:creep new-creep
         :pool (SpawnlingPool. x y creep-left prob path)})
      )
    ))
