(ns nitrogentd.game.hello
  (:use [nitrogentd.game.lasertower :only [construct-lasertower]]
        [nitrogentd.game.chargetower :only [construct-chargetower]]
        [nitrogentd.game.spawnling :only [spawn-spawnling]]
        [nitrogentd.game.spideree :only [spawn-spideree]]
        [nitrogentd.game.roacher :only [spawn-roacher]]
        [nitrogentd.game.spawnlingpool :only [create-spawnling-pool]]
        [nitrogentd.game.spidereenest :only [create-spideree-nest]]
        [nitrogentd.game.drawing :only [canvas]]
        [domina :only [by-id set-text!]]
        [domina.events :only [listen! unlisten!]]
        )
  (:require [clojure.browser.event :as event]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.tower :as tower]
            [nitrogentd.game.pool :as pool]
            [nitrogentd.game.util :as util]
            [nitrogentd.game.line :as line]
            [nitrogentd.game.point :as point]
            [nitrogentd.game.animation :as animation]
            [nitrogentd.game.gamestate :as gamestate]
            [nitrogentd.game.lasertower :as lasertower]
            [nitrogentd.game.chargetower :as chargetower]
            [goog.dom.forms :as forms]
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
                   (spawn-roacher 300 300 creep-path)
                   ]))
(def animations (atom []))

(def pools (atom [(create-spideree-nest 23 creep-path)
                  (create-spawnling-pool 8 creep-path)]))

(def mouse-pos (atom nil))

(defn relative-mouse-pos
  [ev]
  (let [rect (.getBoundingClientRect canvas)
        x (- (:clientX ev) (.-left rect))
        y (- (:clientY ev) (.-top rect))]
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

(defn construct-tower [x y]
  (if (forms/getValue (util/by-id "Laser Tower"))
    (construct-lasertower x y)
    (construct-chargetower x y)))

(defn show-preview [x y]
  (if (forms/getValue (util/by-id "Laser Tower"))
    (lasertower/preview x y)
    (chargetower/preview x y)))

(defn game-loop []
  (gamestate/tick)
  (drawing/clear-canvas)
  (drawing/draw-creep-path creep-path)
  (doseq [tower @towers]
    (tower/draw tower))
  (doseq [creep @creeps]
    (creep/draw creep))
  (doseq [anim @animations]
    (animation/draw anim))

  (apply show-preview @mouse-pos)
  
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
    (reset! pools new-pools)))

(def paused (atom false))
(def events-registered (atom false))

(declare start-game)

(defn deregister-events []
  "Deregister all event listeners which should only take place during gameplay.
   Does not deregister the pause button otherwise unpausing would never happen.
   Deregistering pause is done separately right before start-game registers it
   again with the new handle."
  (unlisten! (by-id "game")))

(defn unpause []
  (gamestate/unpause-time)
  (set-text! (by-id "pause") "Pause")
  (reset! paused false)
  (unlisten! (by-id "pause"))
  (start-game))

(defn pause [handle]
  (gamestate/pause-time)
  (set-text! (by-id "pause") "Unpause")
  (deregister-events)
  (js/clearInterval handle)
  (reset! paused true))

(defn toggle-pause [handle]
  "Toggle the game being paused"
  (if @paused
    (unpause)
    (pause handle)))

(defn register-events [handle]
  "Register all event listeners for during gameplay"
  (listen! (by-id "game") :click
           (fn [ev]
             (let [[x y] (relative-mouse-pos ev)]
               (when-not (line/point-on-thick-path? [x y] creep-path 50)
                 (swap! towers (partial cons (construct-tower x y)))))))

  (listen! (by-id "game") :mousemove
           (fn [ev]
             (let [p (relative-mouse-pos ev)]
               (reset! mouse-pos p))))
  (listen! (by-id "pause") :click
           (fn [ev] (toggle-pause handle))))

(defn start-game []
  (let [handle (util/crashingInterval game-loop 40)]
    (register-events handle)))

;; Only attempts to start the game on browsers which actually support the canvas
;; element. Note phantomjs does not support canvas so the game is not started when
;; running unit tests.
(when canvas
  (start-game))

