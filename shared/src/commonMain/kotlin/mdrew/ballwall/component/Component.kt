package mdrew.ballwall.component

import com.arkivanov.decompose.value.Value
import mdrew.ballwall.ExpectedError

interface Component<out State : Component.UIState<Error>, out Error : Component.ComponentError> {
    fun onStart()
    val state: Value<State>

    interface ComponentError : ExpectedError

    interface UIState<out Error : ComponentError> {
        val loading: Boolean
        val error: Error?
    }
}