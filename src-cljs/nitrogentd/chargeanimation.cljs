(ns nitrogentd.chargeanimation
  (:use [nitrogentd.animation :only [Animation]]
        [nitrogentd.drawing :only [ctx]]
        [nitrogentd.gamestate :only [time]]
        )
  (:require [nitrogentd.creep :as creep]
            [nitrogentd.tower :as tower]
            [nitrogentd.line :as line]
            [nitrogentd.point :as point]
            )
  )

;; In milliseconds
(def animation-length 200)

;; Draws a simple laser animation from the tower to the creep
(deftype ChargeAnimation [start-time t c]
  Animation
  (draw [this]
    (let [[tx ty] (point/get-point t)
          [cx cy] (point/get-point c)
          alpha (- 1 (/ (- time start-time) animation-length))
          laser-color (+ "rgba(50, 150, 255," alpha ")")
          ]
      (set! (.-strokeStyle ctx) laser-color)
      (set! (.-lineWidth ctx) 3)
      (.beginPath ctx)
      (.moveTo ctx tx ty)
      (.lineTo ctx cx cy)
      (.stroke ctx)))
  (continues? [this] (< time (+ start-time animation-length)))
  )
