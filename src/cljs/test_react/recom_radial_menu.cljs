(ns test-react.recom-radial-menu
  (:require-macros [re-com.core :refer [handler-fn]])
  (:require [re-com.validate :refer [position?
                                     position-options-list
                                     button-size?
                                     button-sizes-list
                                     string-or-hiccup?
                                     css-style?
                                     html-attr?
                                     string-or-atom?] :refer-macros [validate-args-macro]]
            [re-com.popover  :refer [popover-tooltip]]
            [re-com.box      :refer [box]]
            [reagent.core    :as    reagent]
            [stylefy.core :as stylefy :refer [use-style]]))

(stylefy/init)

(def radial-menu-args-desc
  [{:name :radial-menu-name      :required true  :default "radial-menu-1" :type "string"          :validate-fn string?           :description [:span "the name of the icon." [:br] "For example, " [:code "\"radial-menu-1\""] " or " [:code "\"sports-menu\""]]}
   {:name :menu-radius           :required true  :default "100px"         :type "string"          :validate-fn string?           :description [:span "how far the icons move radially." [:br] "For example, " [:code "\"100px\""] " or " [:code "\"50px\""]]}
   {:name :center-icon-radius    :required true  :default "75px"          :type "string"          :validate-fn string?           :description [:span "how far the icons move radially." [:br] "For example, " [:code "\"100px\""] " or " [:code "\"50px\""]]}
   {:name :radial-icon-radius    :required true  :default "75px"          :type "string"          :validate-fn string?           :description [:span "how far the icons move radially." [:br] "For example, " [:code "\"100px\""] " or " [:code "\"50px\""]]}
   {:name :background-images     :required false                          :type "vector"          :validate-fn vector?           :description [:span "A list of all background image urls used for icons" [:br] "For example, " [:code "[\"images/home.svg\", \"images/lock.svg\"]"]]}
   {:name :center-on-click       :required false                          :type "-> nil"          :validate-fn fn?               :description "a function which takes no params and returns nothing. Called when the button is clicked"}
   {:name :radial-on-click       :required false                          :type "-> nil"          :validate-fn fn?               :description "a function which takes no params and returns nothing. Called when the button is clicked"}
   {:name :open?                 :required true                           :type "boolean"         :validate-fn boolean?          :description "is the radial menu open?"}
   {:name :tooltip               :required false                          :type "string | hiccup" :validate-fn string-or-hiccup? :description "what to show in the tooltip"}
   {:name :tooltip-position      :required false :default :below-center   :type "keyword"         :validate-fn position?         :description [:span "relative to this anchor. One of " position-options-list]}
   {:name :disabled?             :required false :default false           :type "boolean"                                        :description "if true, the user can't click the button"}
   {:name :class                 :required false                          :type "string"          :validate-fn string?           :description "CSS class names, space separated (applies to the button, not the wrapping div)"}
   {:name :center-icon-style-fn  :required false                          :type "-> nil"          :validate-fn fn?               :description "CSS styles to add or override (applies to the center icon)"}
   {:name :radial-icon-style-fn  :required false                          :type "-> nil"          :validate-fn fn?               :description "CSS styles to add or override (applies to the button, not the wrapping div)"}
   {:name :attr                  :required false                          :type "HTML attr map"   :validate-fn html-attr?        :description [:span "HTML attributes, like " [:code ":on-mouse-move"] [:br] "No " [:code ":class"] " or " [:code ":style"] "allowed (applies to the button, not the wrapping div)"]}])

(defn radial-menu
  "A radial menu!"
  []
  (let [showing? (reagent/atom false)]
    (fn
      [& {:keys [radial-menu-name
                 menu-radius center-icon-radius radial-icon-radius
                 background-images
                 center-on-click radial-on-click
                 open? size
                 tooltip tooltip-position
                 disabled? class
                 center-icon-style-fn radial-icon-style-fn attr]
          :or   {:radial-menu-name "radial-menu-1" :menu-radius "100px"}
          :as   args}]
      {:pre [(validate-args-macro radial-menu-args-desc args "radial-menu-1")]}
      (when-not tooltip (reset! showing? false)) ;; To prevent tooltip from still showing after button drag/drop

      (let [menu-size (str "calc(2*" menu-radius " + " radial-icon-radius ")")
            center-icon-position (str "calc(50% - " radial-icon-radius "/2)")
            create-radial-icon (fn [i icon-url]
                                 ^{:key (str "radial-" i)}
                                 [:button  (merge {:onClick (radial-on-click (str "url(" icon-url ")"))}
                                                  (use-style (radial-icon-style-fn i icon-url)))])

            create-radial-icons (fn [icon-urls]
                                  [:div {:style {:position "absolute"
                                                 :top menu-radius
                                                 :left menu-size}}
                                   (doall (map-indexed create-radial-icon icon-urls))])

            the-menu [:div {:style {:position "absolute"}}
                      [:div#image-container {:style {:width menu-size
                                                     :height menu-size
                                                     :margin "auto"}}
                       [:div.main-image {:style {:position "absolute"
                                                 :top menu-radius
                                                 :left center-icon-position
                                                 :z-index "4"}}
                        [:button#center-icon (merge {:onClick center-on-click}
                                                    (use-style (center-icon-style-fn)))]]
                       (create-radial-icons background-images)]]]

        [box
         :class "rc-md-icon-button-wrapper display-inline-flex"
         :align :start
         :child (if tooltip
                  [popover-tooltip
                   :label    tooltip
                   :position (or tooltip-position :below-center)
                   :showing? showing?
                   :anchor   the-menu]
                  the-menu)]))))


#_[:div
   (merge
    {:class    (str
                "rc-md-icon-button noselect "
                (case size
                  :smaller "rc-icon-smaller "
                  :larger "rc-icon-larger "
                  " ")
                (when disabled? "rc-icon-disabled ")
                class)
     :style    (merge
                {:cursor (when-not disabled? "pointer")}
                radial-icon-style)
     :on-click (handler-fn
                (when (and on-click (not disabled?))
                  (on-click event)))}
    (when tooltip
      {:on-mouse-over (handler-fn (reset! showing? true))
       :on-mouse-out  (handler-fn (reset! showing? false))})
    attr)
   [:i {:class (str "zmdi zmdi-hc-fw-rc " radial-menu-name)} menu-radius]
   [:p {:style {:background-color (if open? "red" "blue")}} (str background-images)]]
