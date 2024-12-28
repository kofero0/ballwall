package mdrew.ballwall.preferences

import platform.Foundation.NSUserDefaults

object DefaultUserPreferences : UserPreferences {
   private val defaults = NSUserDefaults.standardUserDefaults
    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: UserPreferences.Key, defaultValue: T): T {
        return when(defaultValue){
            is Boolean -> defaults.boolForKey(key.value) as T
            is Int -> defaults.integerForKey(key.value) as T
            is String -> defaults.stringForKey(key.value) as T
            else -> throw RuntimeException("how did we get here?")
        }
    }

    override fun <T> set(key: UserPreferences.Key, value: T) {
        when(value){
            is Boolean -> defaults.setBool(value,key.value)
            is Int -> defaults.setInteger(value.toLong(),key.value)
            is String -> defaults.setObject(value,key.value)
        }
    }
}