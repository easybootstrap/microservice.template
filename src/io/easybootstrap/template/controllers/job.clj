(ns io.easybootstrap.template.controllers.job
  (:require
   [io.easybootstrap.template.mediator.db.postgres.job :as db.postgres.job]
   [io.easybootstrap.template.wire.common :as wire.common]
   [io.easybootstrap.template.wire.db.job :as wire.db.job]))

(defn get-all
  {:malli/schema [:=> [:cat wire.common/Components] [:vector wire.db.job/Job]]}
  [{:keys [database]}]
  (db.postgres.job/get-all-jobs database))

(defn create-job!
  {:malli/schema [:=> [:cat wire.db.job/Job wire.common/Components] :any]}
  [job {:keys [database]}]
  (db.postgres.job/insert-job-transaction job database))

(defn get-by-id
  {:malli/schema [:=> [:cat :uuid wire.common/Components] wire.db.job/Job]}
  [id {:keys [database]}]
  (db.postgres.job/get-by-id id database))
