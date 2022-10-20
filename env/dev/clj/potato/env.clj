(ns potato.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [potato.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[potato started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[potato has shut down successfully]=-"))
   :middleware wrap-dev})
