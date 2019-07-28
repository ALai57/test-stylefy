(ns test-react.views
  (:require
   [re-frame.core :as re-frame]
   [test-react.subs :as subs]
   ;;[stylefy.core :as stylefy]
   ))

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

#_(stylefy/keyframes "simple-animation"
                     [:from
                      {:background-color "red"}]
                     [:to
                      {:background-color "blue"}])

;; var style = document.createElement('style');
;; style.type = 'text/css';
;; var keyFrames = '\
;; @-webkit-keyframes spinIt {\
;;     100% {\
;;         -webkit-transform: rotate(A_DYNAMIC_VALUE);\
;;     }\
;; }\
;; @-moz-keyframes spinIt {\
;;     100% {\
;;         -webkit-transform: rotate(A_DYNAMIC_VALUE);\
;;     }\
;; }';
;; style.innerHTML = keyFrames.replace(/A_DYNAMIC_VALUE/g, "180deg");
;; document.getElementsByTagName('head')[0].appendChild(style);

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
     ]))
