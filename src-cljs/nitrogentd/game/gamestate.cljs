(ns nitrogentd.game.gamestate)

;; General useful game state. Global state is evil. Here it is.

;; The current "time" of this game loop.
;; This is updated once per game loop at the beginning of the game loop.
;; This variable should be referenced for things like animations and things
;; which are supposed to last a certain amount of time. Use this instead
;; of calling .getTime directly so that small variations in how it takes to
;; reach a certain piece of code within a game loop do not have a chance to
;; affect anything. This is a dynamic variable instead of making time a
;; parameter to various functions because of the sheer number of functions
;; which required the current time. By storing the start or creation time of
;; an object and basing actions on time differences mutating the underlying
;; object can often be avoided.
;;
;; Note that this is meant to be game time, not wall time, and so will lie.
;; For example when the game has been paused it will make it seem that no
;; time actually passed. Time is guarenteed never to go backwards.
(def ^:dynamic time)

(defn- system-time []
  (.getTime (js/Date.)))

(def ^:private total-paused (atom 0))
(def ^:private last-paused (atom nil))

(defn- unpause-time []
  "Allow game time to continue normally. Idempotent."
  (when @last-paused
    (let [now (system-time)
          since-paused (- now @last-paused)]
      (swap! total-paused (partial + since-paused))
      (reset! last-paused nil))))

(defn tick
  "Updates the current time."
  []
  (let [now (system-time)]
    (set! time (- now @total-paused))))

(defn time-passed?
  "Returns true if duration time has passed since start-time"
  [start-time duration]
  (< (+ start-time duration) time))

(defn pause-time []
  "Pause game time. Make it so time appears not to be passing. Idempotent."
  (let [now (system-time)]
    (compare-and-set! last-paused nil now)))
