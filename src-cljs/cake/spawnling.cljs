(ns cake.spawnling
  (:use [cake.creep :only [Creep]]
        [cake.drawing :only [ctx]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.line :as line]
            )
  )

(deftype Spawnling [x y path]
  Creep
  (draw [this time]
    (set! (.-fillStyle ctx) "rgba(255, 0, 255, 0.3)")
    (let [t (util/to-radians (mod (/ time 3) 360))
          u (* 4 (Math/sin t))
          w (* 2 u) ;; pun
          ]
      (drawing/draw-at #(.fillRect ctx (- u) -4 w 8) x y)
      ))
  ;; TODO: Move math to line.js
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            goalx (first goal)
            goaly (second goal)
            dx (- goalx x)
            dy (- goaly y)
            at (Math/atan (/ dy dx))
            angle (if (> 0 dx) (+ at Math/PI) at)
            newx (+ x (Math/cos angle))
            newy (+ y (Math/sin angle))
            sq-dist (line/sq-point-to-point-dist (list newx newy) (list goalx goaly))
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)
            ]
        (Spawnling. newx newy new-path)
        )))
  )
