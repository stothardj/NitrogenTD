(ns nitrogentd.game.fright
  (:use [nitrogentd.game.statuseffect :only [StatusEffect]]
        [nitrogentd.game.gamestate :only [time-passed?]])
  (:require [nitrogentd.game.statuseffect :as s]))

(def max-stacking 2)
(def effect-duration 2000)

;; Fright causes a creep to run faster
(deftype Fright [start-time]
  StatusEffect
  (apply-effect [this stats]
    (let [old-speed (:speed stats)
          new-speed (* 4 old-speed)]
      (assoc stats :speed new-speed)))
  (add-effect [this effects]
    (let [current-effects (filter s/continues? effects)
          existing-frights (filter (partial instance? Fright) current-effects)
          num-frights (count existing-frights)]
      (if (< num-frights max-stacking)
        (conj current-effects this)
        current-effects)))
  (continues? [this] (not (time-passed? start-time effect-duration))))
