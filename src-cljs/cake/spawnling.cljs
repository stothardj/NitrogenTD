(ns cake.spawnling
  (:use [cake.creep :only [Creep]]
        [cake.drawing :only [ctx]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
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
  (move [this]
    (Spawnling. (inc x) y path))
  )
