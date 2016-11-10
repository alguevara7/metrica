(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require
    [clojure.java.io :as io]
    [clojure.java.javadoc :refer [javadoc]]
    [clojure.pprint :refer [pprint]]
    [clojure.reflect :refer [reflect]]
    [clojure.repl :refer [apropos dir doc find-doc pst source]]
    [clojure.set :as set]
    [clojure.string :as str]
    [clojure.test :as test]
    [clojure.tools.namespace.repl :refer [refresh refresh-all]]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [metrica.system :as system]
    [environ.core :refer [env]]
    [clj-jgit.porcelain :refer [load-repo git-log git-branch-list]]
    [clj-jgit.querying :refer [changes-for changed-files changed-files-with-patch]]
    [clojure.reflect :as r]))

(set! *warn-on-reflection* true)

(def system nil)

(defn init
  "Constructs the current development system."
  []
  (alter-var-root #'system
                  (constantly (system/system (system/dev)))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system system/start))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
                  (fn [s] (when s (system/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))

(go)

;; (reset)

(metrica.graphite/publish (:graphite user/system) "project.fssdf" "metric"
                          100
                          (-> (java.util.Date.) (.getTime) (/ 1000) (int)))

;; (start)

;; (stop)


;; (def r (load-repo "/Users/alguevara/dev/kijijica/kijiji.ca"))

;; (def data (metrica.etl/git->graphite (:etl user/system)
;;                r
;;                "9e8258d02a7668ca35da78213dbbf59dd12114f6"
;;                "1eca35efd661b7aeb78c5b50581f2a91f75c9a6b"))

;; (-> data first)

;; (bean (-> data first :hash (.getCommitterIdent)))

;; (->> (first (git-branch-list r)) (.getName))

;; ;; ;; (publish-to-graphite X)
;; ;; ;; echo "local.random.diceroll 4 `date +%s`" | nc -c ${SERVER} ${PORT}

;; ;; ;; exclude merges

;; (->> r git-log first r/reflect :members (map :name))

;; (->> r git-log first (.getAuthorIdent) bean)

;; (->> r git-log first (.getFullMessage))

;; (->> r git-log first (.getId))

;; (->> r git-log second (.getAuthorIdent) (.getName))

;; (->> r git-log first (.getParents) count)

;; (->> r git-log count)

;; (->> r git-log first )

;; (->> r git-log second (changed-files r))

;; (->> r git-log second (changed-files-with-patch r) (println))

