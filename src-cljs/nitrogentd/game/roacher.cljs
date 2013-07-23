(ns nitrogentd.game.roacher
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time]]
        [nitrogentd.game.numberanimation :only [NumberAnimation]]
        )
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.line :as line]
            )
  )

(deftype Roacher [x y health path facing last-shot]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(0, 200, 0, 0.3)")
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "rgba(0, 200, 0, 0.3)")
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
            move-speed (if (< (- time last-shot) 1000) 4 1)
            [newx newy :as newp] (line/move-towards [x y] goal move-speed)
            new-facing (if (> newx x) 'right 'left)
            sq-dist (line/sq-point-to-point-dist newp goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)]
        (Roacher. newx newy health new-path new-facing last-shot))))
  (damage [this force]
    (let [new-health (- health force)]
      (when (pos? new-health)
        {:creeps [(Roacher. x y new-health path facing time)]
         :animations [(NumberAnimation. time force x y)]})))
  Point
  (get-point [this] [x y])
  )

(defn spawn-roacher
  "Create and return Roacher with given params."
  [x y path]
  (let [health 10000]
    (Roacher. x y health path 'right 0)))
