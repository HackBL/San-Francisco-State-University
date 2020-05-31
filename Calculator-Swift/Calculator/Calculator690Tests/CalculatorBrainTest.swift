//
//  CalculatorBrainTest.swift
//  Calculator690Tests
//
//  Created by Bo Li on 2/24/18.
//  Copyright © 2018 Bo Li. All rights reserved.
//

import XCTest
@testable import Calculator690

var tester: CalculatorBrain!

class CalculatorBrainTest: XCTestCase {
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
        tester = CalculatorBrain()
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
        tester = nil
    }
    
    /*
     *  All operations use same way, and we could test
     *  equal operation directly when computing operation works,
     *  but at the end, we need to use ACTAssertEqual
     *  to test whether the result same as the one we expect
     */
    
    /* equal operation with four calculating operations */
    
    func testAdditionOperation() {  // addition operation testing
        tester.setOperand( 10.0 )
        tester.performOperation( "+" )
        tester.setOperand( 5.0 )
        tester.performOperation( "=" )
        XCTAssertEqual( tester.result, 15.0 )
    }
    
    func testSubtractionOperation() {   // subtraction operation testing
        tester.setOperand( 10.0 )
        tester.performOperation( "-" )
        tester.setOperand( 5.0 )
        tester.performOperation( "=" )
        XCTAssertEqual( tester.result, 5.0 )
    }
    
    func testMultiplicationOperation() {    // multiplication operation testing
        tester.setOperand( 10.0 )
        tester.performOperation( "x" )
        tester.setOperand( 5.0 )
        tester.performOperation( "=" )
        XCTAssertEqual( tester.result, 50.0 )
    }
    
    func testDivisionOperation() {  // division operation testing
        tester.setOperand( 10.0 )
        tester.performOperation( "/" )
        tester.setOperand( 5.0 )
        tester.performOperation( "=" )
        XCTAssertEqual( tester.result, 2.0 )
    }
    
    func testPiOperation() {    // pi operation
        tester.performOperation( "pi" )
        XCTAssertEqual( tester.result, Double.pi )
    }
    
    func testEOperation() {    // e operation
        tester.performOperation( "e" )
        XCTAssertEqual( tester.result, M_E )
    }
    
    func testSqrtOperation() {     // sqrt operation
        tester.setOperand( 16.0 )
        tester.performOperation( "sqrt" )
        XCTAssertEqual( tester.result, 4.0 )
    }
    
    func testPowerOperation() {    // power of square
        tester.setOperand( 16.0 )
        tester.performOperation( "x^2" )
        XCTAssertEqual( tester.result, 256.0 )
    }
    
    func testPercentageOperation() {    // percentage operation
        tester.setOperand( 1.0 )
        tester.performOperation( "%" )
        XCTAssertEqual( tester.result, 0.01 )
    }
    
    func testSignOperation() {    // sign operation
        tester.setOperand(1.0)
        tester.performOperation( "+/-" )
        XCTAssertEqual( tester.result, -1.0 )
    }
    
    func testClearOperation() {    // clear operation
        tester.performOperation( "C" )
        XCTAssertEqual( tester.result, 0.0 )
    }
}
