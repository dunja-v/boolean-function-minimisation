package Parsing.AlgebarExpressionParsing.Expressions;

public class NotExpression extends OperandExpression {

    public NotExpression(Expression firstOperand,
            Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int execute() {
        return (firstOperand.execute() + 1) % 2;
    }

}
