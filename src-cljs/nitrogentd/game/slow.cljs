(ns nitrogentd.game.slow
  (:use [nitrogentd.game.statuseffect :only [StatusEffect]]
        [nitrogentd.game.gamestate :only [time-passed?]])
  (:require [nitrogentd.game.statuseffect :as s]))

(def max-stacking 4)

;; Slow causes a creep to run slower
(deftype Slow [start-time]
  StatusEffect
  (apply-effect [this stats]
    (let [old-speed (:speed stats)
          new-speed (/ old-speed 2)]
      (assoc stats :speed new-speed)))
  (add-effect [this effects]
    (let [existing-slows (filter (partial instance? Slow) effects)
          num-slows (count existing-slows)]
      (if (< num-slows max-stacking)
        (conj (filter s/continues? effects) this)
        effects)))
  (continues? [this] (not (time-passed? start-time 2000))))
