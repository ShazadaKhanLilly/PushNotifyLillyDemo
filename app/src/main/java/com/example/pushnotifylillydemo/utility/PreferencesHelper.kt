package com.example.pushnotifylillydemo.utility

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class to save and retrieve user data from shared preferences
 */

object PreferencesHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_DRUG = "user_drug"
    private const val KEY_USER_EMAIL = "user_email"


    fun saveUserDrug(context: Context, drug: String) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_DRUG, drug)
            apply()
        }
    }

    // Save the user's condition and email
    fun saveUserDrugAndEmail(context: Context, drug: String, email: String) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_DRUG, drug)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }

    fun getUserDrug(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (sharedPrefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            sharedPrefs.getString(KEY_USER_DRUG, null)
        } else {
            null
        }
    }

    fun getUserEmail(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (sharedPrefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            sharedPrefs.getString(KEY_USER_EMAIL, null)
        } else {
            null
        }
    }

    fun clearUserData(context: Context) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            clear()
            apply()
        }
    }
}