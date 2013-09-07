(ns nitrogentd.game.creep)

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
  (apply-effect [this effect] "Attempts to apply status effect to creep.
                               Not guarenteed to be added because creep could have
                               certain immunities or the effect may not be allowed
                               to stack.
                               Returns
                                 {:creeps [new creeps]
                                  :animations [new animations]}"))
