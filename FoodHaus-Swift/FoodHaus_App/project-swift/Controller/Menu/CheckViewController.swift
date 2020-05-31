import UIKit
import Firebase

class CustomCell: UITableViewCell {
    
}

class CheckViewController: UIViewController {
    
    var user: Users!
    
    // Define references to database
    let ref = Database.database().reference()
    
    @IBOutlet weak var subTotalPrice: UILabel!
    @IBOutlet weak var tax: UILabel!
    @IBOutlet weak var totalPrice: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    let items = UserDefaults.standard.stringArray(forKey: "Name") ?? [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Retrieve cur user data
        Auth.auth().addStateDidChangeListener { auth, user in
            guard let user = user else { return }
            self.user = Users(authData: user)
        }
        
        tableView.register(CustomCell.self, forCellReuseIdentifier: "OurCustomCell")
        tableView.delegate = self
        tableView.dataSource = self
        
        // Subtotal
        let subTotalPriceText = subTotalPrice.text!
        let subTotal:String = String(format: "%.2f", UserDefaults.standard.double(forKey: "total"))
        subTotalPrice.text = subTotalPriceText + subTotal
        
        // Tax
        if let taxText = subTotalPrice.text {
            let numberFormatter = NumberFormatter()
            let totalPrice = numberFormatter.number(from: taxText)?.doubleValue
            let taxAmt:Double = totalPrice! * 0.085
            tax.text = String(format: "%.2f", taxAmt)
        }
        
        // Total
        if let totalPriceText = subTotalPrice.text {
            let numberFormatter = NumberFormatter()
            let total = numberFormatter.number(from: totalPriceText)?.doubleValue
            let afterTax:Double = total! * 1.085
            totalPrice.text = String(format: "%.2f", afterTax)
        }
    }
    
    @IBAction func confirmButon(_ sender: Any) {
        let userID = Auth.auth().currentUser?.uid
        
        // Check whether user fill in their info of profile
        ref.child("users").child(userID!).child("profile").observeSingleEvent(of: .value, with: { (snapshot) in
            
            let value = snapshot.value as? NSDictionary
            
            // User could check out if profile has enough info
            if (value?["name"] as? String ?? "" != ""  &&
                value?["address"] as? String ?? "" != "" &&
                value?["phone"] as? String ?? "" != "") {
                
                // Check whether user selected any items
                if self.subTotalPrice.text == "0.00" {
                    let alertController = UIAlertController(title: "Oops!", message: "Please order something!", preferredStyle: .alert)
                    
                    let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                    alertController.addAction(defaultAction)
                    
                    //
                    self.present(alertController, animated: true, completion: nil)
                    
                } else {
                    // Store ordered food into database
                    self.ref.child("users").child(self.user.getUid).child("menu").setValue(["items": self.items,
                                                                                            "price": self.totalPrice.text as Any])
                    // Reset price & item when user click confirm
                    self.resetPrice()
                    self.resetItem()
                    UserDefaults.standard.set(true, forKey: "init")
                    
                    self.performSegue(withIdentifier: "Successful", sender: self)
                }
            } else {    // User has not completed in profile
                let alertController = UIAlertController(title: "Oops!", message: "Please complete Profile, we coule contact to you. Go to Profile -> Edit.", preferredStyle: .alert)
                
                let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                alertController.addAction(defaultAction)
                
                // Move forward to Confirm view controller
                self.present(alertController, animated: true, completion: nil)
            }
        }) { (error) in
            print(error.localizedDescription)
        }
    }
    
    @IBAction func cancelButton(_ sender: Any) {
        // Reset price & item when user click confirm
        self.resetPrice()
        self.resetItem()
        UserDefaults.standard.set(true, forKey: "init")
        // Go back to root view controller
        self.navigationController?.popToRootViewController(animated: true)
    }
    
    // Reset data of subtotal
    func resetPrice() {
        let defaults = UserDefaults.standard
        let dictionary = defaults.dictionaryRepresentation()
        dictionary.keys.forEach { key in
            defaults.removeObject(forKey: "total")
        }
    }
    
    // Reset data of item
    func resetItem() {
        let defaults = UserDefaults.standard
        let dictionary = defaults.dictionaryRepresentation()
        dictionary.keys.forEach { key in
            defaults.removeObject(forKey: "Name")
        }
    }
}

// Display items into table view
extension CheckViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "OurCustomCell") ?? UITableViewCell()
        cell.textLabel?.text = items[indexPath.row]
        return cell
    }
}
