package mdrew.ballwall.preferences

interface UserPreferences {
    fun <T> get(key: Key, defaultValue:T): T
    fun <T> set(key: Key, value:T)

    enum class Key(val value:String) {
        REMEMBER_LOGIN("remember_login"),
        USERNAME("user"),
        PASSWORD("pass"),
        API_TOKEN("api_token"),
        API_TOKEN_TIME("api_token_time")
    }
}