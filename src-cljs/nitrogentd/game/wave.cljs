(ns nitrogentd.game.wave)

(defrecord Wave [name pools next])

(defn load-wave
  "Sets atoms to starting values defined by the wave.
   Returns the new level-info"
  [wave level-info pools]
  (reset! pools (:pools wave))
  {:level (:level level-info)
   :wave wave})
