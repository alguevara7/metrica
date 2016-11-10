(ns metrica.main
  (:gen-class)
  (:require [eftest.runner :as eft]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component  :refer [ex-without-components]]))

(defn- run
  [system]
  (try ((resolve 'metrica.system/start) system)
       (catch Throwable t
         (throw (ex-without-components t)))))

(defn run-tests
  "The entry-point for 'lein run-tests'"
  []
  (eft/run-tests (eft/find-tests "test"))
  (System/exit 0))

(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& [port]]
  (log/info "\nCreating your [DEV] server...")
  (require 'metrica.system)
  (let [config-options (update-in ((resolve 'metrica.system/dev))
                                  [:ring :port]
                                  #(if port (Integer/parseInt port) %))]
    (run ((resolve 'metrica.system/system) config-options))))

(defn -main
  "The entry-point for 'lein run'"
  [& [port]]
  (log/info "\nCreating your server...")
  (require 'metrica.system)
  (let [config-options (update-in ((resolve 'metrica.system/prod))
                                  [:ring :port]
                                  #(if port (Integer/parseInt port) %))]
    (run ((resolve 'metrica.system/system) config-options))))
