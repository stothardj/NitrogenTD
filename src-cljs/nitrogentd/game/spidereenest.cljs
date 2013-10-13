(ns nitrogentd.game.spidereenest
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.spideree :only [spawn-spideree]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.pool :as pool]))

(def time-between-spawns 5000)
(def spawn-at-a-time 5)

(defn- add-noise-to-path [path]
  (map (fn [[x y]]
         [(+ x -10 (rand-int 20))
          (+ y -10 (rand-int 20))])
       path))

(deftype SpidereeNest
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (if-not (time-passed? last-spawn time-between-spawns)
      {:pool this :creep []}
      (let [[x y] (first path)

            {:keys [creep creep-left]}
            (pool/spawn-n spawn-at-a-time n 
                          #(let [[[cx cy]] path
                                 nx (+ cx -25 (rand-int 50))
                                 ny (+ cy -25 (rand-int 50))
                                 p (add-noise-to-path path)]
                             (spawn-spideree nx ny p)))]
        (if (zero? creep-left)
          {:creep creep}
          {:creep creep
           :pool (SpidereeNest. creep-left path time)})))))

(defn create-spideree-nest
  [n path]
  (let [last-spawn time]
    (SpidereeNest. n path last-spawn)))
