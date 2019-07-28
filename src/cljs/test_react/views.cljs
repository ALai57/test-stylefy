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
;; ON CLICK BEHAVIOR
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

(def center-item (merge simple-box
                        {:background-image "url(images/home.svg)"
                         :border-radius "80px"}))

(defn make-radial-icon-style [img]
  (merge simple-box
         {:background-image (str "url(" img ")" )
          :border-radius "80px"
          :animation-name "flash-item-2"
          :animation-duration "3s"
          :animation-delay (str (/ 33 (rand-int 100)) "s")
          :animation-iteration-count "infinite"}))

(defn make-button [img]
  [:button (merge {:onClick toggle-keyframe}
                  (use-style (make-radial-icon-style img)))])

(defn make-buttons []
  [:div {:style {:position "relative"
                 :top "50%"
                 :left "50%"}}
   (map make-button icon-list)])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FINAL RENDERING
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello frome " @name]
     [:div#image-container {:style {:position "absolute"
                                    :top "100px"
                                    :left "10%"
                                    :width "80%"
                                    :height "80%"}}
      [:div.main-image {:style {:position "relative"
                                :top "50%"
                                :left "50%"}}
       [:button#center-item (merge {:onClick toggle-keyframe}
                                   (use-style center-item))]]
      (make-buttons)]
     ]))

