(ns cake.spawnling
  (:use [cake.creep :only [Creep]]
        [cake.drawing :only [ctx]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            )
  )

(deftype Spawnling [x y]
  Creep
  (draw [this time]
    (set! (.-fillStyle ctx) "rgba(0, 255, 255, 0.2)")
    (let [t (util/to-radians (mod (/ time 5) 360))
          u (* 4 (Math/sin t))
          w (* 2 u) ;; pun
          ]
      (drawing/draw-at #(.fillRect ctx (- u) -8 w 16) x y)
      )))
