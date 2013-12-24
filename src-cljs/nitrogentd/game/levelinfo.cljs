(ns nitrogentd.game.levelinfo)

;; Record of current state of game
;; The current level and wave is at the front of each list. When
;; a wave ends the next wave is retrieved from the waves list. If
;; there are no more waves the next level is retrieved from the levels
;; list and waves is populated from the waves in that level. This means
;; following will never provide a level info with either levels or waves
;; being empty. It there are no more levels or waves following returns nil
;; to represent the end of the game.
(defrecord LevelInfo [levels waves])

(defn following
  "Takes the current level info, not as an atom.
   Returns the corresponding level-info for the next thing."
  [info]
  (cond (> (count (:waves info)) 1)  (assoc info :waves (rest (:waves info)))
        (not-empty (:levels info)) (let [next-levels (rest (:levels info))
                                         next-waves (:waves (first next-levels))]
                                     (when-not (and (empty? next-levels) (empty? next-waves))
                                       (map->LevelInfo
                                        {:levels next-levels
                                         :waves next-waves})))))

(defn load
  "Sets atoms to starting values defined by the level info"
  [info paths pools]
  (let [level (first (:levels info))
        wave (first (:waves info))]
    (reset! paths (:paths level))
    (reset! pools (:pools wave))))
