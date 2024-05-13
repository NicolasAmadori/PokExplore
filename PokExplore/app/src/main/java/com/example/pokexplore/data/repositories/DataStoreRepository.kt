package com.example.pokexplore.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.models.Theme
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val FIRST_NAME_KEY = stringPreferencesKey("first_name")
        private val LAST_NAME_KEY = stringPreferencesKey("last_name")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    val theme = dataStore.data
        .map { preferences ->
            try {
                Theme.valueOf(preferences[THEME_KEY] ?: "System")
            } catch (_: Exception) {
                Theme.System
            }
        }

    val user = dataStore.data
        .map { preferences ->
            try {
                if(preferences[EMAIL_KEY] != null) {
                    User(
                        email = preferences[EMAIL_KEY] ?: "",
                        firstName = preferences[FIRST_NAME_KEY] ?: "",
                        lastName = preferences[LAST_NAME_KEY] ?: "",
                        phone = 3,//TODO: change, ?.toInt() generate an error
                        profilePicUrl = null,
                        password = ""
                    )
                }
                else {
                    null
                }
            } catch (e: Exception) {
                Log.d("DataStoreRepository", e.message.toString())
                null
            }
        }

    suspend fun setUser(user: User) = dataStore.edit {
        it[EMAIL_KEY] = user.email
        it[FIRST_NAME_KEY] = user.firstName
        it[LAST_NAME_KEY] = user.lastName
        it[PHONE_KEY] = user.phone.toString()
    }
    suspend fun removeUser() = dataStore.edit {
        it.remove(EMAIL_KEY)
        it.remove(FIRST_NAME_KEY)
        it.remove(LAST_NAME_KEY)
        it.remove(PHONE_KEY)
    }

    suspend fun setTheme(theme: Theme) = dataStore.edit { it[THEME_KEY] = theme.toString() }

}