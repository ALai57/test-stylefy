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
            [reagent.core    :as    reagent]))

(def radial-menu-args-desc
  [{:name :radial-menu-name     :required true  :default "radial-menu-1"   :type "string"          :validate-fn string?           :description [:span "the name of the icon." [:br] "For example, " [:code "\"radial-menu-1\""] " or " [:code "\"sports-menu\""]]}
   {:name :on-click         :required false                        :type "-> nil"          :validate-fn fn?               :description "a function which takes no params and returns nothing. Called when the button is clicked"}
   {:name :tooltip          :required false                        :type "string | hiccup" :validate-fn string-or-hiccup? :description "what to show in the tooltip"}
   {:name :tooltip-position :required false :default :below-center :type "keyword"         :validate-fn position?         :description [:span "relative to this anchor. One of " position-options-list]}
   {:name :emphasise?       :required false :default false         :type "boolean"                                        :description "if true, use emphasised styling so the button really stands out"}
   {:name :disabled?        :required false :default false         :type "boolean"                                        :description "if true, the user can't click the button"}
   {:name :class            :required false                        :type "string"          :validate-fn string?           :description "CSS class names, space separated (applies to the button, not the wrapping div)"}
   {:name :style            :required false                        :type "CSS style map"   :validate-fn css-style?        :description "CSS styles to add or override (applies to the button, not the wrapping div)"}
   {:name :attr             :required false                        :type "HTML attr map"   :validate-fn html-attr?        :description [:span "HTML attributes, like " [:code ":on-mouse-move"] [:br] "No " [:code ":class"] " or " [:code ":style"] "allowed (applies to the button, not the wrapping div)"]}])

(defn radial-menu
  "A radial menu!"
  []
  (let [showing? (reagent/atom false)]
    (fn
      [& {:keys [radial-menu-name on-click size tooltip tooltip-position emphasise? disabled? class style attr]
          :or   {:radial-menu-name "radial-menu-1"}
          :as   args}]
      {:pre [(validate-args-macro radial-menu-args-desc args "radial-menu-1")]}
      (when-not tooltip (reset! showing? false)) ;; To prevent tooltip from still showing after button drag/drop
      (let [the-menu [:div
                      (merge
                       {:class    (str
                                   "rc-md-icon-button noselect "
                                   (case size
                                     :smaller "rc-icon-smaller "
                                     :larger "rc-icon-larger "
                                     " ")
                                   (when emphasise? "rc-icon-emphasis ")
                                   (when disabled? "rc-icon-disabled ")
                                   class)
                        :style    (merge
                                   {:cursor (when-not disabled? "pointer")}
                                   style)
                        :on-click (handler-fn
                                   (when (and on-click (not disabled?))
                                     (on-click event)))}
                       (when tooltip
                         {:on-mouse-over (handler-fn (reset! showing? true))
                          :on-mouse-out  (handler-fn (reset! showing? false))})
                       attr)
                      [:i {:class (str "zmdi zmdi-hc-fw-rc " radial-menu-name)}]]]
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
