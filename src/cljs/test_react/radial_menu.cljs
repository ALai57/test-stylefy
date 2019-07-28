(ns test-react.radial-menu
  (:require
   [re-frame.core :as re-frame]
   [test-react.subs :as subs]
   [stylefy.core :as stylefy :refer [use-style]]
   [garden.units :as g]
   [cljs.pprint :as pprint]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Setup/init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(stylefy/init)
(defonce active? (atom true))

(def image-container-style {:position "absolute"
                            :top "100px"
                            :left "10%"
                            :width "80%"
                            :height "80%"})

(def main-image-style {:position "relative"
                       :top "50%"
                       :left "50%"})

(def base-icon-style {:border "1px solid black"
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

(def center-icon-style (merge base-icon-style
                              {:background-image "url(images/home.svg)"
                               :border-radius "80px"}))

(def radial-icons-style {:position "relative"
                         :top "50%"
                         :left "50%"})
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Animations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn frac->rad [x]
  (* 2 Math/PI x))

(defn calc-y-position [radius i n-icons]
  (pprint/cl-format nil "~,2fpx" (-> i
                                     (/ (count n-icons))
                                     frac->rad
                                     Math/sin
                                     (* radius))))
(defn calc-x-position [radius i n-icons]
  (pprint/cl-format nil "~,2fpx" (-> i
                                     (/ (count n-icons))
                                     frac->rad
                                     Math/cos
                                     (* radius))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ON CLICK BEHAVIOR
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-expand-animation [radius i n-icons]
  (stylefy/keyframes (str "icon-" i "-open")
                     [(g/percent 0)
                      {:top "0px"
                       :left "0px"}]
                     [(g/percent 100)
                      {:top (calc-y-position radius i n-icons)
                       :left (calc-x-position radius i n-icons)}]))

(defn delete-expand-animation [i]
  (stylefy/keyframes (str"icon-" i "-open")))


;; Instaed of deleting - have a property for forward and reverse animation...
(defn toggle-keyframe []
  (if @active?
    (reduce #(create-expand-animation %2) [] (range 8))
    (reduce #(delete-expand-animation %2) [] (range 8)))
  (reset! active? (not @active?)))

(reduce #(create-expand-animation %2 %3 %4) []
        [[100 100] [1 2] [8 8]] #_(partition (constantly 100)
                                             (range 8)
                                             (constantly 8)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TEST/EXAMPLE CODE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn make-radial-icon-style [i img]
  (merge base-icon-style
         {:background-image (str "url(" img ")" )
          :border-radius "80px"
          :animation-name (str "icon-" i "-open")
          :animation-duration "1s"
          :animation-fill-mode "forwards"}))

(defn create-radial-icon [i img]
  [:button (merge {:onClick toggle-keyframe}
                  (use-style (make-radial-icon-style i img)))])

(defn create-radial-icons [icons]
  [:div {:style radial-icons-style}
   (map-indexed create-radial-icon icons)])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FINAL RENDERING
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn radial-menu [icons props]
  [:div#image-container {:style image-container-style}
   [:div.main-image {:style main-image-style}
    [:button#center-icon (merge {:onClick toggle-keyframe}
                                (use-style center-icon-style))]]
   (create-radial-icons icons props)])
