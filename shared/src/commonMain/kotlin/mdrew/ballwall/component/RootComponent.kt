package mdrew.ballwall.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable

interface RootComponent : Component<RootComponent.State, RootComponent.RootError>,
    BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()
    fun onBackClicked(toIndex: Int)

    sealed interface RootError : Component.ComponentError

    data class State(
        override val loading: Boolean = false, override val error: RootError? = null
    ) : Component.UIState<RootError>

    sealed class Child {
        class Splash(val component: SplashComponent) : Child()
        class Login(val component: LoginComponent) : Child()
        class Home(val component: HomeComponent) : Child()
    }
}

class RootComponentImpl(
    componentContext: ComponentContext,
    private val splashBuilder: SplashComponent.Builder,
    private val homeBuilder: HomeComponent.Builder,
    private val loginBuilder: LoginComponent.Builder
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Splash,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Splash -> RootComponent.Child.Splash(splashBuilder.build(componentContext))
            Config.Home -> RootComponent.Child.Home(homeBuilder.build(componentContext))
            Config.Login -> RootComponent.Child.Login(loginBuilder.build(componentContext))
        }

    override fun onBackClicked() {
        navigation.pop()
    }

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(toIndex)
    }

    override fun onStart() {

    }

    private val _state = MutableValue(RootComponent.State())
    override val state: Value<RootComponent.State> = _state

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Splash : Config

        @Serializable
        data object Login : Config

        @Serializable
        data object Home : Config
    }
}