(ns nitrogen.game.statuseffect)

(defprotocol StatusEffect
  "A status effect on a creep"
  (apply-effect [this stats] "Applies the effect to stats, returning a new stats object")
  (continues? [this] "Returns true if the status effect should remain"))
