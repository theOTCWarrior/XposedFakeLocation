//Constants.kt
package com.noobexon.xposedfakelocation.data

// APP
const val MANAGER_APP_PACKAGE_NAME = "com.noobexon.xposedfakelocation"
const val SHARED_PREFS_FILE = "xposed_shared_prefs"

// KEYS
const val KEY_IS_PLAYING = "is_playing"

const val KEY_LAST_CLICKED_LOCATION = "last_clicked_location"

const val KEY_USE_ACCURACY = "use_accuracy"
const val KEY_ACCURACY  = "accuracy"

const val KEY_USE_ALTITUDE = "use_altitude"
const val KEY_ALTITUDE  = "altitude"

const val KEY_USE_RANDOMIZE  = "use_randomize"
const val KEY_RANDOMIZE_RADIUS = "randomize_radius"

 // DEFAULT VALUES
const val DEFAULT_USE_ACCURACY = false
const val DEFAULT_ACCURACY = 0.0

const val DEFAULT_USE_ALTITUDE = false
const val DEFAULT_ALTITUDE = 0.0

const val DEFAULT_USE_RANDOMIZE = false
const val DEFAULT_RANDOMIZE_RADIUS = 0.0

// MATH & PHYS
const val PI = 3.14159265359
const val RADIUS_EARTH = 6378137.0 // Approximately Earth's radius in meters
