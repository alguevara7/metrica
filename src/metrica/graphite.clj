(ns metrica.graphite
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log])
  (:import [java.util Date]
           [java.net Socket]
           [java.io PrintWriter]))


;;echo "local.random.diceroll 4 `date +%s`" | nc -c ${SERVER} ${PORT}

(defn publish

  ([graphite coll]
   (doseq [[metric value timestamp-seconds] coll]
     (publish graphite metric value timestamp-seconds)))

  ([{:keys [host port] :as graphite} metric value timestamp-seconds]
   (with-open [socket (Socket. host port)
               os (.getOutputStream socket)]
     (binding [*out* (PrintWriter. os)]
       (println (str metric " " value " " timestamp-seconds))))))

(defrecord Graphite [host port]
  component/Lifecycle

  (start [component]
         (log/info "Starting Graphite")
         component)

  (stop [{:keys [conn-manager] :as component}]
        (log/info "Stopping Graphite")))

(defn new-graphite [options]
  (map->Graphite options))
