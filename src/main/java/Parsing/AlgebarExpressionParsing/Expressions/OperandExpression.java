package Parsing.AlgebarExpressionParsing.Expressions;

public abstract class OperandExpression implements Expression{
    
    protected Expression firstOperand;
    protected Expression secondOperand;
    
    public OperandExpression(Expression firstOperand,
            Expression secondOperand) {
        super();
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    
    
    
    public Expression getFirstOperand() {
        return firstOperand;
    }


    public Expression getSecondOperand() {
        return secondOperand;
    }


    public abstract int execute();
    
    
    
}
