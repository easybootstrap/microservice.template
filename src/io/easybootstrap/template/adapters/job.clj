(ns io.easybootstrap.template.adapters.job
  (:require
   [io.easybootstrap.template.wire.common :as wire.common]
   [io.easybootstrap.template.wire.db.job :as wire.db.job]
   [io.easybootstrap.template.wire.in.job :as wire.in.job]
   [io.easybootstrap.template.wire.out.job :as wire.out.job])
  (:import
   [java.time ZoneId]
   [java.time.format DateTimeFormatter]))

(defn ^:private date->localdatetime
  {:malli/schema [:=> [:cat inst? wire.common/JavaZoneId] wire.common/JavaLocalDateTime]}
  [value zone-id]
  (-> value
      .toInstant
      (.atZone zone-id)
      .toLocalDateTime))

(defn inst->utc-formated-string
  {:malli/schema [:=> [:cat inst? :string] :string]}
  [inst str-format]
  (-> inst
      (date->localdatetime (ZoneId/of "UTC"))
      (.format (DateTimeFormatter/ofPattern str-format))))

(defn internal->wire
  {:malli/schema [:=> [:cat wire.db.job/Job]
                  wire.out.job/Job]}
  [{:job/keys [id title description created_at]}]
  {:id id :title title :description description :created-at created_at})

(defn wire->internal
  {:malli/schema [:=> [:cat wire.in.job/Job]
                  wire.db.job/Job]}
  [{:keys [title description]}]
  {:job/id (random-uuid) :job/title title :job/description description})

(comment

  (require '[malli.core :as m])

  (m/=> internal->wire [:function [:=> wire.db.job/Job] [:vector wire.out.job/Job]])
  (internal->wire #:job{:id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7",
                        :title "Software Engineer II",
                        :description "We are looking for a Software Engineer II to join our team",
                        :created_at #inst "2024-03-22T14:53:47.479214000-00:00"})

;
  )
