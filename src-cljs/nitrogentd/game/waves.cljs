(ns nitrogentd.game.waves
  (:use [nitrogentd.game.wave :only [map->Wave]])
  (:require [nitrogentd.game.spawnlingpool :as spawnlingpool]
            [nitrogentd.game.spidereenest :as spidereenest]
            [nitrogentd.game.roachernest :as roachernest]
            [nitrogentd.game.splitterpool :as splitterpool]
            [nitrogentd.game.paths :as paths]))

(def wave-1-1 (map->Wave
               {:name "The beginning"
                :pools [(spawnlingpool/construct 8 paths/path-1a)]}))

(def wave-1-2 (map->Wave
               {:name "The next one"
                :pools [(spidereenest/construct 4 paths/path-1a)]}))

(def wave-1-3 (map->Wave
               {:name "And another"
                :pools [(roachernest/construct 2 paths/path-1a)
                        (spawnlingpool/construct 6 paths/path-1b)]}))

(def wave-1-4 (map->Wave
               {:name "Last wave of first level"
                :pools [(spawnlingpool/construct 5 paths/path-1a)
                        (spawnlingpool/construct 5 paths/path-1b)
                        (spidereenest/construct 6 paths/path-1a)]}))

(def wave-2-1 (map->Wave
               {:name "And so begins the second level"
                :pools [(spawnlingpool/construct 3 paths/path-2a)]}))

(def wave-2-2 (map->Wave
               {:name "Omg, what is this?"
                :pools [(splitterpool/construct 4 paths/path-2a)]}))
