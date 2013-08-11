(ns nitrogentd.game.hello
  (:use [nitrogentd.game.gamestate :only [tick]]
        [nitrogentd.game.gameloop :only [run-game]]
        [nitrogentd.game.drawing :only [canvas]]))

;; This file starts the game. It doesn't define any functions or variables so that
;; there will be no temptation to include it, possibly starting the game again.

;; Only attempts to start the game on browsers which actually support the canvas
;; element. Note phantomjs does not support canvas so the game is not started when
;; running unit tests.
(when canvas
  (run-game))

