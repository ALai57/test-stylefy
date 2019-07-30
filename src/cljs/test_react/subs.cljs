(ns test-react.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :radial-menu-open?
 (fn [db]
   (:radial-menu-open? db)))

(re-frame/reg-sub
 :active-icon
 (fn [db]
   (:active-icon db)))
