(ns test-react.views
  (:require
   [re-frame.core :as re-frame]
   [test-react.subs :as subs]
   [test-react.radial-menu :as rm]
   [stylefy.core :as stylefy :refer [use-style]]
   [garden.units :as g]
   [cljs.pprint :as pprint]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Setup/init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(stylefy/init)
(def icon-list ["images/accessibility.svg"
                "images/favorite.svg"
                "images/find-in-page.svg"
                "images/get-app.svg"
                "images/grade.svg"
                "images/home.svg"
                "images/language.svg"
                "images/lock.svg"])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; RENDERING
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn main-panel []
  (let [radial-menu-open? (re-frame/subscribe [:radial-menu-open?])]
    [:div
     [:h1 (str "Is the radial menu open? " @radial-menu-open?)]
     [:div {:style {:position "absolute"
                    :top "100px"
                    :width "80%"
                    :height "80%"}} v (rm/radial-menu icon-list nil)]]))
