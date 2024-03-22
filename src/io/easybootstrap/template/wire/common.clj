(ns io.easybootstrap.template.wire.common
  (:require [com.stuartsierra.component :as component]
            [malli.core :as m]
            [io.easybootstrap.components.database :as components.database]
            [io.easybootstrap.components.clj-http :as components.clj-http]
            [malli.experimental.time :as met]))

(def HttpComponent
  (m/-simple-schema
   {:type :http-component
    :pred #(satisfies? components.clj-http/HttpProvider %)
    :type-properties {:error/message "should satisfy io.easybootstrap.components.clj-http/HttpProvider protocol."}}))

(def DatabaseComponent
  (m/-simple-schema
   {:type :database-component
    :pred #(satisfies? components.database/DatabaseProvider %)
    :type-properties {:error/message "should satisfy io.easybootstrap.components.database/DatabaseProvider protocol."}}))

(def GenericComponent
  (m/-simple-schema
   {:type :generic-component
    :pred #(satisfies? component/Lifecycle %)
    :type-properties {:error/message "should satisfy com.stuartsierra.component/Lifecycle protocol."}}))

(def JavaLocalDateTime
  (met/-local-date-time-schema))

(def JavaZoneId
  (met/-zone-id-schema))

(def Components
  [:map
   [:config GenericComponent]
   [:http HttpComponent]
   [:router GenericComponent]
   [:database DatabaseComponent]])
