import Foundation
import Firebase

struct Users {  // Get user cur status via Firebase
    
    let uid: String
    let email: String
    
    init(authData: User) {
        uid = authData.uid
        email = authData.email!
    }
    
    init(uid: String, email: String) {
        self.uid = uid
        self.email = email
    }
    
    var getUid: String {
        get {
            return uid
        }
    }
    
    var getEmail: String {
        get {
            return email
        }
    }
}
