(ns nitrogentd.game.splitterpool
  (:use [nitrogentd.game.singlepool :only [SinglePool]]
        [nitrogentd.game.splitter :only [spawn]]
        [nitrogentd.game.gamestate :only [time]]))

(def time-between-spawns 3000)
(def spawn-at-a-time 1)

(defn construct
  [n path]
  (let [last-spawn time]
    (SinglePool. n path last-spawn time-between-spawns spawn-at-a-time spawn)))
