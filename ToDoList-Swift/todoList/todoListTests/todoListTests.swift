//
//  todoListTests.swift
//  todoListTests
//
//  Created by Bo Li on 3/5/18.
//  Copyright © 2018 Bo Li. All rights reserved.
//

import XCTest
@testable import todoList

var tester: ListModel!


class todoListTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
        tester = ListModel()

    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
        tester = nil
    }
    
    func testExample() {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
        
        tester.addTask( name: "Play Basketball", completed: false )
        tester.addTask ( name: "Swimming", completed: true)
        tester.addTask( name: "Reading books", completed: false )
        
        XCTAssertEqual (tester.tasksValue[0].name, "Play Basketball" )
        XCTAssertFalse( tester.tasksValue[0].completed )
        
        XCTAssertEqual( tester.tasksValue[1].name, "Swimming" )
        XCTAssertTrue( tester.tasksValue[1].completed )
        
        XCTAssertEqual( tester.tasksValue[2].name, "Reading books" )
        XCTAssertFalse( tester.tasksValue[2].completed )
    }
    
    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }
    
}
