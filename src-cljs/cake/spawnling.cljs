(ns cake.spawnling
  (:use [cake.creep :only [Creep]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.gamestate :only [time]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.line :as line]
            )
  )

(deftype Spawnling [x y path]
  Creep
  (draw [this]
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
            angle (line/angle-to-point [x y] goal)
            newx (+ x (Math/cos angle))
            newy (+ y (Math/sin angle))
            sq-dist (line/sq-point-to-point-dist (list newx newy) goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)
            ]
        (Spawnling. newx newy new-path)
        )))
  Point
  (get-point [this] [x y])
  )
