(ns lsystem.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [lsystem.gen :refer :all]
            [lsystem.operators :refer :all]))

(def system '{:axiom [A]
              :rules {B [B B]
                      A [B O A C A]}})

(def angle-delta (/ Math/PI 4))

(def interpretation
  {'A []
   'B [draw-forward]
   'O [push-state (turn (- angle-delta))]
   'C [pop-state (turn angle-delta)]})

;(def system '{:axiom [F - G - G]
;              :rules {F [F − G + F + G − F]
;                      G [G G]}})
;
;(def interpretation {'F [draw-forward]
;                     'G [draw-forward]
;                     '+ [(turn (- (* 0.33 Math/PI)))]
;                     '- [(turn (* 0.33 Math/PI))]})

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 2)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:str (:axiom system)
   :step 0})

(defn update-state [{:keys [str step]}]
  {:str (expand system str)
   :step (inc step)})





(defn symbols-to-actions [str]
  (mapcat #(get interpretation %) str))

(defn draw-state [{:keys [str step]}]
  (loop [actions (symbols-to-actions str), render-state initial-state]
    (when-let [action (first actions)]
      (let [new-render-state (action render-state)]
        (recur (rest actions) new-render-state))))
  (when (> step 300) (q/no-loop)))

;(defn draw-state [state]
;  ; Clear the sketch by filling it with light-grey color.
;  (q/background 240)
;  ; Set circle color.
;  (q/fill (:color state) 255 255)
;  ; Calculate x and y coordinates of the circle.
;  (let [angle (:angle state)
;        x (* 150 (q/cos angle))
;        y (* 150 (q/sin angle))]
;    ; Move origin point to the center of the sketch.
;    (q/with-translation [(/ (q/width) 2)
;                         (/ (q/height) 2)]
;      ; Draw the circle.
;      (q/ellipse x y 100 100))))

(q/defsketch lsystem
  :title "You spin my circle right round"
  :size [800 800]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
