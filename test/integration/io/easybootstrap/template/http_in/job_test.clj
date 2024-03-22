(ns integration.io.easybootstrap.template.http-in.job-test
  (:require
   [clojure.test :refer [use-fixtures]]
   [com.stuartsierra.component :as component]
   [integration.io.easybootstrap.template.utils :as utils]
   [io.easybootstrap.aux.server :as aux.server]
   [io.easybootstrap.components.clj-http :as components.clj-http]
   [io.easybootstrap.components.config :as config]
   [io.easybootstrap.components.database :as components.database]
   [io.easybootstrap.components.router :as router]
   [io.easybootstrap.components.server :as webserver]
   [io.easybootstrap.helpers.malli :as helpers.malli]
   [io.easybootstrap.template.mediator.db.postgres.job :as db.postgres.job]
   [io.easybootstrap.template.mediator.http-in :as http-in]
   [matcher-combinators.matchers :as matchers]
   [state-flow.api :refer [defflow]]
   [state-flow.assertions.matcher-combinators :refer [match?]]
   [state-flow.core :as state-flow :refer [flow]]
   [state-flow.state :as state]))

(use-fixtures :once helpers.malli/with-intrumentation)

(defn- create-and-start-components! []
  (component/start-system
   (component/system-map
    :config (config/new-config)
    :http (components.clj-http/new-http-mock {})
    :router (router/new-router http-in/routes)
    :database (component/using (components.database/new-database)
                               [:config])
    :webserver (component/using (webserver/new-webserver)
                                [:config :http :router :database]))))

(defflow  flow-integration-job-get-all
  {:init (utils/start-system! create-and-start-components!)
   :cleanup utils/stop-system!
   :fail-fast? true}
  (flow "should interact with system"

    (flow "prepare system with mocks"
      [database (state-flow.api/get-state :database)]
      (state/invoke
       #(db.postgres.job/insert-job-transaction
         {:job/id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7"
          :job/title "Software Engineer II"
          :job/description "We are looking for a Software Engineer II to join our team"}
         database))

      (flow "should insert deposit into wallet"
        (match? (matchers/embeds {:status 200
                                  :body  {:jobs [{:id "cd989358-af38-4a2f-a1a1-88096aa425a7"
                                                  :title "Software Engineer II"
                                                  :description "We are looking for a Software Engineer II to join our team"}]}})
                (aux.server/request! {:method :get
                                      :uri    "/job/"})))

      (flow "should insert a new job"
        (match? (matchers/embeds {:status 201
                                  :body  {:id string? :title "SWE III" :description "We are looking for a Software Engineer III to join our team to work with clojure"}})
                (aux.server/request! {:method :post
                                      :uri    "/job/"
                                      :body   {:title "SWE III" :description "We are looking for a Software Engineer III to join our team to work with clojure"}})))

      (flow "should list all jobs"
        (match? (matchers/embeds {:status 200
                                  :body  {:jobs [{:id "cd989358-af38-4a2f-a1a1-88096aa425a7"
                                                  :title "Software Engineer II"
                                                  :description "We are looking for a Software Engineer II to join our team"}
                                                 {:id string?
                                                  :title "SWE III"
                                                  :description "We are looking for a Software Engineer III to join our team to work with clojure"}]}})
                (aux.server/request! {:method :get
                                      :uri    "/job/"})))

      (flow "should get job by id"
        (match? (matchers/embeds {:status 200
                                  :body {:id "cd989358-af38-4a2f-a1a1-88096aa425a7"
                                         :title "Software Engineer II"
                                         :description "We are looking for a Software Engineer II to join our team"}})
                (aux.server/request! {:method :get
                                      :uri  "/job/cd989358-af38-4a2f-a1a1-88096aa425a7"}))))))
