(ns nitrogentd.game.level
  (:use [nitrogentd.game.wave :only [load-wave]]))

(defrecord Level [name path waves next])

(defn load-level
  "Sets atoms to starting values defined by level and first wave
   Returns the new level-info"
  [level path pools]
  (reset! path (:path level))
  (let [first-wave (first (:waves level))
        level-info {:level level
                    :wave first-wave}]
    (load-wave first-wave level-info pools)))
