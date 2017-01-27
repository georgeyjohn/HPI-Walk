# HPI-Walk

Library Used
•	GSON – To save User Details Distance Walked in JSON Format
•	Instacapture -To take screenshot 
•	Easylocation – To get GPS location

Recommend
Google Fit API – Google Fit API needed Google play credentials so not used in this application, otherwise it’s the best solution to find distance, time, calories burned for physical activities like walk, biking, etc.

Steps
Saving user data in Shared Preference in JSON Format – User Collection to save user data inside there is another collection walk to save user activities.
Used GPS to collect distance walked – Saving data to the collection for date change or user logout and add to User collection then to shared preference, data for a day are saving in shared preference.
On every Location Update, checking for milestone.

Alarm manager and push notification is used to Alert User periodically when user is in office. 
