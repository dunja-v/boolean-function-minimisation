package Parsing.AlgebarExpressionParsing;

public class Token {
    
    private TokenType type;
    private String name;
    
    public Token(TokenType type, String name) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TokenType getType() {
        return type;
    }
    
    

    

}
