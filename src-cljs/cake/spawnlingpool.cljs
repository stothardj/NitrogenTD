(ns cake.spawnlingpool
  (:use [cake.pool :only [Pool]]
        [cake.spawnling :only [spawn-spawnling]]
        [cake.gamestate :only [time time-passed?]]))

;; Spawns n spawnlings at the start of the path. Each time called has a probability of prob to spawn a creep
(deftype SpawnlingPool
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (if-not (time-passed? last-spawn 1000)
      {:pool this :creep []}
      (let [creep-left (dec n)
            new-creep (let [[[cx cy]] path]
                        [(spawn-spawnling. cx cy path)])
            ]
        (if (zero? creep-left)
          {:creep new-creep}
          {:creep new-creep
           :pool (SpawnlingPool. creep-left path time)})))))

(defn create-spawnling-pool
  [n path]
  (let [last-spawn time]
    (SpawnlingPool. n path last-spawn)))
