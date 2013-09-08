(ns nitrogentd.game.slow
  (:use [nitrogentd.game.statuseffect :only [StatusEffect]]
        [nitrogentd.game.gamestate :only [time-passed?]])
  (:require [nitrogentd.game.statuseffect :as s]))

(def max-stacking 4)
(def effect-duration 2000)

;; Slow causes a creep to run slower
(deftype Slow [start-time]
  StatusEffect
  (apply-effect [this stats]
    (let [old-speed (:speed stats)
          new-speed (/ old-speed 2)]
      (assoc stats :speed new-speed)))
  (add-effect [this effects]
    (let [current-effects (filter s/continues? effects)
          existing-slows (filter (partial instance? Slow) current-effects)
          num-slows (count existing-slows)]
      (if (< num-slows max-stacking)
        (conj current-effects this)
        current-effects)))
  (continues? [this] (not (time-passed? start-time effect-duration))))
