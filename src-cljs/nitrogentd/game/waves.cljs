(ns nitrogentd.game.waves
  (:use [nitrogentd.game.wave :only [map->Wave]])
  (:require [nitrogentd.game.spawnlingpool :as spawnlingpool]
            [nitrogentd.game.paths :as paths]))

(def wave-1-1 (map->Wave
               {:name "The beginning"
                :pools [(spawnlingpool/construct 8 paths/path-1)]}))
