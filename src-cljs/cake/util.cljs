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
    (declare ^:dynamic *handle*)
    (binding
        [*handle* (js/setInterval
                 (fn []
                   (if @crashed
                     (js/clearInterval *handle*)
                     (do
                       (compare-and-set! crashed false true)
                       (f)
                       (reset! crashed false)))) interval)]
      *handle*)))

(defn log
  "Displays s followed by obj, then returns obj"
  [s obj]
  (.log js/console s obj)
  obj)
