package Parsing.AlgebarExpressionParsing.Expressions;

public class ConstantExpression implements Expression {
    
    private int value;

    public ConstantExpression(String value) {
        this.value = Integer.parseInt(value);
    }

    @Override
    public int execute() {
        return value;
    }

}
