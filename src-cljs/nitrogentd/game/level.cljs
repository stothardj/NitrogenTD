(ns nitrogentd.game.level
  (:use [nitrogentd.game.wave :only [load-wave]]))

(defrecord Level [name paths waves next])
