(ns io.easybootstrap.template.wire.in.job)

(def Job [:map
          [:description string?]
          [:title string?]])

(def Jobs [:map [:jobs [:vector Job]]])

