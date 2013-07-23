(ns nitrogentd.game.spidereenest
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.spideree :only [spawn-spideree]]
        [nitrogentd.game.gamestate :only [time time-passed?]]))

(defn- add-noise-to-path [path]
  (map (fn [[x y]]
         [(+ x -10 (rand-int 20))
          (+ y -10 (rand-int 20))])
       path))

(deftype SpidereeNest
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (if-not (time-passed? last-spawn 5000)
      {:pool this :creep []}
      (let [want-to-spawn 5
            num-spawned (min want-to-spawn n)
            creep-left (- n num-spawned)
            new-creep (repeatedly num-spawned
                                  #(let [[[cx cy]] path
                                         nx (+ cx -25 (rand-int 50))
                                         ny (+ cy -25 (rand-int 50))
                                         p (add-noise-to-path path)]
                                     (spawn-spideree nx ny p)))
            ]
        (if (zero? creep-left)
          {:creep new-creep}
          {:creep new-creep
           :pool (SpidereeNest. creep-left path time)})))))

(defn create-spideree-nest
  [n path]
  (let [last-spawn time]
    (SpidereeNest. n path last-spawn)))
