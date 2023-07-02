(ns omniward-client.components.validators)

(defn validate-date-of-birth
  [dob]
  (let [valid? (try
                 (let [parsed-dob   (js/Date. dob)
                       current-date (js/Date.)
                       invalid? (> (.getTime parsed-dob) (.getTime current-date))]
                   (if invalid?
                     "Invalid date of birth"
                     nil))
                 (catch js/Error _ "Invalid date of birth"))]
    valid?))

(defn validate-address
  [address]
  (let [address-length (count address)]
    (if (<= 5 address-length 100)
      nil
      "should be between 5 and 100 characters")))

(defn validate-phone-number
  [phone]
  (let [phone-pattern #"\d{10}"
        valid? (try
                 (re-find phone-pattern phone)
                 (catch js/Error _ false))]
    (if (not valid?)
      "Invalid phone number"
      nil)))

(defn validate-name
  "Validate the patient's full name"
  [name]
  (let [name-pattern #"[A-Za-z ]+"
        valid? (try
                 (re-find name-pattern  name)
                 (catch js/Error _ false))]
    (if (not valid?)
      "Invalid name"
      nil)))