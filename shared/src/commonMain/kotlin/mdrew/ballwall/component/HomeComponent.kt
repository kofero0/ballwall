package mdrew.ballwall.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface HomeComponent :
    ActionComponent<HomeComponent.Action, HomeComponent.State, HomeComponent.HomeError> {
    sealed interface Action : ActionComponent.Action

    interface HomeError : Component.ComponentError

    data class State(override val loading: Boolean = false, override val error: HomeError? = null) :
        Component.UIState<HomeError>

    fun interface Builder {
        fun build(componentContext: ComponentContext): HomeComponent
    }
}

class HomeComponentImpl(componentContext: ComponentContext) : HomeComponent,
    ComponentContext by componentContext {
    override fun onAction(action: HomeComponent.Action) {}

    override fun onStart() {}

    private val _state = MutableValue(HomeComponent.State())
    override val state: Value<HomeComponent.State> = _state
}