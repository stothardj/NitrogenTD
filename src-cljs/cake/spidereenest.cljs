(ns cake.spidereenest
  (:use [cake.pool :only [Pool]]
        [cake.spideree :only [Spideree]]
        [cake.gamestate :only [time]]))

(deftype SpidereeNest
    [n prob path]
  Pool
  (spawn-creep [this]
    (let [want-to-spawn (if (< (rand-int 200) prob) 5 0)
          num-spawned (min want-to-spawn n)
          creep-left (- n num-spawned)
          new-creep (repeatedly num-spawned
                                #(let [[[cx cy]] path
                                       nx (+ cx -25 (rand-int 50))
                                       ny (+ cy -25 (rand-int 50))
                                       ]
                                   (Spideree. nx ny 1000 path time)))
          ]
      (if (zero? creep-left)
        {:creep new-creep}
        {:creep new-creep
         :pool (SpidereeNest. creep-left prob path)})
      )
    ))
