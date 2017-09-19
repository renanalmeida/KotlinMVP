package com.github.colecionista.data.source.local

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by renan on 16/09/17.
 */
class SharedPreferenceService(context: Context) {

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getUserName(): String =
            sharedPreferences.getString("userName", "")

    fun setUserName(value: String) {
        sharedPreferences.edit()
                .putString("userName", value)
                .apply()
    }

    fun getUserEmail(): String =
            sharedPreferences.getString("userEmail", "")

    fun setUserEmail(value: String) {
        sharedPreferences.edit()
                .putString("userEmail", value)
                .apply()
    }

    fun getPhotoUri(): String =
            sharedPreferences.getString("userPhotoUri", "")

    fun setPhotoUri(value: String) {
        sharedPreferences.edit()
                .putString("userPhotoUri", value)
                .apply()
    }
}

