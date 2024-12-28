package mdrew.ballwall.di

import mdrew.ballwall.preferences.DefaultUserPreferences
import mdrew.ballwall.preferences.UserPreferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object PlatformObjectGraph {
    actual fun userPreferences(): UserPreferences = DefaultUserPreferences
}