(ns nitrogentd.game.roachernest
  (:use [nitrogentd.game.singlepool :only [SinglePool]]
        [nitrogentd.game.roacher :only [spawn]]
        [nitrogentd.game.gamestate :only [time]]))

(def time-between-spawns 10000)
(def spawn-at-a-time 1)

(defn construct
  [n path]
  (let [last-spawn time]
    (SinglePool. n path last-spawn time-between-spawns spawn-at-a-time spawn)))
