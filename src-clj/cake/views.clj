(ns cake.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js]]
    [page :refer [include-css]]]))

(defn- menu [header & options]
  (list
   [:h2 header]
   (when options
     (letfn [(first-option [option]
               (list [:input {:type "radio" :name header :value option :id option :checked true}]
                     [:label {:for option} option]
                     [:br]))
             (rest-options [option]
               (list [:input {:type "radio" :name header :value option :id option}]
                     [:label {:for option} option]
                     [:br]))]
       (let [[f & r] options]
         [:form {:id (str "menu-" header) :action ""}]
         (cons
          (first-option f)
          (map rest-options r)))))))
        
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

