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

(def center-icon-radius "75px")
(def radial-icon-radius "75px")
(def icon-color-scheme (str "radial-gradient(#6B9EB8 5%, "
                            "#59B1DE 60%, "
                            "#033882 70%)"))

(def base-icon-style {:border "1px solid black"
                      :text-align :center
                      :padding "5px"
                      :position "absolute"
                      :background-repeat "no-repeat"
                      :background-position-x "center"
                      :background-position-y "center"
                      :border-radius "80px"})
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; RENDERING
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn expand-or-contract []
  (re-frame/dispatch [:toggle-menu]))

(defn icon-click-handler [icon-url]
  (fn [] (re-frame/dispatch [:click-radial-icon icon-url])))

(defn center-icon-style []
  (let [active-icon (re-frame/subscribe [:active-icon])]
    (merge base-icon-style
           {:background-image (str @active-icon
                                   ", "
                                   icon-color-scheme)
            :width center-icon-radius
            :height center-icon-radius})))

(defn make-radial-icon-style [i icon-url]
  (let [radial-menu-open? (re-frame/subscribe [:radial-menu-open?])
        animation (if @radial-menu-open?
                    (str "icon-" i "-open")
                    (str "icon-" i "-collapse"))]
    (merge base-icon-style
           {:background-image
            (str "url(" icon-url "), "
                 icon-color-scheme)
            :width radial-icon-radius
            :height radial-icon-radius
            :box-shadow "0 2px 5px 0 rgba(0, 0, 0, .26)"
            :animation-name animation
            :animation-duration "1s"
            :animation-fill-mode "forwards"})))

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
      :background-images icon-list
      :open? @radial-menu-open?
      :tooltip [:div#tooltip {:style {:text-align "left"
                                      :width "100px"}}
                [:p "My button is here!"]]

      :center-icon-radius center-icon-radius
      :on-center-icon-click expand-or-contract
      :center-icon-style-fn center-icon-style

      :radial-icon-radius radial-icon-radius
      :on-radial-icon-click icon-click-handler
      :radial-icon-style-fn make-radial-icon-style
      )])
  )

(comment
  ["images/accessibility.svg"
   "images/favorite.svg"
   "images/find-in-page.svg"
   "images/get-app.svg"
   "images/grade.svg"
   "images/home.svg"
   "images/language.svg"
   "images/lock.svg"]
  )
