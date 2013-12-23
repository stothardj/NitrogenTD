(ns nitrogentd.game.roacher
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time]]
        [nitrogentd.game.numberanimation :only [NumberAnimation]]
        [nitrogentd.game.creepstats :only [map->CreepStats]]
        [nitrogentd.game.fright :only [Fright]]
        )
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.statuseffect :as statuseffect]
            )
  )

(def stats (map->CreepStats {:health 2000 :speed 1 :reward 60}))

(deftype Roacher [x y health path facing status-effects]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(0, 200, 0, 0.7)")
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "rgba(0, 200, 0, 0.7)")
    (let [fleg (+ (* 3 (Math/sin (/ time 100))) 4)
          bleg (- (* 3 (Math/cos (/ time 100))) 4)]
      (drawing/draw-at (fn []
                         (when (= facing 'left)
                           (.scale ctx -1 1))
                         (.fillRect ctx -6 -2 12 4)
                         (.fillRect ctx 6 -3 4 6)
                         (.beginPath ctx)
                         (.moveTo ctx 6 -3)
                         (.lineTo ctx -3 -5)
                         (.moveTo ctx -4 2)
                         (.lineTo ctx bleg 5)
                         (.moveTo ctx 4 2)
                         (.lineTo ctx fleg 5)
                         (.stroke ctx)
                         (when (= facing 'left)
                           (.scale ctx -1 1))
                         ) x y)))
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            current-stats (statuseffect/apply-effects status-effects stats)
            move-speed (:speed current-stats)
            {newx :x newy :y new-path :path} (creep/move-along-path [x y] move-speed 100 path)
            new-facing (if (> newx x) 'right 'left)]
        (Roacher. newx newy health new-path new-facing status-effects))))
  (damage [this force]
    (let [new-health (- health force)
          new-status-effects (statuseffect/add-effect (Fright. time) status-effects)
          new-creeps (if (pos? new-health)
                       [(Roacher. x y new-health path facing new-status-effects)]
                       [])]
      {:creeps new-creeps
       :animations [(NumberAnimation. time force x y)]}))
  (add-effect [this effect]
    (let [new-effects (statuseffect/add-effect effect status-effects)]
      {:creeps [(Roacher. x y health path facing new-effects)]}))
  Point
  (get-point [this] [x y])
  )

(defn spawn-roacher
  "Create and return Roacher with given params."
  [x y path]
  (let [health (:health stats)]
    (Roacher. x y health path 'right [])))
