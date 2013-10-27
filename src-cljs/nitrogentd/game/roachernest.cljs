(ns nitrogentd.game.roachernest
  (:use [nitrogentd.game.pool :only [Pool]]
        [nitrogentd.game.roacher :only [spawn-roacher]]
        [nitrogentd.game.gamestate :only [time time-passed?]]))

(deftype RoacherNest
    [n path last-spawn]
  Pool
  (spawn-creep [this]
    (if-not (time-passed? last-spawn 10000)
      {:pool this :creep []}
      (let [creep-left (dec n)
            new-creep (let [[[cx cy]] path]
                        [(spawn-roacher cx cy path)])]
        (if (zero? creep-left)
          {:creep new-creep}
          {:creep new-creep
           :pool (RoacherNest. creep-left path time)})))))

(defn construct
  [n path]
  (let [last-spawn time]
    (RoacherNest. n path last-spawn)))
