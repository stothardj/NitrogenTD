(ns nitrogentd.game.fright
  (:use [nitrogentd.game.statuseffect :only [StatusEffect]]
        [nitrogentd.game.gamestate :only [time-passed?]]))

;; Fright causes a creep to run faster
(deftype Fright [start-time]
  StatusEffect
  (apply-effect [this stats]
    (let [old-speed (:speed stats)
          new-speed (* 4 old-speed)]
      (assoc stats :speed new-speed)))
  (continues? [this] (not (time-passed? start-time 2000))))
