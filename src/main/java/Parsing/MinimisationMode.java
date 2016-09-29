package Parsing;

public enum MinimisationMode {
    
    SUM_OF_PRODUCTS("Suma produkata"),
    
    PRODUCT_OF_SUMS("Produkt suma");
    
    private final String text;
    
    private MinimisationMode(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
