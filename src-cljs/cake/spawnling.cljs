(ns cake.spawnling
  (:use [cake.creep :only [Creep]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.gamestate :only [time]]
        [cake.numberanimation :only [NumberAnimation]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.line :as line]
            )
  )

(deftype Spawnling [x y health path]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(255, 0, 255, 0.3)")
    (let [t (util/to-radians (mod (/ time 3) 360))
          u (* 4 (Math/sin t))
          w (* 2 u) ;; pun
          ]
      (drawing/draw-at #(.fillRect ctx (- u) -4 w 8) x y)
      ))
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            [newx newy] (line/move-towards [x y] goal 1)
            sq-dist (line/sq-point-to-point-dist (list newx newy) goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)]
        (Spawnling. newx newy health new-path)
        )))
  (damage [this force]
    (let [new-health (- health force)]
      (when (pos? new-health)
        {:creep (Spawnling. x y new-health path)
         :animation [(NumberAnimation. time force x y)]})))
  Point
  (get-point [this] [x y])
  )
