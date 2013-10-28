(ns nitrogentd.game.wave)

(defrecord Wave [name pools next])

(defn load-wave
  "Sets atoms to starting values defined by the wave"
  [wave pools]
  (reset! pools (:pools wave)))
