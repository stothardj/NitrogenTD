(ns nitrogentd.game.drawing
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.line :as line]
            )
  )

;; These don't really need to be dynamic, but ctx seems similar
;; enought to *out* for stdout that it seemed like a good idea
;; to give it the same semantics
(def ^:dynamic canvas (util/by-id "game"))
(def ^:dynamic ctx (when canvas (.getContext canvas "2d")))

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
  (draw-path path)
  (set! (.-strokeStyle ctx) "rgb(255,255,255)")
  (set! (.-lineWidth ctx) 50)
  (.stroke ctx)
  (set! (.-strokeStyle ctx) "rgb(20,20,50)")
  (set! (.-lineWidth ctx) 45)
  (.stroke ctx)
  )
