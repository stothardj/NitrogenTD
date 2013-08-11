(ns nitrogentd.game.pause-screen
  (:use [nitrogentd.game.gamestate :only [pause-time unpause-time]]))

(def ^:private paused (atom false))

(defn is-paused? [] @paused)

(defn- unpause [unpause-action]
  (unpause-time)
  (reset! paused false)
  (unpause-action))

(defn- pause [pause-action]
  (pause-time)
  (pause-action)
  (reset! paused true))

(defn toggle-pause [pause-action unpause-action]
  "Toggle the game being paused. Pause and unpause are private so that it is impossible
   to call pause when already paused. Pause and unpause only keep track of paused state
   and cause the apropriate function to be called on time. The caller of toggle pause is
   repsonsible for providing any action they would like preformed while pausing or unpauseing."
  (if @paused
    (unpause unpause-action)
    (pause pause-action)))

