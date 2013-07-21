(ns nitrogentd.point)

(defprotocol Point
  "An object which can usefully be thought of as a point"
  (get-point [this] "Returns the point"))
