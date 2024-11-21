package mdrew.ballwall.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import mdrew.ballwall.component.LoginComponent

@Composable
fun LoginRoute(component: LoginComponent, modifier: Modifier = Modifier) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        component.onStart()
    }
}