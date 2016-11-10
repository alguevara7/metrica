(ns metrica.server
  (:require [metrica.handler :refer [app]]
    [ring.server.standalone :refer [serve]]
    [compojure.api.middleware :as mw]
    [com.stuartsierra.component :as component]
    [clojure.tools.logging :as log]))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  #'app)

(defn start [{:keys [auto-reload] :as server}]
  (-> (if auto-reload (get-handler) app)
      (mw/wrap-components server)
      (serve server)))

(defn stop [server]
  (when server
    (.stop server))
  nil)

(defrecord Server [host port auto-reload join stacktraces ring-server]
  component/Lifecycle

  (start [component]
    (log/info "Starting Server")
    (let [server (start component)]
      (assoc component :ring-server server)))

  (stop [component]
    (log/info "Stopping Server")
    (stop ring-server)
    (assoc component :ring-server nil)))

(defn new-server [options]
  (map->Server options))
