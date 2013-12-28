(ns nitrogentd.game.simple)

;; Returns the simple version of a type. That is, "empty" or "default"
(defmulti simple identity)

(defmethod simple :default [t] :debugging)
