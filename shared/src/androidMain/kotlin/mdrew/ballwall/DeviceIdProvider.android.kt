package mdrew.ballwall

import android.app.Application

internal class DefaultDeviceIdProvider(private val application:Application): DeviceIdProvider {
    override val deviceId: String
        get() = "application"
}