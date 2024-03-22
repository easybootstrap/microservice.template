(ns user
  (:require [malli.dev :as dev]
            [malli.dev.pretty :as pretty]
            [io.easybootstrap.tempalte.server]))

(defn start
  []
  (dev/start! {:report (pretty/thrower)}))

(defn stop
  []
  (dev/stop!))

(comment
  (start)
  (stop))
