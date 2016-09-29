package Parsing.AlgebarExpressionParsing.Expressions;

public class AndExpression extends OperandExpression{

    public AndExpression(Expression firstOperand,
            Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int execute() {
        return firstOperand.execute()*secondOperand.execute();
    }
    
    

    
}
