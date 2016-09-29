package Parsing.AlgebarExpressionParsing.Expressions;

public class VarExpression implements Expression{
    
    private int value;
    private String name;

    public VarExpression(int value, String name) {
        this.value = value;
        this.name = name;        
    }

    @Override
    public int execute() {
        return value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
    
    

}
