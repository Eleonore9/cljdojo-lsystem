(ns lsystem.operators
  (:require [quil.core :as q]))

(def segment 0.1)

(def initial-state
  {:position [0 400]
   :angle 0
   :stack '()})

(defn compute-point [[x y] angle]
  [(+ x (* segment (Math/cos angle)))
   (+ y (* segment (Math/sin angle)))])

(defn draw-forward [render-state]
  (let [next-pos (compute-point (:position render-state)
                                (:angle render-state))
        [x1 y1] (:position render-state)
        [x2 y2] next-pos]
    (q/line x1 y1 x2 y2)
    (assoc render-state :position next-pos)))

(defn turn [angle]
  (fn [render-state]
    (assoc render-state :angle (+ angle (:angle render-state)))))


(defn push-state [render-state]
  (assoc render-state
         :stack
         (cons (select-keys render-state [:position :angle])
               (:stack render-state))))

(defn pop-state [render-state]
  (if-let [saved (first (:stack render-state))]
    {:position (:position saved)
     :angle (:angle saved)
     :stack (rest (:stack render-state))}
    render-state))


