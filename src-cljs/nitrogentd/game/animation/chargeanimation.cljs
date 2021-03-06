(ns nitrogentd.game.animation.chargeanimation
  (:use [nitrogentd.game.animation.animation :only [Animation]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.gamestate :only [time time-passed?]])
  (:require [nitrogentd.game.creep.creep :as creep]
            [nitrogentd.game.tower.tower :as tower]
            [nitrogentd.game.line :as line]
            [nitrogentd.game.point :as point]))

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
  (continues? [this] (not (time-passed? start-time animation-length))))
