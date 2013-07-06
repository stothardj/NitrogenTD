(ns cake.hello
  (:use [cake.lasertower :only [construct-lasertower]]
        [cake.chargetower :only [construct-chargetower]]
        [cake.spawnling :only [spawn-spawnling]]
        [cake.spideree :only [spawn-spideree]]
        [cake.spawnlingpool :only [create-spawnling-pool]]
        [cake.spidereenest :only [create-spideree-nest]]
        [cake.drawing :only [canvas]]
        )
  (:require [clojure.browser.event :as event]
            [cake.drawing :as drawing]
            [cake.creep :as creep]
            [cake.tower :as tower]
            [cake.pool :as pool]
            [cake.util :as util]
            [cake.line :as line]
            [cake.point :as point]
            [cake.animation :as animation]
            [cake.gamestate :as gamestate]
            )
  )

(gamestate/tick)

(def creep-path '((30 40) (70 90) (70 200) (300 200) (500 400) (600 500) (700 250) (600 100) (500 200)  ))

(def towers (atom [(construct-lasertower 290 240)
                   (construct-lasertower 100 400)
                   (construct-chargetower 150 150)
                   ]))
(def creeps (atom [(spawn-spawnling 150 100 creep-path)
                   (spawn-spawnling 100 200 creep-path)
                   (spawn-spideree 250 250 creep-path)
                   ]))
(def animations (atom []))

(def pools (atom [(create-spideree-nest 23 creep-path)
                  (create-spawnling-pool 8 creep-path)]))

(def mouse-pos (atom nil))

(defn relative-mouse-pos
  [ev]
  (let [rect (.getBoundingClientRect canvas)
        x (- (.-clientX ev) (.-left rect))
        y (- (.-clientY ev) (.-top rect))]
    [x y]))

(defn- combine-spawns
  [v]
  (reduce
   (fn [accum spawn-result]
     (let [{:keys [creep pool]} spawn-result
           {:keys [creeps pools]} accum
           new-creeps (concat creep creeps)
           new-pools (if pool (conj pools pool) pools)
           ]
       {:creeps new-creeps
        :pools new-pools}))
   {} v))

(when canvas

  (event/listen canvas "click"
                (fn [ev]
                  (let [[x y] (relative-mouse-pos ev)]
                    (when-not (line/point-on-thick-path? [x y] creep-path 50)
                      (swap! towers (partial cons (construct-lasertower x y)))))))

  (event/listen canvas "mousemove"
                (fn [ev]
                  (let [p (relative-mouse-pos ev)]
                    (reset! mouse-pos p))))

  (util/crashingInterval
   (fn []
     (gamestate/tick)
     (drawing/clear-canvas)
     (drawing/draw-creep-path creep-path)
     (doseq [tower @towers]
       (tower/draw tower))
     (doseq [creep @creeps]
       (creep/draw creep))
     (doseq [anim @animations]
       (animation/draw anim))
     
     (swap! creeps
            #(->> %
                  (map creep/move)
                  (filter (complement nil?))))

     (let [{na :animations
            nc :creeps
            nt :towers} (tower/attack-all @towers @creeps)]
       (reset! towers nt)
       (reset! creeps nc)
       (swap! animations (partial concat na)))

     (swap! animations (partial filter animation/continues?))

     (let [{new-creeps :creeps
            new-pools :pools} (-> (map pool/spawn-creep @pools)
                                  (combine-spawns))]
       (swap! creeps (partial concat new-creeps))
       (reset! pools new-pools))
     )
   40)
  )

