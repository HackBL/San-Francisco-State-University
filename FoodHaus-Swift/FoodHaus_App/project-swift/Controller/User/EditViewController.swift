import UIKit
import MobileCoreServices
import FirebaseDatabase
import FirebaseAuth
import Firebase

class EditViewController: UIViewController, UITextFieldDelegate {
    var user: Users!
    
    // Define references to database
    let ref = Database.database().reference()
    
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var addressTextField: UITextField!
    @IBOutlet weak var phoneTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        
        // Retrieve cur user data
        Auth.auth().addStateDidChangeListener { auth, user in
            guard let user = user else { return }
            self.user = Users(authData: user)
        }
    }
    
    // Keyboard shows
    func textFieldDidBeginEditing(_ textField: UITextField) {
        moveTextFIeld(textField: textField, moveDistance: -100, up: true)
    }
    
    // Keyboard hidden
    func textFieldDidEndEditing(_ textField: UITextField) {
        moveTextFIeld(textField: textField, moveDistance: -100, up: false)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func moveTextFIeld(textField: UITextField, moveDistance: Int, up: Bool) {
        let moveDuration = 0.3
        let movement: CGFloat = CGFloat(up ? moveDistance : -moveDistance)
        
        UIView.beginAnimations("animateTextField", context: nil)
        UIView.setAnimationBeginsFromCurrentState(true)
        UIView.setAnimationDuration(moveDuration)
        self.view.frame = self.view.frame.offsetBy(dx: 0, dy: movement)
        UIView.commitAnimations()
    }
    
    @IBAction func saveButton(_ sender: Any) {
        // Save data into Database as json
        let email = self.user.getEmail
        guard
            let name = nameTextField.text,
            let phone = phoneTextField.text,
            let address = addressTextField.text
            else {
                return
        }
        
        // User has to fill in all info in their profile
        if (name != "" && address != "" && phone != "") {
            self.ref.child("users").child(user.getUid).child("profile").setValue(["email": email,
                                                                 "name": name,
                                                                 "phone": phone,
                                                                 "address": address])
            
            _ = navigationController?.popViewController(animated: true)
            
        } else {
            // Tells the user that they need to fill in all info
            let alertController = UIAlertController(title: "Oops", message: "Please fill in all information so that we could contact and delivery to you", preferredStyle: .alert)
            
            let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
            alertController.addAction(defaultAction)
            
            self.present(alertController, animated: true, completion: nil)
        }
    }
}
