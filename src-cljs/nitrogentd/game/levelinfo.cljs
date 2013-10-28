(ns nitrogentd.game.levelinfo)

;; Record of current state of game
(defrecord LevelInfo [levels waves])

(defn following
  "Takes the current level info, not as an atom.
   Returns the corresponding level-info for the next thing."
  [info]
  (cond (not-empty (:waves info))  (assoc info :waves (rest (:waves info)))
        (not-empty (:levels info)) (let [next-levels (rest (:levels info))
                                         next-waves (:waves (first next-levels))]
                                     (when-not (and (empty? next-levels) (empty? next-waves))
                                       {:levels next-levels
                                        :waves next-waves}))))

(defn load
  "Sets atoms to starting values defined by the level info"
  [info path pools]
  (let [level (first (:levels info))
        wave (first (:waves info))]
    (reset! path (:path level))
    (reset! pools (:pools wave))))
