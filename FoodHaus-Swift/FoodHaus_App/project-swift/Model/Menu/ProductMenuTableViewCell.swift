import UIKit

fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l < r
    case (nil, _?):
        return true
    default:
        return false
    }
}

fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l > r
    default:
        return rhs < lhs
    }
}

let screenHeight = UIScreen.main.bounds.height
let screenWidth  = UIScreen.main.bounds.width

// delete and add button use one common closure, and then send to the model object
class PrdouctMenuTableViewCell: UITableViewCell,CAAnimationDelegate {
    var productName:UILabel!
    var productPrice: UILabel!  // Price
    var minusBtn:UIButton!
    var plusBtn:UIButton!
    var buyCount:UILabel!
    var separateLine:UIView?
    var productNameStr:String?
    var productPriceStr: String?
    var items:[String] = []
    
    // claim closure
    var addProClosure:((UITableViewCell,Bool)->())?
    
    override init(style: UITableViewCellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        // Product Name
        self.productName = UILabel(frame:CGRect(x: 15,y: 15,width: (screenWidth*0.7) - 30,height: 20))
        self.productName.font = UIFont.systemFont(ofSize: 15)
        self.productName.textColor = UIColor.black
        self.productName.textAlignment = NSTextAlignment.left
        self.contentView.addSubview(self.productName)
        
        // Product Price
        if(screenWidth < 350) { // if it's iPhone 5s or SE, the price needs to be closer to the left
            self.productPrice = UILabel(frame:CGRect(x: 170,y: 15,width: (screenWidth*0.7) - 30,height: 20))
        }
        else {
            self.productPrice = UILabel(frame:CGRect(x: 200,y: 15,width: (screenWidth*0.7) - 30,height: 20))
        }
        self.productPrice.font = UIFont.systemFont(ofSize: 15)
        self.productPrice.textColor = UIColor.black
        self.productPrice.textAlignment = NSTextAlignment.left
        self.contentView.addSubview(self.productPrice)
        
        self.plusBtn = UIButton(type: UIButtonType.custom)
        self.plusBtn.frame =  CGRect(x: (screenWidth*0.7) - 59,y: 36,width: 44,height: 44)
        self.plusBtn.setImage(UIImage(named: "btn_increase"), for: UIControlState())
        self.plusBtn.addTarget(self, action: #selector(PrdouctMenuTableViewCell.plusBtnClick(_:)), for: UIControlEvents.touchUpInside)
        self.contentView.addSubview(self.plusBtn)
        
        self.separateLine = UIView(frame:CGRect(x: 0,y: 85-0.5,width: screenWidth*0.7,height: 0.5))
        self.separateLine?.backgroundColor = UIColor.gray
        self.contentView.addSubview(self.separateLine!)
        
        
        self.minusBtn = UIButton(type: UIButtonType.custom)
        self.minusBtn.frame =  CGRect(x: (screenWidth*0.7) - 59,y: 36,width: 44,height: 44)
        self.minusBtn.setImage(UIImage(named: "btn_decrease"), for: UIControlState())
        self.minusBtn.addTarget(self, action: #selector(PrdouctMenuTableViewCell.minusBtnClick(_:)), for: UIControlEvents.touchUpInside)
        self.contentView.addSubview(self.minusBtn)
        
        self.buyCount = UILabel(frame: CGRect(x: (screenWidth*0.7) - 59,y: 36,width: 44,height: 44))
        self.buyCount.font = UIFont.systemFont(ofSize: 13)
        self.buyCount.textColor = UIColor.black
        self.buyCount.textAlignment = NSTextAlignment.center
        self.contentView.addSubview(self.buyCount)
        
        self.contentView.bringSubview(toFront: self.plusBtn)
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        self.productName.text = self.productNameStr
        self.productPrice.text = self.productPriceStr
    }
    
    // plus button click
    @objc func plusBtnClick(_ btn:UIButton){
        let point:CGPoint = self.convert(btn.frame.origin, to: (UIApplication.shared.delegate?.window)!)
        let circleView:UIView = UIView(frame:CGRect(x: point.x,y: point.y,width: 20,height: 20))
        circleView.layer.cornerRadius = btn.frame.width / 2
        circleView.backgroundColor = UIColor.blue
        let window:UIWindow! = (UIApplication.shared.delegate?.window)!
        window.addSubview(circleView)
        let keyframeAnimation:CAKeyframeAnimation = CAKeyframeAnimation(keyPath: "position")
        let path:CGMutablePath = CGMutablePath()
        
        // animation begin position
        path.move(to: CGPoint(x:circleView.layer.position.x, y:circleView.layer.position.y))
        // animation to: end position
        // control1: a position between begin and end
        // control2: a second position between begin and end
        path.addCurve(to: CGPoint(x:30, y:screenHeight-30), control1: CGPoint(x: circleView.layer.position.x-150, y: circleView.layer.position.y-30), control2: CGPoint(x: circleView.layer.position.x-200, y:  circleView.layer.position.y+40))
        keyframeAnimation.path = path
        keyframeAnimation.delegate = self
        keyframeAnimation.duration = 0.5
        circleView.layer.add(keyframeAnimation, forKey: "position");
        
        // once the click is done,remove the view
        circleView.perform(#selector(UIView.removeFromSuperview), with: nil, afterDelay: 0.45)
        
        // a comparison to the stock according to the number of clicks
        if self.buyCount.text == nil
        {
            UIView.animate(withDuration: 0.5, animations: { () -> Void in
                self.minusBtn.frame.origin.x = (screenWidth*0.7) - 157
                self.buyCount.frame.origin.x = (screenWidth*0.7) - 108
                self.minusBtn.alpha = 1.0
                self.buyCount.text = "1"
            })
            
            // add first price
            if let price:Double = Double(productPrice.text!)
            {
                var curTotal = UserDefaults.standard.double(forKey: "total")
                curTotal = curTotal + price
                UserDefaults.standard.set(curTotal, forKey: "total")
            }
            
            // add first item
            let name: String = String(productName.text!)
            if let items = UserDefaults.standard.array(forKey: "Name") as? [String] {
                self.items = items
            }
            items.append(name)
            items.sort()
            UserDefaults.standard.set(items, forKey: "Name")
        }
        else
        {
            self.buyCount.text = String(Int(self.buyCount.text!)! + 1)
            
            // add price
            if let price:Double = Double(productPrice.text!)
            {
                var curTotal = UserDefaults.standard.double(forKey: "total")
                curTotal = curTotal + price
                UserDefaults.standard.set(curTotal, forKey: "total")
            }
            
            // add item
            let name: String = String(productName.text!)
            items = UserDefaults.standard.array(forKey: "Name") as! [String]
            items.append(name)
            items.sort()
            UserDefaults.standard.set(items, forKey: "Name")
        }
        
        if addProClosure != nil
        {
            addProClosure!(self, true)
        }
    }
    
    // minus button click
    @objc func minusBtnClick(_ btn:UIButton)
    {
        
        if Int(self.buyCount.text!) > 1
        {
            self.buyCount.text = String(Int(self.buyCount.text!)! - 1)
            
            // remove price
            if let price:Double = Double(productPrice.text!)
            {
                var curTotal = UserDefaults.standard.double(forKey: "total")
                curTotal = curTotal - price
                UserDefaults.standard.set(curTotal, forKey: "total")
            }
            
            // remove item
            let name: String = String(productName.text!)
            items = UserDefaults.standard.array(forKey: "Name") as! [String]
            let index = items.index { (elementName) -> Bool in
                elementName == name
            }
            items.remove(at: index!)
            UserDefaults.standard.set(items, forKey: "Name")            
        }
        else
        {
            UIView.animate(withDuration: 0.5, animations: { () -> Void in
                self.minusBtn.frame.origin.x = (screenWidth*0.7) - 59
                self.buyCount.frame.origin.x = (screenWidth*0.7) - 59
                self.minusBtn.alpha = 0.0
                self.buyCount.text = nil
            })
            
            // remove last price
            if let price:Double = Double(productPrice.text!)
            {
                var curTotal = UserDefaults.standard.double(forKey: "total")
                curTotal = curTotal - price
                UserDefaults.standard.set(curTotal, forKey: "total")
            }
            
            // remove last item
            let name: String = String(productName.text!)
            items = UserDefaults.standard.array(forKey: "Name") as! [String]
            
            let index = items.index { (elementName) -> Bool in
                elementName == name
            }
            items.remove(at: index!)
            UserDefaults.standard.set(items, forKey: "Name")
        }
        if addProClosure != nil
        {
            addProClosure!(self, false)
        }
    }
}
