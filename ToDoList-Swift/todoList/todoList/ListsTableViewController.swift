import UIKit

class ListsTableViewController: UITableViewController {
    
    var task = ListModel()  // create object of listModel

    let defaults = UserDefaults.standard

    // return the number of lists
    override func tableView( _ tableView: UITableView, numberOfRowsInSection section: Int ) -> Int {
        //return tasks.count
        return task.tasksValue.count
    }
    
    // create new task
    @IBAction func addListButtonPressed( _ sender: Any ) {
        let alert = UIAlertController( title: "New task",message: "Enter contents", preferredStyle: UIAlertControllerStyle.alert )
        
        // user input field
        alert.addTextField { ( textField: UITextField ) in
            textField.placeholder = "Text here"
        }
        
        // click OK to save task into array
        let ok: UIAlertAction = UIAlertAction( title: "OK",style: UIAlertActionStyle.default ) { ( action: UIAlertAction ) in
            if let alertTextField = alert.textFields?.first {
                if alertTextField.text != "" {
                    self.task.addTask( name: "\(alertTextField.text!)", completed: false )
                    
                    self.task.save()
                    
                    self.tableView.reloadData()
                }
            }
        }
        
        // click Cancel to terminate current action
        let cancel = UIAlertAction( title: "Cancel",  style: UIAlertActionStyle.cancel, handler: nil )
        
        // call alert actions
        alert.addAction( ok )
        alert.addAction( cancel )
        
        self.present( alert, animated: true, completion: nil )
    }
    
    
    override func tableView( _ tableView: UITableView, cellForRowAt indexPath: IndexPath ) -> UITableViewCell {
        let curTask = task.tasksValue[indexPath.row]
        
        let cell = tableView.dequeueReusableCell( withIdentifier: "listsCell" )!
        
        cell.textLabel?.text = curTask.name
        
        // the task is completed as long as user clicks the task
        if curTask.completed {
            cell.accessoryType = UITableViewCellAccessoryType.checkmark
        } else {
            cell.accessoryType = UITableViewCellAccessoryType.none
        }
        
        return cell
    }
    
    
    override func tableView( _ tableView: UITableView, canEditRowAt indexPath: IndexPath ) -> Bool {
        return true
    }
    
    
    // edit and delete task
    override func tableView( _ tableView: UITableView, editActionsForRowAt indexPath: IndexPath ) -> [UITableViewRowAction]? {
        
        // edit the selected task
        let editAction = UITableViewRowAction( style: .normal, title: "Edit", handler: { ( action, indexPath ) in
            
            let curTest = self.task.tasksValue[indexPath.row].name
            
            let alert = UIAlertController( title: "", message: "Edit task", preferredStyle: .alert )
            
            alert.addTextField( configurationHandler: { ( textField ) in
                textField.text = self.task.tasksValue[indexPath.row].name
            } )
            
            // click Save to edit the selected task successfully
            alert.addAction( UIAlertAction( title: "Save", style: .default, handler: { ( updateAction ) in
                
                // do nothing if user edit nothing or input empty
                if alert.textFields!.first!.text! != "", alert.textFields!.first!.text! != curTest {
                   
                    self.task.tasksValue[indexPath.row].completed = false // it will be marked as incompleted task as long as user edit the task
                    
                    self.task.tasksValue[indexPath.row].name = alert.textFields!.first!.text!
                    
                    self.task.save()

                    self.tableView.reloadData()
                } 
            } ) )
            
            // click Cancel to terminate current action
            alert.addAction( UIAlertAction( title: "Cancel", style: .cancel, handler: nil ) )
            
            self.present( alert, animated: false )
        })
        
        
        // delete the selected task
        let deleteAction = UITableViewRowAction( style: .default, title: "Delete", handler: { ( action, indexPath ) in
            self.task.tasksValue.remove(at: indexPath.row)
            
            self.task.save()
            
            tableView.reloadData()
        } )
        
        return [deleteAction, editAction]
    }
    
    
    // user could mark task as complete, as well as unmark it
    override func tableView( _ tableView: UITableView, didSelectRowAt indexPath: IndexPath ) {
        if !self.task.tasksValue[indexPath.row].completed {
            self.task.tasksValue[indexPath.row].completed = true
        } else {
            self.task.tasksValue[indexPath.row].completed = false
        }
        
        self.task.save()
        
        tableView.reloadRows( at: [indexPath], with: UITableViewRowAnimation.none )
    }

    override func viewDidAppear( _ animated: Bool ) {
        task.load()
        tableView.reloadData()
    }
}
