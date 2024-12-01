package mdrew.ballwall

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object DispatcherProvider {
    val http: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}