import shared
import SwiftUI

struct LoginView : View {
    private let component: LoginComponent
    
    @StateValue
    private var state: LoginComponentState
    
    init(_ login: LoginComponent){
        self.component = login
        self._state = StateValue(component.state as! Value<LoginComponentState>)
        
    }
    
    var body: some View {
        VStack{
        }
    }
}
