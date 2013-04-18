(ns cake.drawing
  (:require [cake.util :as util])
  )

;; These don't really need to be dynamic, but ctx seems similar
;; enought to *out* for stdout that it seemed like a good idea
;; to give it the same semantics
(def ^:dynamic canvas (util/by-id "game"))
(def ^:dynamic ctx (.getContext canvas "2d"))

(defn drawPath
  "Given a list of points '((x y) (x y) ...) draw the polygon on the given context. Does not close the path for you. Does not call stroke or fill for you"
  [points]
  (.beginPath ctx)
  (let [[x y] (first points)]
    (.moveTo ctx x y))
  (doseq [[x y] (rest points)]
    (.lineTo ctx x y)))

(defn clearCanvas
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
