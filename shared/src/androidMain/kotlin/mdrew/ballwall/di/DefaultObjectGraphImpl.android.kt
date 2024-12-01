package mdrew.ballwall.di

import android.content.Context
import mdrew.ballwall.preferences.DefaultUserPreferences
import mdrew.ballwall.preferences.UserPreferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformObjectGraph(private val context: Context) {
    private var userPreferences: UserPreferences? = null
    actual fun userPreferences(): UserPreferences {
        if (userPreferences == null) {
            userPreferences = DefaultUserPreferences(context)
        }
        return userPreferences!!
    }
}