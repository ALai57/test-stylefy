(ns test-react.views
  (:require
   [re-frame.core :as re-frame]
   [test-react.subs :as subs]
   [stylefy.core :as stylefy :refer [use-style]]
   [garden.units :as g]))

(stylefy/init)

(def icon-list ["images/accessibility.svg"
                "images/favorite.svg"
                "images/find-in-page.svg"
                "images/get-app.svg"
                "images/grade.svg"
                "images/home.svg"
                "images/language.svg"
                "images/lock.svg"])

(defn myfunction []
  (let [mybutton (-> js/document
                     (.getElementById "mybutton"))]
    (println mybutton)
    (set! (.. mybutton -style -top) "10px")
    (println (.. mybutton -style -top))))

(defn main-circle-icon []
  [:div.main-image {:style {:position "relative"
                            :top "50%"
                            :left "50%"}}
   [:button#mybutton  {:onClick myfunction
                       :style {:position "absolute"
                               :background-image "url(images/home.svg)"
                               :background-repeat "no-repeat"
                               :background-position-x "center"
                               :background-position-y "center"
                               :background-color "blue"
                               :height "80px"
                               :width "80px"
                               :border-radius "80px"
                               :animation-delay "0.24s"
                               :animation-duration "0.4s"
                               :animation-timing-function "ease-out"
                               :animation-name "contract-item"
                               :animation-fill-mode "backwards"
                               }}]])


(defn outer-circle-icons [img-path]
  [:div.outer-image {:style {:position "relative"
                             :top "50%"
                             :left "50%"}}
   [:img {:src img-path
          :style {:position "absolute"
                  :background-color "blue"
                  :height "80px"
                  :width "80px"
                  :border-radius "80px"}}]])

#_(stylefy/keyframes "simple-animation"
                     [:from
                      {:background-color "red"}]
                     [:to
                      {:background-color "blue"}])

(stylefy/keyframes "expand-item"
                   [(g/percent 50)
                    {:background-color "red"}]
                   [(g/percent 100)
                    {:background-color "blue"}])

(def simple-box {:border "1px solid black"
                 :background-color "#FFDDDD"
                 :text-align :center
                 :padding "5px"
                 :width "150px"
                 :height "150px"})

(def animated-box (merge simple-box
                         {:animation-name "expand-item"
                          :animation-duration "3s"
                          :animation-iteration-count "infinite"}))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello frome " @name]
     [:div#image-container {:style {:position "absolute"
                                    :top "100px"
                                    :left "10%"
                                    :width "80%"
                                    :height "80%"}}
      (map outer-circle-icons icon-list)
      [main-circle-icon]]
     [:div#animated-box (use-style animated-box)]
     ]))


(comment
  (require '[garden.stylesheet :refer [at-keyframes]])
  (require '[garden.core :refer [css]])
  (require '[garden.compiler :as compiler])
  (let [identified "expand-item"
        frames [[(g/percent 50)
                 {:background-color "red"}]
                [(g/percent 100)
                 {:background-color "blue"}]
                [:from
                 {:background-color "red"}]]
        adk (apply at-keyframes identifier frames)
        css_result (css adk) ;; problem is here!
        [flags & rules] adk
        c_result (compiler/do-compile flags rules)
        exr (->> (compiler/expand-stylesheet rules)
                 (filter compiler/top-level-expression?)
                 (map #(println "TYPE!!!!! "(type %)))
                 #_(map #(println "NEXTCHECK" %))
                 #_(map compiler/render-css ) ;; THe problem is in the expand functions missing selectors
                 #_(remove nil?)              ;; BEFORE CSSAtRule
                 #_(rule-join))
        ]
    (println "-----------------------DONE ----------------")
    (println "-----------------------DONE ----------------")
    (println "-----------------------DONE ----------------"))
  )
