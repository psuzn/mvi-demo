package me.sujanpoudel.mvidemo.data

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(@ApplicationContext val context: Context) {
    private val userPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val isUserLoggedIn get() = token != null

    var token
        get() = userPreferences.getString(KEY_TOKEN, null)
        set(value) = userPreferences.edit { putString(KEY_TOKEN, value) }

    fun cleanUp() {
        userPreferences.edit(commit = true) { clear() }
    }

    companion object {
        const val KEY_TOKEN = "token"
    }

}