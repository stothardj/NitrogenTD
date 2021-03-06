(ns nitrogentd.game.animation.numberanimation
  (:use [nitrogentd.game.animation.animation :only [Animation]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.gamestate :only [time time-passed?]]))

(def animation-length 1000)

(deftype NumberAnimation [start-time number x y]
  Animation
  (draw [this]
    (let [diffy (/ (- start-time time) 20)
          cx x
          cy (+ y diffy)
          font-size (int (/ number 20))
          font (+ font-size "pt Helvetica")
      ]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.4)")
      (set! (.-font ctx) font)
      (.fillText ctx number cx cy)))
  (continues? [this] (not (time-passed? start-time animation-length))))
