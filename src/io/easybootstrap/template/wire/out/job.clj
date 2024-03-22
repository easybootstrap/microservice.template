(ns io.easybootstrap.template.wire.out.job)

(def Job [:map
          [:id uuid?]
          [:created-at inst?]
          [:description string?]
          [:title string?]])

(def Jobs [:map [:jobs [:vector Job]]])
