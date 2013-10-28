(ns nitrogentd.game.levels
  (:use [nitrogentd.game.level :only [map->Level]])
  (:require [nitrogentd.game.waves :as waves]
            [nitrogentd.game.paths :as paths])
  )

(def level-1 (map->Level
              {:name "Intro"
               :path paths/path-1
               :waves [waves/wave-1-1]
               :next nil}))
