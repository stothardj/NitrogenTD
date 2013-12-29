(ns nitrogentd.game.animation.quakeanimation
  (:use [nitrogentd.game.animation.animation :only [Animation]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.gamestate :only [time time-passed?]]))

;; In milliseconds
(def animation-length 500)

;; Draws a quake centered at x y
(deftype QuakeAnimation [start-time x y max-range]
  Animation
  (draw [this]
    (let [percent-complete (/ (- time start-time) animation-length)
          radius (* percent-complete max-range)]
      (set! (.-strokeStyle ctx) "rgb(255,200,0)")
      (set! (.-lineWidth ctx) 2)
      (.beginPath ctx)
      (.arc ctx x y radius 0 (* Math/PI 2) false)
      (.closePath ctx)
      (.stroke ctx)))
  (continues? [this] (not (time-passed? start-time animation-length))))
