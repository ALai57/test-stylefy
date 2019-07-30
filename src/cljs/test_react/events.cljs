(ns test-react.events
  (:require
   [re-frame.core :as re-frame]
   [test-react.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :toggle-menu
 (fn [db [_ value]]
   (let [new-value (not (:radial-menu-open? db))]
     (assoc db :radial-menu-open? new-value))))
