(ns cake.gamestate)

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
(def ^:dynamic time)

(defn tick
  "Updates the current time"
  []
  (set! time (.getTime (js/Date.))))
