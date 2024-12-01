package mdrew.ballwall.route

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import ballwall.composeapp.generated.resources.Res
import ballwall.composeapp.generated.resources.logincomponent_loginbutton_label
import mdrew.ballwall.component.SplashComponent
import org.jetbrains.compose.resources.stringResource

@Composable
fun SplashRoute(component: SplashComponent, modifier: Modifier = Modifier) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        component.onStart()
    }
    Column(modifier = modifier.fillMaxSize()) {
        Text(stringResource(Res.string.logincomponent_loginbutton_label))
    }
}