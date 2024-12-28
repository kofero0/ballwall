package mdrew.ballwall.preferences

import android.content.Context

class DefaultUserPreferences(context:Context) : UserPreferences {
    private val sharedPrefs =
        context.getSharedPreferences("",Context.MODE_PRIVATE)

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: UserPreferences.Key, defaultValue: T): T {
        return when(defaultValue){
            is Boolean -> { sharedPrefs.getBoolean(key.value,defaultValue) as T }
            is Int -> { sharedPrefs.getInt(key.value,defaultValue) as T }
            is String -> {(sharedPrefs.getString(key.value,defaultValue) ?: "") as T}
            else -> { defaultValue }
        }
    }

    override fun <T> set(key: UserPreferences.Key, value: T) {
        when(value){
            is Boolean -> { sharedPrefs.edit().putBoolean(key.value,value).apply() }
            is Int -> { sharedPrefs.edit().putInt(key.value,value).apply() }
            is String -> { sharedPrefs.edit().putString(key.value,value).apply() }
        }
    }
}