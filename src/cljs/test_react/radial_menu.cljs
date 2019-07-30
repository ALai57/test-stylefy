(ns test-react.radial-menu
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [test-react.subs :as subs]
   [stylefy.core :as stylefy :refer [use-style]]
   [garden.units :as g]
   [cljs.pprint :as pprint]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Setup/init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(stylefy/init)
(defonce active? (atom true))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Animations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn frac->rad [x]
  (* 2 Math/PI x))

(defn calc-y-position [radius i n-icons]
  (pprint/cl-format nil "~,2fpx" (-> i
                                     (/ n-icons)
                                     frac->rad
                                     Math/sin
                                     (* radius))))
(defn calc-x-position [radius i n-icons]
  (pprint/cl-format nil "~,2fpx" (-> i
                                     (/ n-icons)
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

(defn create-collapse-animation [radius i n-icons]
  (stylefy/keyframes (str "icon-" i "-collapse")
                     [(g/percent 0)
                      {:top (calc-y-position radius i n-icons)
                       :left (calc-x-position radius i n-icons)}]
                     [(g/percent 100)
                      {:top "0px"
                       :left "0px"}]))

(defn delete-expand-animation [i]
  (stylefy/keyframes (str"icon-" i "-open")))
(defn delete-collapse-animation [i]
  (stylefy/keyframes (str"icon-" i "-collapse")))


;; Instaed of deleting - have a property for forward and reverse animation...
(defn toggle-animation []
  (dispatch [:toggle-menu]))

(comment
  (create-expand-animation 200 1 8)
  (create-collapse-animation 200 1 8))

(reduce #(create-expand-animation (first %2)
                                  (second %2)
                                  (nth %2 2))
        []
        [[100 0 8]
         [100 1 8]
         [100 2 8]
         [100 3 8]
         [100 4 8]
         [100 5 8]
         [100 6 8]
         [100 7 8]])
(reduce #(create-collapse-animation (first %2)
                                    (second %2)
                                    (nth %2 2))
        []
        [[100 0 8]
         [100 1 8]
         [100 2 8]
         [100 3 8]
         [100 4 8]
         [100 5 8]
         [100 6 8]
         [100 7 8]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TEST/EXAMPLE CODE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn make-radial-icon-style [i img]
  (let [radial-menu-open? (subscribe [:radial-menu-open?])
        animation (if @radial-menu-open?
                    (str "icon-" i "-open")
                    (str "icon-" i "-collapse"))]
    (merge base-icon-style
           {:background-image
            (str "url(" img "), "
                 "radial-gradient(#6B9EB8 5%, #59B1DE 60%, #033882 70%)")
            :border-radius "80px"
            :animation-name animation
            :box-shadow "0 2px 5px 0 rgba(0, 0, 0, .26)"
            :background-color "#6B9EB8"
            :animation-duration "1s"
            :animation-fill-mode "forwards"})))


(defn icon-click-handler [i]
  (fn []
    (dispatch [:click-radial-icon i])))

(defn create-radial-icon [i img]
  (let [radial-icon-style (make-radial-icon-style i img)
        img (:background-image radial-icon-style)]
    ^{:key (str "radial-" i)}
    [:button  (merge {:onClick (icon-click-handler img)}
                     (use-style radial-icon-style))]))

(defn create-radial-icons [icons]
  [:div {:style radial-icons-style}
   (doall (map-indexed create-radial-icon icons))])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; COMPONENT
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def image-container-style {:width "275px"
                            :height "275px"
                            :margin "auto"})

(def main-image-style {:position "absolute"
                       :top "100px"
                       :left "calc(50% - 75px/2)"
                       :z-index "4"})

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
  (let [active-icon (subscribe [:active-icon])]
    (merge base-icon-style
           {:background-image
            @active-icon
            :border-radius "80px"})))

(def radial-icons-style {:position "absolute"
                         :top "100px"
                         :left "calc(50% - 75px/2)"})


(defn radial-menu [icons props]
  [:div#image-container {:style image-container-style}
   [:div.main-image {:style main-image-style}
    [:button#center-icon (merge {:onClick toggle-animation}
                                (use-style (center-icon-style)))]]
   [:div {:style radial-icons-style}]
   (create-radial-icons icons props)])

