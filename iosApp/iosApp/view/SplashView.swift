import shared
import SwiftUI

struct SplashView : View {
    private let component: SplashComponent
    
    @StateValue
    private var state: SplashComponentState
    
    init(_ splash: SplashComponent){
        self.component = splash
        self._state = StateValue(component.state as! Value<SplashComponentState>)
    }
    
    var body: some View {
        VStack{
            Text("logincomponent_guestbutton_label")
        }
    }
}
