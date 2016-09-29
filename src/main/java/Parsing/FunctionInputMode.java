package Parsing;

public enum FunctionInputMode {
    
    SUM_OF_MINTERMS("Suma minterma"),
    
    SUM_OF_PRODUCTS("Suma maksterma"),
    
    ALGEBAR("Algebarski");
    
    private final String text;
    
    private FunctionInputMode(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }
    
}
