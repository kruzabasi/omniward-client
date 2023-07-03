(ns omniward-client.components.validators)

(defn error-message
  [input valid? msg]
  (cond
    (nil? input) "empty"
    valid?       nil
    :else msg))

(defn validate-date
  [dob]
  (try
    (let [parsed-dob (js/Date. dob)
          current-date (js/Date.)
          valid? (> (.getTime current-date) (.getTime parsed-dob))]
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
  (let [phone-pattern #"\d{10}"
        valid? (try
                 (re-find phone-pattern phone)
                 (catch js/Error _ false))]
    (error-message phone valid? "Invalid phone number")))

(defn validate-name
  "Validate the patient's full name"
  [name]
  (let [name-pattern #"[A-Za-z ]+"
        valid? (try
                 (re-find name-pattern  name)
                 (catch js/Error _ false))]
    (error-message name valid? "Invalid name")))