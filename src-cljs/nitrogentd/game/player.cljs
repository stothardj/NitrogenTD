(ns nitrogentd.game.player)

(defrecord Player [gold])

(defn construct []
  (map->Player {:gold 600}))
