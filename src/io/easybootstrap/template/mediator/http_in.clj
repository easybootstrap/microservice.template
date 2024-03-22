(ns io.easybootstrap.template.mediator.http-in
  (:require
   [io.easybootstrap.template.adapters.job :as adapters.job]
   [io.easybootstrap.template.controllers.job :as controllers.job]
   [io.easybootstrap.template.wire.in.job :as wire.in.job]
   [io.easybootstrap.template.wire.out.job :as wire.out.job]
   [reitit.swagger :as swagger]))

(defn get-all [{components :components}]
  (let [jobs (->> (controllers.job/get-all components)
                  (map adapters.job/internal->wire))]
    {:status 200
     :body {:jobs jobs}}))

(defn create-job-post!
  [{{job :body} :parameters
    components :components}]
  (let [job (-> job
                (adapters.job/wire->internal)
                (controllers.job/create-job! components)
                (adapters.job/internal->wire))]

    {:status 201
     :body  job}))

(defn get-job-by-id
  [{{:keys [id]} :path-params
    components :components :as _request}]
  (let [job (-> (controllers.job/get-by-id (parse-uuid id) components)
                adapters.job/internal->wire)]
    {:status 200
     :body job}))

(def routes
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "Simple job posting"
                            :description "A simple job board API as template"}}
           :handler (swagger/create-swagger-handler)}}]

   ["/job"
    {:swagger {:tags ["job"]}}

    ["/"
     {:get {:summary "get all jobs"
            :responses {200 {:body wire.out.job/Jobs}
                        500 {:body :string}}
            :handler get-all}
      :post {:summary "create a job"
             :parameters {:body wire.in.job/Job}
             :responses {201 {:body wire.out.job/Job}
                         400 {:body :string}
                         500 {:body :string}}
             :handler create-job-post!}}]

    ["/:id"
     {:get {:summary "get job by id"
            :responses {200 {:body wire.out.job/Job}
                        400 {:body :string}
                        500 {:body :string}}
            :handler get-job-by-id}}]]])

