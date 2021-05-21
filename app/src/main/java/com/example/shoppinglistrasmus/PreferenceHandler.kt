package com.example.shoppinglistrasmus

import android.content.Context
import androidx.preference.PreferenceManager

// Singleton class to handle preferences from any fragment/activity
object PreferenceHandler {
    private const val SETTINGS_NAMEKEY = "name"
    private const val SETTINGS_NOTIFICATIONS = "notifications"

    fun useNotifications(context: Context) : Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_NOTIFICATIONS, true)
    }

    // These are static methods - meaning they always exists
    // so we do not have to create an instance of this class to
    // get the values - the Singleton patter again

    fun getName(context: Context) : String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SETTINGS_NAMEKEY, "")!!
    }
}