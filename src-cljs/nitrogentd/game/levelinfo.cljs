(ns nitrogentd.game.levelinfo)

;; Record of current state of game
(defrecord LevelInfo [levels waves])

(defn following
  "Takes the current level info, not as an atom.
   Returns the corresponding level-info for the next thing."
  [info]
  (cond (:waves info)  (assoc info :waves (rest (:waves info)))
        (:levels info) {:levels (rest (:levels info))
                        :waves (:waves (second (:level info)))}))

(defn load
  "Sets atoms to starting values defined by the level info"
  [info path pools]
  (let [level (first (:levels info))
        wave (first (:waves info))]
    (.log js/console "Level" (clj->js level))
    (.log js/console "Wave" (clj->js wave))
    (reset! path (:path level))
    (reset! pools (:pools wave))))
