(ns nitrogentd.game.level
  (:use [nitrogentd.game.wave :only [load-wave]]))

(defrecord Level [name path waves])

(defn load-level
  "Sets atoms to starting values defined by level and first wave"
  [level path pools]
  (reset! path (:path level))
  (load-wave (first (:waves level)) pools)
  )
