(ns integration.io.easybootstrap.template.utils
  (:require
   [com.stuartsierra.component :as component]
   [io.easybootstrap.helpers.logs :as logs]
   [io.easybootstrap.helpers.migrations :as migrations]
   [pg-embedded-clj.core :as pg-emb]))

(defn start-system!
  [system-start-fn]
  (fn []
    (logs/setup :info :auto)
    (pg-emb/init-pg)
    (migrations/migrate (migrations/configuration-with-db))
    (system-start-fn)))

(defn stop-system!
  [system]
  (component/stop-system system)
  (pg-emb/halt-pg!))
