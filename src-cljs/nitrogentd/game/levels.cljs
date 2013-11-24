(ns nitrogentd.game.levels
  (:use [nitrogentd.game.level :only [map->Level]])
  (:require [nitrogentd.game.waves :as waves]
            [nitrogentd.game.paths :as paths]))

(def level-1 (map->Level
              {:name "Intro"
               :paths [paths/path-1a paths/path-1b]
               :waves [waves/wave-1-1 waves/wave-1-2 waves/wave-1-3]}))