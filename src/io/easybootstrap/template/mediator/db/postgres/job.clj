(ns io.easybootstrap.template.mediator.db.postgres.job
  (:require [io.easybootstrap.template.wire.db.job :as wire.db.job]
            [honey.sql :as sql]
            [io.easybootstrap.components.database :as components.database]
            [honey.sql.helpers :as sql.helpers]
            [io.easybootstrap.template.wire.common :as wire.common]))

(defn insert-job-transaction
  {:malli/schema [:=> [:cat wire.db.job/Job wire.common/DatabaseComponent] :any]}
  [transaction db]
  (->> (-> (sql.helpers/insert-into :job)
           (sql.helpers/values [transaction])
           (sql.helpers/returning :*)
           sql/format)
       (components.database/execute db)
       first))

(defn get-all-jobs
  {:malli/schema [:=> [:cat wire.common/DatabaseComponent] [:vector wire.db.job/Job]]}
  [db]
  (components.database/execute db (-> (sql.helpers/select :id :title :description :created_at)
                                      (sql.helpers/from :job)
                                      sql/format)))

(defn get-by-id
  {:malli/schema [:=> [:cat :uuid wire.common/DatabaseComponent] wire.db.job/Job]}
  [id db]
  (->> (components.database/execute
        db
        (-> (sql.helpers/select :*)
            (sql.helpers/from :job)
            (sql.helpers/where [:= :id id])
            sql/format))
       first))
