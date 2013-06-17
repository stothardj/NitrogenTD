(ns cake.numberanimation
  (:use [cake.animation :only [Animation]]
        [cake.drawing :only [ctx]]
        [cake.gamestate :only [time]]))

(def animation-length 1000)

(deftype NumberAnimation [start-time number x y]
  Animation
  (draw [this]
    (let [diffy (/ (- start-time time) 20)
          cx x
          cy (+ y diffy)]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.4)")
      (set! (.-font ctx) "10pt Helvetica")
      (.fillText ctx number cx cy)))
  (continues? [this] (< time (+ start-time animation-length))))
      
          
