package mdrew.ballwall.route

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import ballwall.composeapp.generated.resources.Res
import ballwall.composeapp.generated.resources.logincomponent_guestbutton_label
import ballwall.composeapp.generated.resources.logincomponent_loginbutton_label
import ballwall.composeapp.generated.resources.logincomponent_signupbutton_label
import mdrew.ballwall.component.LoginComponent
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginRoute(component: LoginComponent, modifier: Modifier = Modifier) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        component.onStart()
    }
    Column(modifier = modifier.fillMaxSize()){
        Button({component.onAction(LoginComponent.Action.Login)}) {
            Text(stringResource(Res.string.logincomponent_loginbutton_label))
        }
        Button({component.onAction(LoginComponent.Action.Register)}) {
            Text(stringResource(Res.string.logincomponent_signupbutton_label))
        }
        Button({component.onAction(LoginComponent.Action.Guest)}) {
            Text(stringResource(Res.string.logincomponent_guestbutton_label))
        }
    }
}