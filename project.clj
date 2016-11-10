(defproject metrica (or (System/getenv "VERSION") "0.0.1")
            :description "FIXME: enter the description of your service here"
            :url "http://example.com/FIXME"
            :dependencies [[org.clojure/clojure "1.8.0"]
                           [org.clojure/tools.logging "0.3.1"]
                           [ch.qos.logback/logback-classic "1.1.3"]
                           [net.logstash.logback/logstash-logback-encoder "4.5.1"]
                           [ring/ring-core "1.4.0"]
                           [ring-server "0.4.0"]
                           [clj-time "0.11.0"]
                           [environ "1.0.0"]
                           [eftest "0.1.0"]
                           [metosin/compojure-api "1.0.0"]
                           [com.stuartsierra/component "0.3.1"]
                           [clj-http "2.0.0"]
                           [clj-jgit "0.8.9"]]
            :main metrica.main
            :jvm-opts ["-Duser.timezone=UTC"]
            :profiles {:dev     {:aliases      {"run-dev"   ["trampoline" "run" "-m" "metrica.main/run-dev"]
                                                "run-tests" ["trampoline" "run" "-m" "metrica.main/run-tests"]}
                                 :dependencies [[ring/ring-mock "0.2.0"]
                                                [ring/ring-devel "1.4.0"]
                                                [org.clojure/tools.namespace "0.2.11"]]
                                 :repl-options {:init-ns user}
                                 :source-paths ["dev"]}
                       :uberjar {:aot [metrica.main]}}
            :offline true
            :mirrors {"central" {:name "central"
                                 :url  "http://nexus.kjdev.ca/content/groups/public"}
                      "clojars" {:name         "Internal nexus"
                                 :url          "http://nexus.kjdev.ca/content/repositories/clojars"
                                 :repo-manager true}})
