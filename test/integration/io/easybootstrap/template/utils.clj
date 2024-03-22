(ns integration.io.easybootstrap.template.utils
  (:require [io.easybootstrap.helpers.logs :as logs]
            [io.easybootstrap.helpers.migrations :as migrations]
            [com.stuartsierra.component :as component]
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
