import UIKit
import Firebase

class Counter {
    var currentNumber: Int = 5
    
    static let counterHasChanged = NSNotification.Name(rawValue: "Counter.counterHasChanged")
    
    init() {
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
            self.currentNumber -= 1
            NotificationCenter.default.post(name: Counter.counterHasChanged, object: self)
        }
    }
}

class ConfirmViewController: UIViewController {
    
    @IBOutlet weak var timerLabel: UILabel!
    
    let counter = Counter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NotificationCenter.default.addObserver(self, selector: #selector(updateView), name: Counter.counterHasChanged, object: nil)
    }
    
    @objc func updateView() {
        if counter.currentNumber > 0 {
            timerLabel.text = String(counter.currentNumber)
        } else {
            self.navigationController?.popToRootViewController(animated: true)
        }
    }
    
    @IBAction func BackToMenuButton(_ sender: Any) {   
        self.navigationController?.popToRootViewController(animated: true)
    }
}
