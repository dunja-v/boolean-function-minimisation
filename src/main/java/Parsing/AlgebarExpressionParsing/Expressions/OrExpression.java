package Parsing.AlgebarExpressionParsing.Expressions;

public class OrExpression extends OperandExpression {

    public OrExpression(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public int execute() {
        return (firstOperand.execute() + secondOperand.execute()) > 0 ? 1 : 0;
    }

}
