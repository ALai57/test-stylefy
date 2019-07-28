(ns test-react.views
  (:require
   [re-frame.core :as re-frame]
   [test-react.subs :as subs]
   [stylefy.core :as stylefy :refer [use-style]]
   [garden.units :as g]))

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
;; Animations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn frac->rad [x]
  (* 2 Math/PI x))

(def n-icons (count icon-list))

(defn create-keyframe [i]
  (stylefy/keyframes (str "expand-item-" i)
                     [(g/percent 50)
                      {:background-color "red"
                       :top (str (-> i
                                     (/ 8)
                                     frac->rad
                                     Math/sin
                                     (* 50)) "px")
                       :left "-100px"}]
                     [(g/percent 100)
                      {:background-color "black"
                       :top (str (-> i
                                     (/ 8)
                                     frac->rad
                                     Math/sin
                                     (* 50)) "px")
                       :left "200px"}]))

(defn myfunction []
  (let [mybutton (-> js/document
                     (.getElementById "mybutton"))]
    (println mybutton)
    (set! (.. mybutton -style -top) "100px")
    (println (.. mybutton -style -top))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DOM
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TEST/EXAMPLE CODE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def simple-box {:border "1px solid black"
                 :background-color "#FFDDDD"
                 :text-align :center
                 :padding "5px"
                 :width "150px"
                 :height "150px"
                 :position "absolute"
                 :background-repeat "no-repeat"
                 :background-position-x "center"
                 :background-position-y "center"
                 :border-radius "80px"})


(stylefy/keyframes "flash-item-2"
                   [(g/percent 0)
                    {:background-color "green"
                     :left "0px"}]
                   [(g/percent 50)
                    {:background-color "yellow"
                     :left "200px"}]
                   [(g/percent 100)
                    {:background-color "green"
                     :left "0px"}])

(def animated-box-2 (merge simple-box
                           {:background-image "url(images/home.svg)"
                            :border-radius "80px"
                            :animation-name "flash-item-2"
                            :animation-duration "3s"
                            :animation-iteration-count "infinite"}))

(defn make-box-style [img] (merge simple-box
                                  {:background-image (str "url(" img ")" )
                                   :border-radius "80px"
                                   :animation-name "flash-item-2"
                                   :animation-duration "3s"
                                   :animation-delay (str (/ 33 (rand-int 100)) "s")
                                   :animation-iteration-count "infinite"}))
(make-box-style (first icon-list))

(defonce active? (atom true))
(defn toggle-keyframe []
  (if @active?
    (stylefy/keyframes "flash-item-2")
    (stylefy/keyframes "flash-item-2"
                       [(g/percent 0)
                        {:background-color "green"
                         :left "0px"}]
                       [(g/percent 50)
                        {:background-color "yellow"
                         :left "200px"}]
                       [(g/percent 100)
                        {:background-color "green"
                         :left "0px"}]))
  (reset! active? (not @active?)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FINAL RENDERING
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn make-button [img]
  [:button (merge {:onClick toggle-keyframe}
                  (use-style (make-box-style img)))])

(defn make-buttons []
  [:div (map make-button icon-list)])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello frome " @name]
     [:div#image-container {:style {:position "absolute"
                                    :top "100px"
                                    :left "10%"
                                    :width "80%"
                                    :height "80%"}}]

     [:div.main-image {:style {:position "relative"
                               :top "50%"
                               :left "50%"}}
      [:button#animated-box-2 (merge {:onClick toggle-keyframe}
                                     (use-style animated-box-2))]]
     (make-buttons)
     [:div#s]
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
