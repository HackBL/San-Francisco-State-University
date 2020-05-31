import UIKit

struct Task {
    var name: String
    var completed: Bool
}

extension Task {
    func encode() -> Data {
        let data = NSMutableData()
        let archiver = NSKeyedArchiver( forWritingWith: data )
        archiver.encode( name, forKey: "name" )
        archiver.encode( completed, forKey: "completed" )
        archiver.finishEncoding()
        return data as Data
    }

    init?( data: Data ) {
        let unarchiver = NSKeyedUnarchiver( forReadingWith: data )
        defer {
            unarchiver.finishDecoding()
        }
        guard let name = unarchiver.decodeObject( forKey: "name" ) as? String else { return nil }
        completed = unarchiver.decodeBool( forKey: "completed" )
        
        self.name = name
    }
}


class ListModel {
    var tasks: [Task] // array to store tasks of todo list
    
    init() {
        tasks = []
    }

    func addTask( name: String, completed: Bool ) {   // store task with status of complete into array
        self.tasks.append( Task( name: name, completed: completed ) )
    }
  
    var tasksValue: [Task] { // getter and setter of array tasks
        get {
            return tasks
        }
        set {
            tasks = newValue
        }
    }

    func save() {
        let tasksData = tasks.map{ $0.encode() }
        UserDefaults.standard.set( tasksData, forKey: "tasks" )
    }

    func load() {
        guard let tasksData = UserDefaults.standard.object( forKey: "tasks" ) as? [Data] else { return }

        tasks = tasksData.flatMap{ Task( data: $0 ) }
    }
}
