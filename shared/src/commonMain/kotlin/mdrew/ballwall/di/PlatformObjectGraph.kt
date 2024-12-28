package mdrew.ballwall.di

import mdrew.ballwall.ApplicationExiter
import mdrew.ballwall.DeviceIdProvider
import mdrew.ballwall.preferences.UserPreferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PlatformObjectGraph {
    fun userPreferences(): UserPreferences
    fun applicationExiter(): ApplicationExiter
    fun deviceIdProvider(): DeviceIdProvider
}