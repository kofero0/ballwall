package mdrew.ballwall.preferences

import android.content.Context

class DefaultUserPreferences(context:Context) : UserPreferences {
    private val sharedPrefs =
        context.getSharedPreferences("",Context.MODE_PRIVATE)

    override fun <T> get(key: UserPreferences.Key, defaultValue: T): T {
//        return when(defaultValue){
//            is Boolean -> {  }
//            is Int -> {}
//            else -> { defaultValue }
//        }
        return defaultValue
    }

    override fun <T> set(key: UserPreferences.Key, value: T) {
        TODO("Not yet implemented")
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserPreferencesBuilder {
    actual fun build(): UserPreferences {
        TODO("Not yet implemented")
    }
}