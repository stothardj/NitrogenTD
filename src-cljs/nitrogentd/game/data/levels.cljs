(ns nitrogentd.game.data.levels
  (:use [nitrogentd.game.level :only [map->Level]])
  (:require [nitrogentd.game.data.waves :as waves]
            [nitrogentd.game.data.paths :as paths]))

(def level-1 (map->Level
              {:name "Intro"
               :paths [paths/path-1a paths/path-1b]
               :waves [waves/wave-1-1 waves/wave-1-2 waves/wave-1-3 waves/wave-1-4]}))

(def level-2 (map->Level
              {:name "Crawling"
               :paths [paths/path-2a]
               :waves [waves/wave-2-1 waves/wave-2-2]}))
