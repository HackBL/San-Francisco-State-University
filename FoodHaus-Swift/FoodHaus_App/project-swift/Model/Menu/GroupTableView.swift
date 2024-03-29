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

class GroupTableView: UIView,UITableViewDelegate,UITableViewDataSource {
    var groupTableView:UITableView!
    var classifyTableView:UITableView!
    var productTypeArr:[String] = []
    var productNameArr:[AnyObject] = []
    var productPriceArr:[AnyObject] = []   // Price
    var currentExtendSection:Int = 0
    var isScrollSetSelect = false
    var isScrollClassiftyTable = false
    
    override init(frame:CGRect) {
        super.init(frame: frame)
        self.initData()
        self.groupTableView = UITableView(frame: CGRect(x: frame.width*0.3, y: 0, width: frame.width*0.7, height: frame.height), style: UITableViewStyle.plain)
        self.groupTableView.delegate = self
        self.groupTableView.dataSource = self
        self.groupTableView.separatorStyle = UITableViewCellSeparatorStyle.none
        self.groupTableView.backgroundColor = UIColor.brown
        self.groupTableView.tableHeaderView?.backgroundColor = UIColor(red: 230, green: 230, blue: 230, alpha: 1)
        self.addSubview(self.groupTableView)
        
        self.classifyTableView = UITableView(frame:CGRect(x: 0, y: 0, width: frame.width*0.3, height: frame.height),style:UITableViewStyle.plain)
        self.classifyTableView.delegate = self;
        self.classifyTableView.dataSource = self;
        self.classifyTableView.tableFooterView = UIView()
        self.addSubview(self.classifyTableView)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func  initData()
    {
        // Product Name
        let path:String = (Bundle.main.path(forResource: "MenuData", ofType: "json"))!
        let data:Data = try! Data(contentsOf: URL(fileURLWithPath: path))
        let json:AnyObject = try!JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as AnyObject
        let resultDict = json.object(forKey: "data") as! Dictionary<String,AnyObject>
        let productMenuArr:[AnyObject] = resultDict["productType"] as! Array
        for i:Int in 0 ..< productMenuArr.count
        {
            productTypeArr.append(productMenuArr[i]["typeName"] as! String)
            productNameArr.append(productMenuArr[i]["productName"] as! [String] as AnyObject)
        }
        
        // Product Price
        let pricePath:String = (Bundle.main.path(forResource: "MenuPrice", ofType: "json"))!
        let pricePata:Data = try! Data(contentsOf: URL(fileURLWithPath: pricePath))
        let priceJson:AnyObject = try!JSONSerialization.jsonObject(with: pricePata, options: JSONSerialization.ReadingOptions.allowFragments) as AnyObject
        let priceResultDict = priceJson.object(forKey: "data") as! Dictionary<String,AnyObject>
        let priceProductMenuArr:[AnyObject] = priceResultDict["productType"] as! Array
        for i:Int in 0 ..< priceProductMenuArr.count
        {
            productPriceArr.append(priceProductMenuArr[i]["productPrice"] as! [String] as AnyObject)
        }
    }
    
    //MARK:UITableViewDataSource
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if tableView == self.classifyTableView
        {
            return productTypeArr.count
        }
        else
        {
            return productNameArr[section].count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let classifyCell:UITableViewCell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "classifyCell")
        let extendCell:PrdouctMenuTableViewCell = PrdouctMenuTableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "extendCell")
        extendCell.backgroundColor = UIColor(red: 238, green: 238, blue: 238, alpha: 1);
        if tableView == self.classifyTableView
        {
            classifyCell.textLabel?.text = self.productTypeArr[indexPath.row]
            classifyCell.textLabel?.numberOfLines = 0
            classifyCell.backgroundColor = UIColor(red: 238, green: 238, blue: 238, alpha: 1)
            classifyCell.selectionStyle = UITableViewCellSelectionStyle.none
            // The first option by default
            if indexPath.row == currentExtendSection
            {
                let tempView:UIView = UIView(frame:CGRect(x: 0,y: 0,width: 5,height: 55))
                tempView.tag = 101
                tempView.backgroundColor = UIColor.red
                classifyCell.addSubview(tempView)
            }
            return classifyCell
        }
        else
        {
            if productNameArr[indexPath.section].count > indexPath.row
            {
                extendCell.productNameStr = (productNameArr[indexPath.section] as! Array)[indexPath.row]
                extendCell.productPriceStr = (productPriceArr[indexPath.section] as! Array)[indexPath.row]
                
            }
            extendCell.addProClosure = {(cell:UITableViewCell,isAddProduct:Bool) in
                let indexPath:IndexPath = (self.groupTableView.indexPath(for: cell))!
                let cell:UITableViewCell = self.classifyTableView.cellForRow(at: IndexPath(row: indexPath.section, section: 0))!
                var buyCountLab:UILabel? = cell.viewWithTag(200) as? UILabel
                if buyCountLab == nil
                {
                    buyCountLab = UILabel(frame:CGRect(x: cell.frame.width-20,y: 5,width: 15,height: 15))
                    buyCountLab?.backgroundColor = UIColor.red
                    buyCountLab?.textColor = UIColor.white
                    buyCountLab?.textAlignment = NSTextAlignment.center
                    buyCountLab?.font = UIFont.systemFont(ofSize: 10)
                    buyCountLab?.tag = 200
                    buyCountLab?.text = "1"
                    cell.addSubview(buyCountLab!)
                }
                else
                {
                    if isAddProduct == true
                    {
                        buyCountLab?.text = String(Int((buyCountLab?.text)!)!+1)
                    }
                    else
                    {
                        if Int((buyCountLab?.text)!) > 1
                        {
                            buyCountLab?.text = String(Int((buyCountLab?.text)!)!-1)
                        }
                        else
                        {
                            buyCountLab?.removeFromSuperview()
                        }
                    }
                }
                print(indexPath.section)
            }
            return extendCell
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int
    {
        if tableView == self.groupTableView
        {
            return productTypeArr.count
        }
        return 1
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section:
        Int) -> String?
    {
        if tableView == groupTableView
        {
            return productTypeArr[section]
        }
        return ""
    }
    
    //MARK:UITableViewDelegate
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if tableView == self.classifyTableView
        {
            return 55
        }
        else
        {
            return 85
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        if tableView == groupTableView
        {
            return 30
        }
        return 0.1
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if tableView == self.classifyTableView
        {
            tableView.deselectRow(at: indexPath, animated: true)
            self.leftSectionSelected(indexPath, withTableView: tableView, didSelectClassifyTable: true)
        }
    }
    
    func leftSectionSelected(_ indexPath:IndexPath, withTableView tableView:UITableView,didSelectClassifyTable:Bool)
    {
        if tableView == self.classifyTableView
        {
            if indexPath.row == currentExtendSection
            {
                // if the current option is the same as last one, just return.
                return ;
            }
            
            // select a new choice
            let newCell:UITableViewCell? = tableView.cellForRow(at: indexPath)
            let tempView:UIView = UIView(frame:CGRect(x: 0,y: 0,width: 5,height: 55))
            tempView.tag = 101
            tempView.backgroundColor = UIColor.red
            newCell?.addSubview(tempView)
            
            // cancel the last choice
            let oldIndexPath:IndexPath = IndexPath(row: currentExtendSection, section: 0)
            let oldCell:UITableViewCell? = tableView.cellForRow(at: oldIndexPath)
            let view:UIView? = oldCell?.viewWithTag(101)
            view? .removeFromSuperview()
            
            self.currentExtendSection = indexPath.row
            
            // freeze the selected category on the top
            self.groupTableView.scrollToRow(at: IndexPath(row: 0, section:self.currentExtendSection), at: UITableViewScrollPosition.top, animated: true)
            isScrollClassiftyTable = didSelectClassifyTable
            
            let cellR:CGRect = self.classifyTableView.rectForRow(at: indexPath)
            if self.classifyTableView.contentOffset.y - cellR.origin.y > 54
            {
                // move up the tableview (the tableview is on the left) to make the selected cell visible
                self.classifyTableView.contentOffset.y = CGFloat(55 * indexPath.row)
            }
            
            // move down the tableview (the tableview is on the left) to make the selected cell visible
            if cellR.origin.y - self.classifyTableView.frame.size.height > 0 {
                
                self.classifyTableView.contentOffset.y = cellR.origin.y - self.classifyTableView.frame.size.height+55
            }
        }
    }
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView)
    {
        // fix iOS 11.0/iPhone X cannot scroll menu issue
        if #available(iOS 11.0, *) {
            scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentBehavior.never
        }
        isScrollSetSelect = false
        if scrollView == self.groupTableView
        {
            isScrollSetSelect = true
            isScrollClassiftyTable = false
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView)
    {
        if scrollView == self.groupTableView && isScrollSetSelect && isScrollClassiftyTable == false
        {
            // scroll the tableview on the right
            let indexPathArr:[IndexPath]? =  self.groupTableView.indexPathsForVisibleRows
            self.leftSectionSelected(IndexPath(row:indexPathArr![0].section, section: 0), withTableView: self.classifyTableView, didSelectClassifyTable: false)
        }
    }
}
