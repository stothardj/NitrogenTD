(ns cake.chargetower
  (:use [cake.tower :only [Tower]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.gamestate :only [time time-passed?]])
  (:require [cake.drawing :as drawing]
            [cake.point :as point]
            [cake.line :as line]
            [cake.creep :as creep]))

(def attack-range 100)
(def attack-cooldown 2500)
(def max-targets 1)

(deftype ChargeTower [x y cooldown-start]
  Tower
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(0, 200, 255, 0.3)")
    (drawing/draw-at (fn []
                       (.beginPath ctx)
                       (.moveTo ctx -6 6)
                       (.lineTo ctx -3 -6)
                       (.lineTo ctx 3 -6)
                       (.lineTo ctx 6 6)
                       (.closePath ctx)
                       (.fill ctx))
                     x y)
    )
  (attack [this creeps]
    {:creeps creeps
     :tower this})
  Point
  (get-point [this] [x y]))

(defn construct-chargetower
  "Constructs and returns a charge tower"
  [x y]
  (let [cooldown-start 0]
    (ChargeTower. x y cooldown-start)))

