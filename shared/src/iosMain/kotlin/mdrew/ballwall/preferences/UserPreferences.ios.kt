package mdrew.ballwall.preferences

class DefaultUserPreferences : UserPreferences {
    override fun <T> get(key: UserPreferences.Key, defaultValue: T): T {
        TODO("Not yet implemented")
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