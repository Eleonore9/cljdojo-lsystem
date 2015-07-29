(ns lsystem.gen)

(def algae '{:axiom [A]
             :rules {A [A B]
                     B [A]}})

(defn expand [sys s]
  (let [rules (:rules sys)]
    (mapcat #(get rules % [%]) s)))

(expand algae '[A C A])

(defn expansions [sys]
  (iterate #(expand sys %) (:axiom sys)))

(take 10 (expansions algae))

