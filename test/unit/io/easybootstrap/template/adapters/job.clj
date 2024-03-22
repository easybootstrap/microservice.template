(ns unit.io.easybootstrap.template.adapters.job
  (:require [clojure.test :refer [deftest is testing]]
            [io.easybootstrap.template.adapters.job :as adapters.job]
            [state-flow.assertions.matcher-combinators :refer [match?]]))

(deftest internal->wire
  (testing "convert internal job to wire job"
    (let [id (random-uuid)]
      (is (match? {:id id
                   :description "test",
                   :created-at #inst "2021-09-01T00:00:00.000-00:00"}
                  (adapters.job/internal->wire {:job/id id
                                                :job/title "test"
                                                :job/description "test"
                                                :job/created_at #inst "2021-09-01T00:00:00.000-00:00"}))))))

(deftest wire->internal
  (testing "convert internal to wire job"
    (is (match?
         {:job/id uuid? :job/title "test" :job/description "test"}
         (adapters.job/wire->internal {:title "test"
                                       :description "test"})))))
