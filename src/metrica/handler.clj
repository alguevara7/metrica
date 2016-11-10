(ns metrica.handler
  (:require [compojure.core :refer [routes]]
    [metrica.service :as service]))

(def app
  (-> (routes service/routes)))
