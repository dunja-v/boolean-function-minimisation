package Parsing.AlgebarExpressionParsing;

public class AlgebarLexer {
    
    private char[] data;
    private int currentIndex = 0;
    private Token token;
    
    static final char OR_OPERATOR = '+';
    static final String OR_NAME = "OR";
    
    static final char NOT_OPERATOR = '\'';
    static final String NOT_NAME = "NOT";
    
    static final char OPENED_BRACKETS = '(';
    static final char CLOSED_BRACKETS = ')';

    public AlgebarLexer(String input) {
        data = input.toUpperCase().toCharArray();
        
        if(data.length == 0){
            throw new LexerException("Polje za unos je prazno!");
        }
    }
    
    public Token getToken(){
        return token;
    }
    
    public Token nextToken(){
        
        skipBlanks();
        
        if (token != null && token.getType().equals(TokenType.EOF)) {
            throw new LexerException("End of file has been reached.");
        }

        else if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, "EOF");
        }
        
        else if(data[currentIndex]==OR_OPERATOR){
            token = new Token(TokenType.OR_OPERATOR, OR_NAME);
        }
        
        else if(data[currentIndex]==NOT_OPERATOR){
            token = new Token(TokenType.NOT_OPERATOR, NOT_NAME);
        }
        
        else if(Character.isAlphabetic(data[currentIndex])){
            token = new Token(TokenType.VARIABLE, String.valueOf(data[currentIndex]));
        }
        
        else if(data[currentIndex]==OPENED_BRACKETS){
            token = new Token(TokenType.OPENED_BRACKETS, "(");
        }
        
        else if(data[currentIndex]==CLOSED_BRACKETS){
            token = new Token(TokenType.CLOSED_BRACKETS, ")");
        }
        
        else if(data[currentIndex]=='1' || data[currentIndex]=='0'){
            token = new Token(TokenType.CONSTANT, String.valueOf(data[currentIndex]));
        }
        
        else{
            throw new LexerException("Znak " + data[currentIndex] + " nije dozvoljen u ovom obliku zadavanja funkcije.");
        }
        
        currentIndex++;
        return token;
        
    }
    
    public Token peek(){
        
        skipBlanks();
        
        if (currentIndex >= data.length) {
            return new Token(TokenType.EOF, "EOF");
        }
        
        else if(data[currentIndex]==OR_OPERATOR){
            return new Token(TokenType.OR_OPERATOR, OR_NAME);
        }
        
        else if(data[currentIndex]==NOT_OPERATOR){
            return new Token(TokenType.NOT_OPERATOR, NOT_NAME);
        }
        
        else if(Character.isAlphabetic(data[currentIndex])){
            return new Token(TokenType.VARIABLE, String.valueOf(data[currentIndex]));
        }
        
        else if(data[currentIndex]==OPENED_BRACKETS){
            return new Token(TokenType.OPENED_BRACKETS, "(");
        }
        
        else if(data[currentIndex]==CLOSED_BRACKETS){
            return new Token(TokenType.CLOSED_BRACKETS, ")");
        }
        
        else if(data[currentIndex]=='1' || data[currentIndex]=='0'){
            return new Token(TokenType.CONSTANT, String.valueOf(data[currentIndex]));
        }
        
        else{
            throw new LexerException("Znak " + data[currentIndex] + " nije dozvoljen u ovom obliku zadavanja funkcije.");
        }
    }

    private void skipBlanks() {
            while (currentIndex < data.length) {
                char c = data[currentIndex];
                if (Character.isWhitespace(c)) {
                    currentIndex++;
                    continue;
                }
                break;
            }
    }

}
