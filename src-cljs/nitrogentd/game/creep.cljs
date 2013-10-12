(ns nitrogentd.game.creep
  (:require [nitrogentd.game.line :as line]))

(defprotocol Creep
  "A creep to be killed by the towers"
  (draw [this] "Draws the creep")
  (move [this] "Move, follow path, returns a new creep")
  (damage [this force] "Does damage of a certain amount of force.
                        Force is only one factor in how much health the creep loses.
                        The type of creep, status effects, etc. could also play a roll.
                        Returns
                          {:creeps [new creeps]
                           :animations [new animations]}")
  (add-effect [this effect] "Attempts to add status effect to creep.
                             Not guarenteed to be added because creep could have certain
                             immunities or the effect may not be allowed to stack.
                             Returns
                               {:creeps [new creeps]
                                :animations [new animations]}"))

(defn move-along-path
  "Moves a creep some distance along it's path. If the creep gets within corner dist
   to a turn it moves onto looking at the next turn
   Returns
    {:x new point x coord
     :y new point y coord
     :path new path. nil if at the end}"
  [start-point move-speed corner-dist path]
  (let [goal (first path)
        [newx newy :as new-point] (line/move-towards start-point goal move-speed)
        sq-dist (line/sq-point-to-point-dist new-point goal)
        new-path (if (< sq-dist corner-dist)
                   (rest path)
                   path)]
    {:x newx
     :y newy
     :path new-path}))
