(ns cake.roacher
  (:use [cake.creep :only [Creep]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.gamestate :only [time]]
        [cake.numberanimation :only [NumberAnimation]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.line :as line]
            )
  )

(deftype Roacher [x y health path]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(0, 200, 0, 0.3)")
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "rgba(0, 200, 0, 0.3)")
    (let [fleg (+ (* 3 (Math/sin (/ time 100))) 4)
          bleg (- (* 3 (Math/cos (/ time 100))) 4)]
      (drawing/draw-at (fn []
                         (.fillRect ctx -6 -2 12 4)
                         (.fillRect ctx 6 -3 4 6)
                         (.beginPath ctx)
                         (.moveTo ctx 6 -3)
                         (.lineTo ctx -3 -5)
                         (.moveTo ctx -4 2)
                         (.lineTo ctx bleg 6)
                         (.moveTo ctx 4 2)
                         (.lineTo ctx fleg 6)
                         (.stroke ctx)
                         ) x y)))
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            [newx newy :as newp] (line/move-towards [x y] goal 1)
            sq-dist (line/sq-point-to-point-dist newp goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)]
        (Roacher. newx newy health new-path))))
  (damage [this force]
    (let [new-health (- health force)]
      (when (pos? new-health)
        {:creeps [(Roacher. x y new-health path)]
         :animations [(NumberAnimation. time force x y)]})))
  Point
  (get-point [this] [x y])
  )

(defn spawn-roacher
  "Create and return Roacher with given params."
  [x y path]
  (let [health 1000]
    (Roacher. x y health path)))
