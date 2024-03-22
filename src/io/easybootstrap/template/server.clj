(ns io.easybootstrap.template.server
  (:require
   [com.stuartsierra.component :as component]
   [io.easybootstrap.components.clj-http :as http]
   [io.easybootstrap.components.config :as config]
   [io.easybootstrap.components.database :as database]
   [io.easybootstrap.components.router :as router]
   [io.easybootstrap.components.server :as webserver]
   [io.easybootstrap.helpers.logs :as logs]
   [io.easybootstrap.helpers.migrations :as migrations]
   [io.easybootstrap.template.mediator.http-in :as http-in])
  (:gen-class))

(def system-atom (atom nil))

(defn- build-system-map []
  (component/system-map
   :config (config/new-config)
   :http (http/new-http)
   :router (router/new-router http-in/routes)
   :database (component/using (database/new-database) [:config])
   :webserver (component/using (webserver/new-webserver) [:config :http :router :database])))

(defn start-system! [system-map]
  (logs/setup :info :auto)
  (migrations/migrate (migrations/configuration-with-db))
  (->> system-map
       component/start
       (reset! system-atom)))

#_{:clj-kondo/ignore [:unused-public-var]}
(defn stop-system! []
  (swap!
   system-atom
   (fn [s] (when s (component/stop s)))))

(defn -main
  "The entry-point for 'gen-class'"
  [& _args]
  (start-system! (build-system-map)))

(comment
  (start-system! (build-system-map))
  (stop-system!))
