package mdrew.ballwall

import android.app.Activity

class DefaultApplicationExiter(private val activity: Activity): ApplicationExiter {
    override fun exit() {
        activity.finish()
    }
}