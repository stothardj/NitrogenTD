(ns cake.tower)

(defprotocol Tower
  "A tower defense tower"
  (draw [this] "Draws the tower")
  (attack [this creeps] "Possibly attack some of the creeps.
                         Returns a map
                          :creeps Updated creeps. Killed creeps removed.
                          :tower The updated tower to handle charging, cooldown, etc."))


        
