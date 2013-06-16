(ns cake.lasertower
  (:use [cake.tower :only [Tower]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.gamestate :only [time]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.point :as point]
            [cake.line :as line]
            [cake.creep :as creep]
            )
  )

(deftype LaserTower [x y]
  Tower
  (draw [this]
    (let [angle (util/to-radians (mod (/ time 5) 360))]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.2)")
      (drawing/draw-at #(.fillRect ctx -8 -8 16 16) x y angle)
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.5)")
      (drawing/draw-at #(.fillRect ctx -5 -5 10 10) x y (- angle))))
  (attack [this creeps]
    (let [nc (for [c creeps
                   :let [new-creep
                         (if (in-range? this c)
                           (creep/damage c 300)
                           c)
                         ]
                   :when new-creep] new-creep)]
      {:tower this
       :creeps nc}))
  Point
  (get-point [this] [x y])
  )

(def range 40)

;; Make range param and move to tower?
(defn in-range?
  [tower creep]
  (let [creep-p (point/get-point creep)
        tower-p (point/get-point tower)
        r2 (* range range)]
    (< (line/sq-point-to-point-dist creep-p tower-p) r2)))

