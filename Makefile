# Requirements
# ------------------------------------------

# .PHONY: ensures target used rather than matching file name
# https://makefiletutorial.com/#phony
.PHONY: deps

deps: deps.edn  ## Prepare dependencies for test and dist targets
	$(info --------- Download test and service libraries ---------)
	clojure -P -X:build

# to build the yml service

# to build the api service
build-template: build-uberjar-api ## Build and package Clojure service
	$(info --------- Build and Package Clojure service ---------)

build-uberjar-api: ## Build a uberjar archive of Clojure project & Clojure runtime
	$(info --------- Build service Uberjar  ---------)
	clojure -T:build uberjar

