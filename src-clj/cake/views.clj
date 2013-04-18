(ns cake.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js]]
    [page :refer [include-css]]]))

(defn index-page []
  (html5
   [:head
    (include-css "/css/style.css")
    [:title "Open"]]
   [:body
    [:h1 "Open"]
    [:canvas#game {:width "800" :height "600"}]
    (include-js "/js/main.js")
    ]))
