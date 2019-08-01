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

(defn toggle-animation []
  (re-frame/dispatch [:toggle-menu]))

(def base-icon-style {:border "1px solid black"
                      :background-color "#FFDDDD"
                      :text-align :center
                      :padding "5px"
                      :width "75px"
                      :height "75px"
                      :position "absolute"
                      :background-repeat "no-repeat"
                      :background-position-x "center"
                      :background-position-y "center"
                      :border-radius "80px"})

(defn center-icon-style []
  (let [active-icon (re-frame/subscribe [:active-icon])]
    (merge base-icon-style
           {:background-image
            @active-icon
            :border-radius "80px"})))


(defn main-panel []
  (let [radial-menu-open? (re-frame/subscribe [:radial-menu-open?])]
    [:div
     [:h1 (str "Is the radial menu open? " @radial-menu-open?)]
     [:div {:style {:position "static"
                    :height "275px"
                    :width "275px"}}
      [:div {:style {:position "absolute"}} (rm/radial-menu icon-list nil)]]
     ((rcm/radial-menu)
      :radial-menu-name "radial-menu-1"
      :menu-radius "100px"
      :background-images ["images/home.svg" "images/lock.svg"]
      :open? @radial-menu-open?
      :center-on-click toggle-animation
      :center-icon-style (center-icon-style)
      :tooltip [:div#tooltip {:style {:text-align "left"
                                      :width "100px"}}
                [:p "My button is here!"]]
      :radial-icon-style {:width "100px"
                          :height "100px"
                          :background-color "aquamarine"
                          :position "static"})]))
