(ns potato.routes.home
  (:require
   [potato.layout :as layout]
   [clojure.java.io :as io]
   [potato.middleware :as middleware]
   [ring.util.response]
   [clj-http.client :as client]
   [cheshire.core :as json]
   [struct.core :as st]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(def search-schema
  [[:name
    st/required
    st/string]

   [:search
    st/required
    st/string
    {:search "search must contain at least 3 characters"
     :validate #(> (count %) 2)}]])

(defn validate-search [params]
  (first (st/validate params search-schema)))

(defn search-games! [{:keys [params]}]
  (if-let [errors (validate-search params)]
    (-> (response/found "/search")
        (assoc :flash (assoc params :errors errors)))
    (do
      (response/found (str "/search?query=" params) ))))

(def h {"User-Agent" "Mozilla/5.0 (Windows NT 6.1;) Gecko/20100101 Firefox/13.0.1"})
(def url (System/getenv "GAMES_URL"))

(defn get-game-names [games]
  (map (fn [game] {:name (get-in game ["name"]) :image (get-in game ["image" "icon_url"])}) games))

(defn game-filter [params]
  (println params)
  (str "name:" params)
  )

(defn search-page [request]
  (let [search-param (get-in request [:query-params "query"])]
    (layout/render
      request
      "search.html"
      (merge {:games (get-game-names
                       (get (json/parse-string
                              (get-in (client/get url
                                                  {:accept :json
                                                   :headers h
                                                   :query-params {"format" "json"
                                                                  "field_list" "name,image"
                                                                  "limit" 100
                                                                  "filter" (str "name:" search-param)
                                                                  }})
                                      [:body])
                              ) "results")
                       )}))))

(defn checkout-page [request]
  (layout/render request "checkout.html"))

(defn home-routes []
  [ "" 
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats
                 ]}
   ["/" {:get home-page}]
   ["/search" {:get search-page}]
   ["/checkout" {:get checkout-page}]])

