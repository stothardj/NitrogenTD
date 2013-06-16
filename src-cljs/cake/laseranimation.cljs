(ns cake.laseranimation
  (:use [cake.animation :only [Animation]]
        [cake.drawing :only [ctx]]
        [cake.gamestate :only [time]]
        )
  (:require [cake.creep :as creep]
            [cake.tower :as tower]
            [cake.line :as line]
            [cake.point :as point]
            )
  )

;; In milliseconds
(def animation-length 10)

;; Draws a simple laser animation from the tower to the creep
(deftype LaserAnimation [start-time t c]
  Animation
  (draw [this]
    (let [[tx ty] (point/get-point t)
          [cx cy] (point/get-point c)
          alpha (- 1 (/ (- time start-time) animation-length))
          laser-color (+ "rgba(255, 0, 0," alpha ")")
          ]
      (set! (.-strokeStyle ctx) laser-color)
      (set! (.-lineWidth ctx) 2)
      (.beginPath ctx)
      (.moveTo ctx tx ty)
      (.lineTo ctx cx cy)
      (.stroke ctx)))
  (continues? [this] (< time (+ start-time animation-length)))
  )
