(ns nitrogentd.game.slow
  (:use [nitrogentd.game.statuseffect :only [StatusEffect]]
        [nitrogentd.game.gamestate :only [time-passed?]]))

;; Slow causes a creep to run slower
(deftype Slow [start-time]
  StatusEffect
  (apply-effect [this stats]
    (let [old-speed (:speed stats)
          new-speed (/ old-speed 2)]
      (assoc stats :speed new-speed)))
  (continues? [this] (not (time-passed? start-time 2000))))
