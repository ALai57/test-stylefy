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

(defn icon-click-handler [icon-url]
  (println "clicked: " icon-url)
  (fn [] (re-frame/dispatch [:click-radial-icon icon-url])))

(def base-icon-style {:border "1px solid black"
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
           {:background-image (str @active-icon
                                   ", radial-gradient(#6B9EB8 5%, #59B1DE 60%, #033882 70%)")})))

(defn make-radial-icon-style [i icon-url]
  (let [radial-menu-open? (re-frame/subscribe [:radial-menu-open?])
        animation (if @radial-menu-open?
                    (str "icon-" i "-open")
                    (str "icon-" i "-collapse"))]
    (merge base-icon-style
           {:background-image
            (str "url(" icon-url "), "
                 "radial-gradient(#6B9EB8 5%, #59B1DE 60%, #033882 70%)")
            :box-shadow "0 2px 5px 0 rgba(0, 0, 0, .26)"
            :animation-name animation
            :animation-duration "1s"
            :animation-fill-mode "forwards"})))

(def main-image-style {:position "absolute"
                       :top "100px"
                       :left "calc(50% - 75px/2)"
                       :z-index "4"})

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
      :center-icon-radius "75px"
      :radial-icon-radius "75px"
      :background-images ["images/home.svg" "images/lock.svg"]
      :open? @radial-menu-open?
      :center-on-click toggle-animation
      :radial-on-click icon-click-handler
      :center-icon-style-fn center-icon-style
      :tooltip [:div#tooltip {:style {:text-align "left"
                                      :width "100px"}}
                [:p "My button is here!"]]
      :radial-icon-style-fn make-radial-icon-style)])
  )
