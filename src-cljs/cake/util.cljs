(ns cake.util)

;; Essentially misc

(defn by-id [id]
  (.getElementById js/document (name id)))

(defn mod-inc
  ([num max] (mod-inc num max 1))
  ([num max step]
     (let [num' (+ num step)]
       (if (> num' max) 0 num'))))

(defn to-radians
  [degrees]
  (* degrees (/ Math/PI 180)))

(defn crashingInterval
  "Like setInterval, except terminates if the function ever crashes"
  [f interval]
  (let [crashed (atom false)]
    (def ^:dynamic handle) ;; chicken, meet egg
    (binding
        [handle (js/setInterval
                 (fn []
                   (if @crashed
                     (js/clearInterval handle)
                     (do
                       (compare-and-set! crashed false true)
                       (f)
                       (reset! crashed false)))) interval)]
      handle)))

(defn loud
  "Takes a function. Output the return value of that function to the console, then return it. For debugging. Also outputs useful identifier in same line"
  [s fn]
  (let [ret (fn)]
    (.log js/console "HEllo")
    (.log js/console s ret)
    ret))
