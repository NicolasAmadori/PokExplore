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
        private val PROFILE_PIC_KEY = stringPreferencesKey("profile_pic")
        private val THEME_KEY = stringPreferencesKey("theme")
        private val COUNTRY_CODE_KEY = stringPreferencesKey("country_code")
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
                        phone = preferences[PHONE_KEY]?.toIntOrNull(),
                        profilePicUrl = preferences[PROFILE_PIC_KEY],
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

    val countryCode = dataStore.data
        .map { preferences ->
            try {
                preferences[COUNTRY_CODE_KEY]
            } catch (_: Exception) {
                ""
            }
        }

    suspend fun setUser(user: User) = dataStore.edit {
        it[EMAIL_KEY] = user.email
        it[FIRST_NAME_KEY] = user.firstName
        it[LAST_NAME_KEY] = user.lastName
        it[PHONE_KEY] = user.phone.toString()
        it[PROFILE_PIC_KEY] = user.profilePicUrl.toString()
    }

    suspend fun removeUser() = dataStore.edit {
        it.remove(EMAIL_KEY)
        it.remove(FIRST_NAME_KEY)
        it.remove(LAST_NAME_KEY)
        it.remove(PHONE_KEY)
        it.remove(PROFILE_PIC_KEY)
    }

    suspend fun setTheme(theme: Theme) = dataStore.edit { it[THEME_KEY] = theme.toString() }

    suspend fun setCountryCode(countryCode: String) = dataStore.edit { it[COUNTRY_CODE_KEY] = countryCode }

}