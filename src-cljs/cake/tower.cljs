(ns cake.tower)

(defprotocol Tower
  "A tower defense tower"
  (draw [this time] "Draws the tower"))

