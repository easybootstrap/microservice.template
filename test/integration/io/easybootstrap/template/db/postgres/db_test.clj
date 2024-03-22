(ns integration.io.easybootstrap.template.db.postgres.db-test
  (:require
   [clojure.test :refer [use-fixtures]]
   [com.stuartsierra.component :as component]
   [integration.io.easybootstrap.template.utils :as utils]
   [io.easybootstrap.components.config :as config]
   [io.easybootstrap.components.database :as components.database]
   [io.easybootstrap.helpers.malli :as helpers.malli]
   [io.easybootstrap.template.mediator.db.postgres.job :as db.postgres.job]
   [state-flow.api :refer [defflow match?]]
   [state-flow.core :as state-flow :refer [flow]]
   [state-flow.state :as state]))

(use-fixtures :once helpers.malli/with-intrumentation)

(defn- create-and-start-components! []
  (component/start-system
   (component/system-map
    :config (config/new-config)
    :database (component/using (components.database/new-database)
                               [:config]))))

(defflow flow-integration-db-test
  {:init (utils/start-system! create-and-start-components!)
   :cleanup utils/stop-system!
   :fail-fast? true}
  (flow "creates a table, insert data and checks return in the database"
        [database (state-flow.api/get-state :database)]

        (state/invoke
         #(db.postgres.job/insert-job-transaction
           {:job/id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7"
            :job/title "Software Engineer II"
            :job/description "We are looking for a Software Engineer II to join our team"}
           database))

        (flow "check transaction was inserted in db"
              (match? [{:job/id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7"
                        :job/title "Software Engineer II"
                        :job/created_at inst?
                        :job/description "We are looking for a Software Engineer II to join our team"}]
                      (db.postgres.job/get-all-jobs database)))

        (flow "check get by id, should return a job"
              (match? {:job/id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7"
                       :job/title "Software Engineer II"
                       :job/created_at inst?
                       :job/description "We are looking for a Software Engineer II to join our team"}
                      (db.postgres.job/get-by-id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7" database)))))
