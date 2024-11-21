package mdrew.ballwall.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import mdrew.ballwall.component.RootComponent

@Composable
fun RootRoute(component: RootComponent, modifier: Modifier = Modifier) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        component.onStart()
    }
    Child(
        component = component,
        modifier = modifier
    )
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun Child(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(fade() + scale()),
            onBack = component::onBackClicked
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeRoute(component = child.component)
                is RootComponent.Child.Login -> LoginRoute(component = child.component)
                is RootComponent.Child.Splash -> SplashRoute(component = child.component)
            }
        }
    }
}