(ns cake.line
  (:require [cake.util :as util])
  )

;; All mathematics relating to lines that I'll need

(defn infinity?
  [n]
  (or (= n js/Infinity)
      (= n (- js/Infinity))))

(defn slope
  "Returns the slope of the given line segment"
  [x1 y1 x2 y2]
  (/ (- y2 y1) (- x2 x1)))

(defn perp-slope
  "Returns the slope of a line perpendicular to one with given slope"
  [slope]
  (- (/ slope))) ;; Special cases not needed for infinity and zero, just works

(defn intersection
  "Returns the point of intersection between two lines. Each line in form [m x y]"
  [line0 line1]
  (let [[m0 x0 y0] line0
        [m1 x1 y1] line1]
    (cond
     (= m0 m1) nil
     (infinity? m0 ) (recur line1 line0)
     (infinity? m1)
     (let [xi x1
           yi (+ (* m0 (- xi x0)) y0)]
       [xi yi])
     :else
     (let [xi (/ (+ (* m1 x1) (- y1) (- (* m0 x0)) y0) (- m1 m0))
           yi (+ (* m0 (- xi x0)) y0)]
       [xi yi]))))

(defn sq-point-to-point-dist
  "Returns the distance between two points, squared"
  [p0 p1]
  (let [[x0 y0] p0
        [x1 y1] p1
        xdiff (- x1 x0)
        ydiff (- y1 y0)]
    (+ (* xdiff xdiff) (* ydiff ydiff))))

(defn- between?
  "Returns true if a<=b<=c or a>=b>=c. That is, returns true if b is between a and c"
  [a b c]
  (or (<= a b c) (>= a b c)))

(defn on-segment?
  "Given a line segment and a point on the overall line, returns if that point is on the given segment"
  [point segment]
  (let [[x0 y0] point
        [x1 y1 x2 y2] segment]
    (and (between? x1 x0 x2) (between? y1 y0 y2))))

;; Logic:
;; The shortest distance between a point and a line is given by the length the
;; perpendular segment originating from the point and hitting the line.
(defn closest-point-on-line
  "The point on the line closest to the point"
  [point line]
  (let [[x0 y0] point
        [m1 x1 y1] line
        m0 (perp-slope m1)]
    (intersection line [m0 x0 y0])))

;; Demo of above function, not necessarily useful here
(defn sq-point-to-line-dist
  "Returns the square distance of a point to a line. Point is [x y]. Line is [m x1 y1]."
  [point line]
    (sq-point-to-point-dist point (closest-point-on-line point line)))

(defn get-line
  "Given a segment return the full line"
  [segment]
  (let [[x1 y1 x2 y2] segment]
    [(slope x1 y1 x2 y2) x1 y1]))

(defn point-on-thick-line-segment?
  "Given a line segment with a width, and a point, return whether the point is on that thick line"
  [point segment width]
  (let [[x0 y0] point
        [x1 y1 x2 y2] segment
        cpol (closest-point-on-line point (get-line segment))
        hwidth (/ width 2)]
    (.log js/console "Closest point on line" cpol)
    (and (on-segment? cpol segment) (<= (util/loud "distance" #(sq-point-to-point-dist cpol point)) (* hwidth hwidth)))))

(defn point-on-thick-path?
  "Similar to above. Path is a list of points on the path. Ex path: '((1 2) (5 7) (3 4))"
  [point path width]
  (if (< (count path) 2)
    false
    (let [[x1 y1] (first path)
          [x2 y2] (second path)]
      (.log js/console "Checking from" [x1 y1] "to" [x2 y2])
      (or (point-on-thick-line-segment? point [x1 y1 x2 y2] width)
           (point-on-thick-path? point (rest path) width)))))

