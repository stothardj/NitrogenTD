(ns cake.tower)

(defprotocol Tower
  "A tower defense tower"
  (draw [this] "Draws the tower"))

