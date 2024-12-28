package mdrew.ballwall.di

import android.app.Activity
import android.app.Application
import android.content.Context
import mdrew.ballwall.ApplicationExiter
import mdrew.ballwall.DefaultApplicationExiter
import mdrew.ballwall.DefaultDeviceIdProvider
import mdrew.ballwall.DeviceIdProvider
import mdrew.ballwall.preferences.DefaultUserPreferences
import mdrew.ballwall.preferences.UserPreferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformObjectGraph(private val activity: Activity) {
    private var userPreferences: UserPreferences? = null
    actual fun userPreferences(): UserPreferences {
        if (userPreferences == null) {
            userPreferences = DefaultUserPreferences(activity)
        }
        return userPreferences!!
    }

    private var applicationExiter: ApplicationExiter? = null
    actual fun applicationExiter(): ApplicationExiter {
        if(applicationExiter == null){
            applicationExiter = DefaultApplicationExiter(activity)
        }
        return applicationExiter!!
    }

    private var deviceIdProvider: DeviceIdProvider? = null
    actual fun deviceIdProvider(): DeviceIdProvider {
        if(deviceIdProvider == null){
            deviceIdProvider = DefaultDeviceIdProvider(activity.application)
        }
        return deviceIdProvider!!
    }
}