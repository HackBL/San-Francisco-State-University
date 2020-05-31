import UIKit

class TermsViewController: UIViewController {
    @IBOutlet weak var termsText: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        guard let path = Bundle.main.path(forResource: "Terms", ofType: "txt") else {return}
        var readFile: String = ""
        
        do {
            readFile = try String(contentsOfFile: path, encoding: String.Encoding.utf8)
        } catch let error as NSError {
            print("Failed to read from terms.txt")
            print(error)
        }
        
        termsText.text = readFile
    }
}
