(ns cake.drawing
  (:require [cake.util :as util]
            [cake.line :as line]
            )
  )

;; These don't really need to be dynamic, but ctx seems similar
;; enought to *out* for stdout that it seemed like a good idea
;; to give it the same semantics
(def ^:dynamic canvas (util/by-id "game"))
(def ^:dynamic ctx (.getContext canvas "2d"))

(defn draw-path
  "Given a list of points '((x y) (x y) ...) draw the polygon on the given context. Does not close the path for you. Does not call stroke or fill for you"
  [points]
  (.beginPath ctx)
  (let [[x y] (first points)]
    (.moveTo ctx x y))
  (doseq [[x y] (rest points)]
    (.lineTo ctx x y)))

(defn clear-canvas
  []
  (let [w (.-width canvas)
        h (.-height canvas)]
    (.clearRect ctx 0 0 w h)))

(defn draw-at
  ([draw-fn x y]
     (draw-at draw-fn x y 0))
  ([draw-fn x y angle]
     (.translate ctx x y)
     (.rotate ctx angle)
     (draw-fn)
     (.rotate ctx (- angle))
     (.translate ctx (- x) (- y)))
  )

(defn draw-creep-path
  [path]
  (set! (.-strokeStyle ctx) "rgba(255,255,255,0.1)")
  (set! (.-lineWidth ctx) 50)
  (draw-path path)
  (.stroke ctx)
  )

(defn draw-debug-info
  [mouse-pos creep-path]
  (set! (.-fillStyle ctx) "rgb(255,0,0)")
  (let [[x y] mouse-pos]
    (.fillRect ctx (- x 5) (- y 5) 10 10))
  (set! (.-strokeStyle ctx) "rgb(255,0,0)")
  (set! (.-lineWidth ctx) 2)
  (draw-path creep-path)
  (.stroke ctx)
  (loop [path creep-path]
    (if (< (count path) 2)
      nil
      (let [[x1 y1] (first path)
            [x2 y2] (second path)
            [xi yi] (line/closest-point-on-line mouse-pos (line/get-line [x1 y1 x2 y2]))
            ]
        (.fillRect ctx (- xi 3) (- yi 3) 6 6)
        (recur (rest path)))))
  )
