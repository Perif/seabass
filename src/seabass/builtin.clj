(ns seabass.builtin
  (:import [com.hp.hpl.jena.reasoner.rulesys.builtins BaseBuiltin])
  (:import [com.hp.hpl.jena.reasoner.rulesys Util])
  (:import [com.hp.hpl.jena.datatypes.xsd XSDDatatype])
  (:import [com.hp.hpl.jena.graph Node])
  (:require [clojure.math.numeric-tower :as math]))
					
(defn timeDiff [label n]
  (proxy [BaseBuiltin] []
    (getName [] label)
    (getArgLength [] 3)
    (bodyCall [args len ctx]
	      (let [env	(.getEnv ctx)
		    t1 (nth args 0)
		    t2 (nth args 1)
		    t3 (nth args 2) ]
		(if (and (Util/isInstant t1) (Util/isInstant t2))
		  (let [c1 (-> t1 .getLiteralValue .asCalendar .getTimeInMillis (/ n) double)
                        c2 (-> t2 .getLiteralValue .asCalendar .getTimeInMillis (/ n) double)
                        diff (-> (- c2 c1) math/abs str (Node/createLiteral ,,, "" XSDDatatype/XSDdouble))]
		    (.bind env t3 diff))
		  nil)))))

(def diff-second (timeDiff "diff-second" 1000))
(def diff-minute (timeDiff "diff-minute" (* 1000 60)))
(def diff-hour (timeDiff "diff-hour" (* 1000 60 60)))
(def diff-day (timeDiff "diff-day" (* 1000 60 60 24)))