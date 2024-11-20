package mdrew.ballwall.di

import com.arkivanov.decompose.ComponentContext
import mdrew.ballwall.component.HomeComponent
import mdrew.ballwall.component.HomeComponentImpl
import mdrew.ballwall.component.LoginComponent
import mdrew.ballwall.component.LoginComponentImpl
import mdrew.ballwall.component.RootComponent
import mdrew.ballwall.component.RootComponentImpl
import mdrew.ballwall.component.SplashComponent
import mdrew.ballwall.component.SplashComponentImpl

object DefaultObjectGraph {
    fun rootComponent(
        componentContext: ComponentContext
    ) : RootComponent = RootComponentImpl(
        componentContext = componentContext,
        splashBuilder = splashBuilder(),
        homeBuilder = homeBuilder(),
        loginBuilder = loginBuilder()
    )

    private fun loginBuilder() = LoginComponent.Builder { LoginComponentImpl(componentContext = it) }

    private fun homeBuilder() = HomeComponent.Builder { HomeComponentImpl(componentContext = it) }

    private fun splashBuilder() = SplashComponent.Builder { SplashComponentImpl(componentContext = it) }
}