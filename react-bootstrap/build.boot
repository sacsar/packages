(set-env!
  :resource-paths #{"resources"}
                  [cljsjs/react "15.0.1-1"]
  :dependencies '[[adzerk/bootlaces "0.1.13" :scope "test"]
                  [cljsjs/boot-cljsjs "0.6.0" :scope "test"]
                  [cljsjs/bootstrap "3.3.6-1"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "0.30.7")
(def +version+ (str +lib-version+ "-0"))


(task-options!
 pom  {:project     'cljsjs/react-bootstrap
       :version     +version+
       :description "The most popular front-end framework, rebuilt for React."
       :url         "https://react-bootstrap.github.io/index.html"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "https://raw.githubusercontent.com/react-bootstrap/react-bootstrap/master/LICENSE"}})

(deftask download-react-bootstrap []
  (download :url      (format "https://github.com/react-bootstrap/react-bootstrap-bower/archive/v%s.zip" +lib-version+)
            :checksum "5aa6815d722a25046f5ef2a2f9412098" ;;MD5
            :unzip    true))

(deftask package []
  (comp
    (download-react-bootstrap)
    (sift :move {#"^react-bootstrap-bower-.*/react-bootstrap.js"
                 "cljsjs/react-bootstrap/development/react-bootstrap.inc.js"
                 #"^react-bootstrap-bower-.*/react-bootstrap.min.js"
                 "cljsjs/react-bootstrap/production/react-bootstrap.min.inc.js"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.react-bootstrap"
               :requires ["cljsjs.react.dom"])
    (pom)
    (jar)))
