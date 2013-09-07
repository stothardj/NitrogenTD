(ns nitrogentd.game.fright
  (:use [nitrogentd.game.statuseffect :only [StatusEffect]]
        [nitrogentd.game.gamestate :only [time-passed?]])
  (:require [nitrogentd.game.statuseffect :as s]))

(def max-stacking 2)

;; Fright causes a creep to run faster
(deftype Fright [start-time]
  StatusEffect
  (apply-effect [this stats]
    (let [old-speed (:speed stats)
          new-speed (* 4 old-speed)]
      (assoc stats :speed new-speed)))
  (add-effect [this effects]
    (let [existing-frights (filter (partial instance? Fright) effects)
          num-frights (count existing-frights)]
      (if (< num-frights max-stacking)
        (conj (filter s/continues? effects) this)
        effects)))
  (continues? [this] (not (time-passed? start-time 2000))))
