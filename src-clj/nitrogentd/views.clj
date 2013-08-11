(ns nitrogentd.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js]]
    [page :refer [include-css]]]))

(defn page-menu []
  [:div.navbar.navbar-inverse.navbar-fixed-top
   [:div.navbar-inner
    [:div.container
     [:a.brand {:href "#"} "NitrogenTD"]
     [:ul.nav
      [:li [:a {:href "#"} "Game"]]]]]])

(defn- game-radio-buttons [header & options]
  (list
   [:h3 header]
   (when options
     (letfn [(first-option [option]
               [:label.radio
                [:input {:type "radio" :name header :value option :id option :checked true}]
                option])
             (rest-options [option]
               [:label.radio
                [:input {:type "radio" :name header :value option :id option}]
                option])]
       (let [[f & r] options]
         [:form {:id (str "menu-" header) :action ""}]
         (cons
          (first-option f)
          (map rest-options r)))))))
        
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
       [:button#pause "Pause"]
       (game-radio-buttons "Build" "Laser Tower" "Charge Tower")]]]
    (include-js "/js/bootstrap.min.js")
    (include-js "/js/main.js")
    ]))

