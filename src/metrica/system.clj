(ns metrica.system
  (:require [metrica.server :as server]
            [metrica.graphite :as graphite]
            [metrica.etl :as etl]
            [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [clojure.string :as str]))

(defn prod
  "Returns a new instance of the whole application.
  see http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded"
  []
  {:server {:port          8080
            :auto-reload   false
            :join          true
            :stacktraces   false
            :open-browser? false
            :max-idle-time (* 1000 60)}
   :graphite {:host "192.168.99.100"
              :port 2003
              :conn-manager-config {:timeout 1
                                    :threads 4
                                    :default-per-route 4}
              :request-config {:socket-timeout 1000
                               :conn-timeout 100}}})

(defn dev []
  (merge-with merge (prod) {:server {:host "127.0.0.1"
                                     :auto-reload true
                                     :join false
                                     :stacktraces true}}))

(defn system [{:keys [server graphite etl] :as options}]
  (-> (component/system-map
        :server (server/new-server server)
        :graphite (graphite/new-graphite graphite)
        :etl (etl/new-etl etl))
      (component/system-using
        {:server []
         :graphite []
         :etl [:graphite]})))

(defn start [system]
  (component/start system))

(defn stop [{:keys [ring] :as system}]
  (component/stop system))
