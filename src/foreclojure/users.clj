(ns foreclojure.users
  (:use foreclojure.utils
        somnium.congomongo
        compojure.core
        [hiccup.page-helpers :only (link-to)]))

(defn get-users []
  (let [users (from-mongo
               (fetch :users
                      :only [:user :solved :contributor]))
        sortfn  (comp count :solved)]
    (reverse (sort-by sortfn users))))

(def-page users-page []
  [:div
   [:span.contributor "*"] " "
   (link-to "https://github.com/dbyrne/4clojure" "4clojure contributor")]
  [:br]
  [:table#user-table.my-table
   [:thead
    [:tr
     [:th {:style "width: 40px;"} "Rank"]
     [:th "Username"]
     [:th "Problems Solved"]]]
   (map-indexed #(vec [:tr (row-class %1)
                       [:td (inc %1)]
                       [:td
                        (when (:contributor %2)
                          [:span.contributor "* "])
                        (:user %2)]
                       [:td {:class "centered"} (count (:solved %2))]])
                (get-users))])

(defroutes users-routes
  (GET "/users" [] (users-page)))
