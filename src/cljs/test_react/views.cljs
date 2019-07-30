(ns test-react.views
  (:require [cljs.pprint :as pprint]
            [garden.units :as g]
            [re-frame.core :as re-frame]
            [stylefy.core :as stylefy :refer [use-style]]
            [test-react.radial-menu :as rm]
            [test-react.recom-radial-menu :as rcm]
            [test-react.subs :as subs]))

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
                    :height "80%"}} v (rm/radial-menu icon-list nil)]
     ((rcm/radial-menu)
      :radial-menu-name "radial-menu-1"
      :tooltip "My button is here!")]))
