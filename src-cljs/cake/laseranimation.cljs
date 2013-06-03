(ns cake.laseranimation
  (:use [cake.animation :only [Animation]]
        [cake.drawing :only [ctx]]
        )
  (:require [cake.creep :as creep]
            [cake.tower :as tower]
            [cake.line :as line]
            [cake.point :as point]
            )
  )

;; In milliseconds
(def animation-length 5000)

;; Draws a simple laser animation from the tower to the creep
(deftype LaserAnimation [start-time t c]
  Animation
  (draw [this time]
    (let [[tx ty] (point/get-point t)
          [cx cy] (point/get-point c)]
      (set! (.-fillStyle ctx) "rgb(255, 255, 255)")
      
      (.beginPath ctx)
      (.moveTo ctx tx ty)
      (.lineTo ctx cx cy)
      (.stroke ctx)))
  (continues? [this time] (< time (+ start-time animation-length)))
  )
