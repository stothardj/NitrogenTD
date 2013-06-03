(ns cake.lasertower
  (:use [cake.tower :only [Tower]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.gamestate :only [time]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            )
  )

(deftype LaserTower [x y]
  Tower
  (draw [this]
    (let [angle (util/to-radians (mod (/ time 5) 360))]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.2)")
      (drawing/draw-at #(.fillRect ctx -8 -8 16 16) x y angle)
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.5)")
      (drawing/draw-at #(.fillRect ctx -5 -5 10 10) x y (- angle))
      ))
  Point
  (get-point [this] [x y])
  )
