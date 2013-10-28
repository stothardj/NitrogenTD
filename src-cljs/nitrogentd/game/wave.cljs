(ns nitrogentd.game.wave)

(defrecord Wave [name pools])

(defn load-wave
  "Sets atoms to starting values defined by the wave"
  [wave pools]
  (reset! pools (:pools wave)))
