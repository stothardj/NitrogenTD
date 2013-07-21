(ns nitrogentd.creep)

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
  )
