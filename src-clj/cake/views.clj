(ns cake.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js]]
    [page :refer [include-css]]]))

(defn- menu [header & options]
  (list
   [:h2 header]
   (map (fn [option] [:a {:href "#"} [:li option]]) options)))

(defn index-page []
  (html5
   [:head
    (include-css "/css/style.css")
    [:title "OpenTD"]]
   [:body
    [:div#full-page.cf
     [:div#center-container
      [:h1 "OpenTD"]
      [:canvas#game {:width "800" :height "600"}]]
     [:div#right-container
      (menu "Build" "Laser Tower" "Charge Tower")]]
    (include-js "/js/main.js")
    ]))

