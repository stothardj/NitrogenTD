(ns nitrogentd.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js]]
    [page :refer [include-css]]]))

(defn page-menu []
  [:div.navbar.navbar-inverse.navbar-fixed-top
   [:div.container
    [:div.navbar-header
     [:a.navbar-brand {:href "#"} "NitrogenTD"]]
    [:ul.nav.navbar-nav
     [:li [:a {:href "#"} "Game"]]]]])
        
(defn index-page []
  (html5
   [:head
    (include-css "/css/style.css")
    (include-css "/css/bootstrap.min.css")
    [:title "NitrogenTD"]]
   [:body
    (page-menu)
    [:div.container
     [:div#full-page.cf
      [:div#center-container
       [:canvas#game {:width "800" :height "600"}]]
      [:div#right-container
       [:div#player-stats.right-item.thumbnail.stat-surround
        [:table.stats-table
         [:tr [:th "Gold"] [:td#gold-stat.stats-data "600"]]]]
       [:button#pause.btn.btn-default.right-item {:data-toggle "button"} "Pause"]
       [:div.btn-group-vertical.right-item {:data-toggle "buttons"}
        [:label#laser-tower.btn.btn-default.active [:input {:name "towers" :type "radio"} "Laser Tower"]]
        [:label#charge-tower.btn.btn-default [:input {:name "towers" :type "radio"} "Charge Tower"]]
        [:label#concussive-tower.btn.btn-default [:input {:name "towers" :type "radio"} "Concussive Tower"]]]
       [:div#selected-info.right-item.thumbnail]]]]
    ;; The game does not use jquery but it's required for the bootstrap plugins
    [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"}]
    (include-js "/js/bootstrap.min.js")
    [:script "$('.btn-group').button()"]
    (include-js "/js/main-debug.js")
    ]))

