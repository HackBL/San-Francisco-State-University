import UIKit
import FirebaseDatabase
import FirebaseAuth
import MessageUI

class ProfileViewController: UIViewController, MFMailComposeViewControllerDelegate {
    var user: Users!
    
    // Define references to database
    let ref = Database.database().reference()
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // Retrieve cur user data
        Auth.auth().addStateDidChangeListener { auth, user in
            guard let user = user else { return }
            self.user = Users(authData: user)
        }
        
        // Retrieve current user's info from database
        let userID = Auth.auth().currentUser?.uid
        
        ref.child("users").child(userID!).child("profile").observeSingleEvent(of: .value, with: { (snapshot) in
            // Get user value
            let value = snapshot.value as? NSDictionary
            self.nameLabel.text = value?["name"] as? String ?? ""
            self.emailLabel.text = value?["email"] as? String ?? ""
            self.addressLabel.text = value?["address"] as? String ?? ""
            self.phoneLabel.text = value?["phone"] as? String ?? ""
            
        }) { (error) in
            print(error.localizedDescription)
        }
    }
    
    @IBAction func contactAdminButton(_ sender: Any) {
        if MFMailComposeViewController.canSendMail() {
            let mail = MFMailComposeViewController()
            mail.mailComposeDelegate = self
            mail.setToRecipients(["admin@foodhaus.com"])
            mail.setMessageBody("<p>Dear customer how can I help you?</p>", isHTML: true)
            
            present(mail, animated: true)
        } else {
            _ = UIAlertController(title: "Sorry!", message: "Your device does not support this function, please send email to admin@foodhaus.com", preferredStyle: .alert)
        }
    }
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func editButton(_ sender: Any) {
        self.performSegue(withIdentifier: "ProfileToEdit", sender: self)
    }
}
