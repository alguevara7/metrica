(ns metrica.service
  (:require [compojure.api.sweet :refer [defapi swagger-routes describe
                                         context GET POST PUT DELETE]]
    [ring.util.http-response :refer :all]
    [schema.core :as s]
    [clojure.tools.logging :as log]))

;;render graphite chart here

;;integrate with chronos to schedule metrics update job
;; - it's like a db migration

(s/defschema Thing
             {:id   s/Int
              :name s/Str})

(defn handle-exception [^Exception e data request]
  (log/error e)
  (internal-server-error {:message (.getMessage e)}))

(defapi routes
        {:exceptions {:handlers {:compojure.api.exception/default handle-exception}}}
        (swagger-routes
          {:ui      "/docs"
           :spec    "/swagger.json"
           :options {:ui {:validatorUrl nil}}
           :data    {:basePath "/"
                     :info     {:version     "1.0.0"
                                :title       "Sample API"
                                :description "the description"}
                     :tags     [{:name "health", :description "Health check end-point"}
                                {:name "things", :description "Things API"}]}})

        (context "/health" []
                 :tags ["health"]

                 (GET "/" []
                      :return String
                      (ok "OK")))

        (context "/things" []
                 :tags ["things"]

                 (GET "/" []
                      :return [Thing]
                      :query-params [count :- Long]
                      :summary "retrieve things"
                      (ok (reduce #(conj % {:id %2 :name (str "the-" %2)}) [] (range count))))

                 (GET "/:id" []
                      :return Thing
                      :path-params [id :- (describe Long "the thing id")]
                      :summary "retrieves the thing by id"
                      (if (< id 5)
                        (ok {:id id :name (str "the-" id)})
                        (not-found)))

                 (POST "/echo" []
                       :return Thing
                       :body [thing Thing]
                       :summary "echoes a Thing"
                       (ok thing))
                 ))
