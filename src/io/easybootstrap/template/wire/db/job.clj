(ns io.easybootstrap.template.wire.db.job
  (:require [malli.core :as m]))

(def Job [:map
          [:job/id uuid?]
          [:job/created_at {:optional true} inst?]
          [:job/description string?]
          [:job/title string?]])

(comment

  (m/validate
   [:vector Job]
   [#:job{:id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7",
          :title "Software Engineer II",
          :description "We are looking for a Software Engineer II to join our team",
          :created_at #inst "2024-03-22T14:32:32.913921000-00:00"}])

;
  )
