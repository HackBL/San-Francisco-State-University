import Foundation

struct CalculatorBrain {
    
    var accumulator: Double?
    
    var binaryOperationPressed: Bool = false    // check whether binary operation is pressed
    
    var operationPressRepeat: Bool = false  // check whether binary operation is pressed multiple times before the second operand is input
    
    private var currentPendingBinaryOperation: PendingBinaryOperation?
    
    enum Operation {
        case constant(Double)
        case unaryOperation( (Double) -> Double )
        case binareOperation( (Double,Double) -> Double )
        case equals
        case clears
    }
    
    var operations: [String: Operation] = [
        "pi": Operation.constant(Double.pi),
        "e": Operation.constant(M_E),
        "sqrt": Operation.unaryOperation(sqrt),
        "x^2": Operation.unaryOperation( {
            return $0 * $0
        }),
        "%": Operation.unaryOperation( { return $0 / 100} ),
        "+/-": Operation.unaryOperation({ return 0 - $0 }),
        "+": Operation.binareOperation(
            { $0 + $1 }
        ),
        "-": Operation.binareOperation({ $0 - $1 }),
        "x": Operation.binareOperation({ $0 * $1 }),
        "/": Operation.binareOperation({ $0 / $1 }),
        "=": Operation.equals,
        "C": Operation.clears
    ]
    
    mutating func performOperation(_ mathematicalSymbol: String) {
        if let operation = operations[mathematicalSymbol] {
            switch operation {
            case Operation.constant(let value):
                accumulator = value
            case Operation.unaryOperation(let function):
                
                if let value = accumulator {
                    accumulator = function(value)
                }
            case .binareOperation(let function):
                if !operationPressRepeat {

                    if binaryOperationPressed { // start from second operation
                        performBinaryOperation()
                    }

                    if let firstOperand = accumulator {
                        currentPendingBinaryOperation = PendingBinaryOperation(firstOperand: firstOperand, function: function)
                        accumulator = nil
                    }
                    
                    binaryOperationPressed = true   // once first operation is pressed, it's always true
                }
                operationPressRepeat = true
            case .equals:
                performBinaryOperation()
                binaryOperationPressed = false
            case .clears:
                accumulator = 0.0
                binaryOperationPressed = false
                operationPressRepeat = false
                currentPendingBinaryOperation = nil
            }
        }
    }
    
    mutating func performBinaryOperation() {
        if let operation = currentPendingBinaryOperation, let secondOperand = accumulator {
            accumulator = operation.perform(secondOperand: secondOperand)
        }
    }
    
    /**
     *   in order to avoid "1" "+" "+" by pressing continuously , and the result will get 3, we need to use the variable and function to handle it
     */
    mutating func setOperation() {
        operationPressRepeat = false
    }
    
    mutating func setOperand(_ operand: Double) {
        accumulator = operand
    }
    
    var result: Double? {
        get {
            return accumulator
        }
    }
    
    private struct PendingBinaryOperation {
        let firstOperand: Double
        let function: (Double, Double) -> Double
        
        func perform(secondOperand: Double) -> Double {
            return function(firstOperand, secondOperand)
        }
    }
}


