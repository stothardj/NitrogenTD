(ns cake.spideree
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

(deftype Spideree [x y health path]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(255, 0, 0, 0.3)")
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "rgba(255, 0, 0, 0.3)")
    (drawing/draw-at (fn []
                       (let [osc (* 6 (Math/sin (/ time 200)))
                             nosc (- osc)]
                       (.fillRect ctx -4 -4 8 8)
                       (.beginPath ctx)
                       (.moveTo ctx -6 osc)
                       (.lineTo ctx 6 nosc)
                       (.moveTo ctx 6 osc)
                       (.lineTo ctx -6 nosc)
                       (.stroke ctx)
                       )) x y))
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            [newx newy :as newp] (line/move-towards [x y] goal 1)
            sq-dist (line/sq-point-to-point-dist newp goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)]
        (Spideree. newx newy health new-path))))
  (damage [this force]
    (let [hit (min force 300)
          new-health (- health hit)]
      (when (pos? new-health)
        {:creep (Spideree. x y new-health path)
         :animation [(NumberAnimation. time hit x y)]})))
  Point
  (get-point [this] [x y]))
