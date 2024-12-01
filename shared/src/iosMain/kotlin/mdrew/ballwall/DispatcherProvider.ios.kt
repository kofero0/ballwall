package mdrew.ballwall

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object DispatcherProvider {
    actual val http: CoroutineDispatcher
        get() = TODO("Not yet implemented")
    actual val main: CoroutineDispatcher
        get() = TODO("Not yet implemented")
    actual val io: CoroutineDispatcher
        get() = TODO("Not yet implemented")
}