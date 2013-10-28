(ns nitrogentd.game.gameloop
  (:use [nitrogentd.game.spawnling :only [spawn-spawnling]]
        [nitrogentd.game.spideree :only [spawn-spideree]]
        [nitrogentd.game.roacher :only [spawn-roacher]]
        [nitrogentd.game.drawing :only [canvas]]
        [nitrogentd.game.pause-screen :only [toggle-pause]]
        [domina :only [by-id set-text!]]
        [domina.events :only [listen! unlisten!]])
  (:require [clojure.browser.event :as event]

            [nitrogentd.game.spawnlingpool :as spawnlingpool]
            [nitrogentd.game.spidereenest :as spidereenest]
            [nitrogentd.game.roachernest :as roachernest]
            [nitrogentd.game.splitterpool :as splitterpool]
            
            [nitrogentd.game.level :as level]
            [nitrogentd.game.levels :as levels]
            [nitrogentd.game.wave :as wave]
            [nitrogentd.game.waves :as waves]

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
            [nitrogentd.game.concussivetower :as concussivetower]
            [goog.dom.forms :as forms]
            [domina :as d]))

(def creep-path (atom))
(def pools (atom))
(def towers (atom []))
(def creeps (atom []))
(def animations (atom []))
(level/load-level levels/level-1 creep-path pools)

(.log js/console "Pools" (clj->js @pools))

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

(defn- selected-radio [& choices]
  "Returns selected radio button id if any selected"
  (some (comp d/value by-id) choices))

(defn selected-tower [] (selected-radio "Laser Tower" "Charge Tower" "Concussive Tower"))

(defn construct-tower [x y]
  (case (selected-tower)
    "Laser Tower" (lasertower/construct x y)
    "Charge Tower" (chargetower/construct x y)
    "Concussive Tower" (concussivetower/construct x y)
    nil))

(defn show-preview [x y]
  (case (selected-tower)
    "Laser Tower" (lasertower/preview x y)
    "Charge Tower" (chargetower/preview x y)
    "Concussive Tower" (concussivetower/preview x y)
    nil))

(defn check-end-wave []
  (if (and (empty? @pools) (empty? @creeps))
    (wave/load-wave waves/wave-1-2 pools)))

(defn game-loop []
  (gamestate/tick)
  (check-end-wave)
  (drawing/clear-canvas)
  (drawing/draw-creep-path @creep-path)
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

(defn deregister-events []
  "Deregister all event listeners which should only take place during gameplay.
   Does not deregister the pause button otherwise unpausing would never happen.
   Deregistering pause is done separately right before run-game registers it
   again with the new handle."
  (unlisten! (by-id "game")))

(defn create-pause-action [handle]
  (fn []
    (set-text! (by-id "pause") "Unpause")
    (deregister-events)
    (js/clearInterval handle)))

(declare run-game)

(defn unpause-action []
  (set-text! (by-id "pause") "Pause")
  (unlisten! (by-id "pause"))
  (run-game))

(defn register-events [handle]
  "Register all event listeners for during gameplay"
  (listen! (by-id "game") :click
           (fn [ev]
             (let [[x y] (relative-mouse-pos ev)]
               (when-not (line/point-on-thick-path? [x y] @creep-path 50)
                 (swap! towers (partial cons (construct-tower x y)))))))

  (listen! (by-id "game") :mousemove
           (fn [ev]
             (let [p (relative-mouse-pos ev)]
               (reset! mouse-pos p))))
  (listen! (by-id "pause") :click
           (fn [ev] (toggle-pause (create-pause-action handle) unpause-action))))

(defn run-game []
  (let [handle (util/crashingInterval game-loop 40)]
    (register-events handle)))
