(ns metrica.graphite
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log])
  (:import [java.util Date]
           [java.net Socket]
           [java.io PrintWriter]))

;;metrica/{system-name}/{committer}/lines/(added|removed)/{count}


;;echo "local.random.diceroll 4 `date +%s`" | nc -c ${SERVER} ${PORT}

(defn publish [{:keys [host port] :as graphite}
               project metric value timestamp-seconds]
  (with-open [socket (Socket. host port)
              os (.getOutputStream socket)]
    (binding [*out* (PrintWriter. os)]
      (println (str project "." metric " " value " " timestamp-seconds)))))

;; (defn now []
;;   (int (/ (System/currentTimeMillis) 1000)))

;; (defn write-metric [name value timestamp]
;;   (with-open [socket (Socket. "localhost" 2003)
;;               os (.getOutputStream socket)]
;;     (binding [*out* (PrintWriter. os)]
;;       (println name value timestamp))))

(defrecord Graphite [host port]
  component/Lifecycle

  (start [component]
         (log/info "Starting Graphite")
         component)

  (stop [{:keys [conn-manager] :as component}]
        (log/info "Stopping Graphite")))

(defn new-graphite [options]
  (map->Graphite options))
