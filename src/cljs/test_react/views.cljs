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
;; Animations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn frac->rad [x]
  (* 2 Math/PI x))

(def n-icons (count icon-list))

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
(def radius 200)
(defonce active? (atom true))
(defn calc-y-position [i]
  (pprint/cl-format nil "~,2fpx" (-> i
                                     (/ (count icon-list))
                                     frac->rad
                                     Math/sin
                                     (* radius))))
(defn calc-x-position [i]
  (pprint/cl-format nil "~,2fpx" (-> i
                                     (/ (count icon-list))
                                     frac->rad
                                     Math/cos
                                     (* radius))))

(defn create-expand-animation [i]
  (stylefy/keyframes (str"icon-" i "-open")
                     [(g/percent 0)
                      {:top "0px"
                       :left "0px"}]
                     [(g/percent 100)
                      {:top (calc-y-position i)
                       :left (calc-x-position i)}]))

(defn delete-expand-animation [i]
  (stylefy/keyframes (str"icon-" i "-open")))

(defn toggle-keyframe []
  (if @active?
    (reduce #(create-expand-animation %2) [] (range 8))
    (reduce #(delete-expand-animation %2) [] (range 8)))
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

(def center-item (merge simple-box
                        {:background-image "url(images/home.svg)"
                         :border-radius "80px"}))

(defn make-radial-icon-style [i img]
  (merge simple-box
         {:background-image (str "url(" img ")" )
          :border-radius "80px"
          :animation-name (str "icon-" i "-open")
          :animation-duration "1s"
          ;;:animation-delay (str (/ 33 (rand-int 100)) "s")
          :animation-fill-mode "forwards"
          ;;:animation-iteration-count "infinite"
          }))

(defn make-button [i img]
  [:button (merge {:onClick toggle-keyframe}
                  (use-style (make-radial-icon-style i img)))])

(defn make-buttons []
  [:div {:style {:position "relative"
                 :top "50%"
                 :left "50%"}}
   (map-indexed make-button icon-list)])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FINAL RENDERING
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]
     (rm/radial-menu icon-list nil)
     ]))


#_(defn -main-panel []
    (let [name (re-frame/subscribe [::subs/name])]
      [:div
       [:h1 "Hello from " @name]
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
