(ns nitrogentd.game.level)

(defrecord Level [name path waves])

(defn load-level
  "Sets atoms to starting values defined by level and first wave"
  [level path]
  (reset! path (:path level)))
