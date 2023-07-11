(ns omniward-client.components.validators)

(defn error-message
  [input valid? msg]
  (cond
    (nil? input) "empty"
    valid?       nil
    :else msg))

(defn validate-date
  [date]
  (try
    (let [parsed-date  (js/Date. date)
          current-date (js/Date.)
          valid?       (> (.getTime current-date) (.getTime parsed-date))]
      valid?)
    (catch js/Error _ false)))

(defn validate-date-of-birth
  [dob]
  (let [valid? (validate-date dob)]
    (error-message dob valid? "Invalid date of birth")))

(defn validate-gender
  "Validate the patient's gender"
  [gender]
  (let [valid-genders #{"Male" "Female" "Other"}
        valid? (contains? valid-genders gender)]
    (error-message gender valid? "Invalid gender")))

(defn validate-address
  [address]
  (let [address-length (count address)]
    (error-message
     address
     (<= 5 address-length 100)
     "should be between 5 and 100 characters")))

(defn validate-phone-number
  [phone]
  (let [phone-pattern #"[0-9]{10,15}"
        valid? (try
                 (re-matches phone-pattern phone)
                 (catch js/Error _ false))]
    (error-message phone valid? "Invalid phone number")))

(defn validate-name
  "Validate the patient's full name"
  [name]
  (let [name-pattern #"[A-Za-z ' - .]+"
        valid? (try
                 (and
                  (>= 40 (count name) 2)
                  (re-matches name-pattern  name))
                 (catch js/Error _ false))]
    (error-message name valid? "Invalid name")))