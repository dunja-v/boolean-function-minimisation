package Parsing;

import java.util.Arrays;

public class NumericalValuesParser implements Parser {

    private char[] data;
    private char[] dcData;
    
    private int maxNumOfMinterm;
    private int numOfVars;
    
    private int currentIndex = 0;
    private int dcIndex;
    
    private int positiveValue = 1;
    private int negativeValue = 0;
    private int dontCareValue = 2;
    private int[] function;
    
    private static final String[] DEFAULT_VAR_NAMES = new String[]{"A","B","C","D"};

    public NumericalValuesParser(int numOfVars, String input, String dontCareInput, boolean isSum) {

        data = input.toCharArray();
        dcData = dontCareInput.toCharArray();
        
        if(data.length == 0){
            throw new ParsingException("Funkcija mora sadržavati barem jedan minterm ili maksterm.");
        }
        
        this.numOfVars = numOfVars;
        maxNumOfMinterm = (int) Math.pow(2, numOfVars);

        function = new int[maxNumOfMinterm];
        
        if(!isSum){            
            Arrays.fill(function, 1);
            positiveValue = 0;
            negativeValue = 1;
        }
    }
    
    public int[] parse(){
        parse(function, data, currentIndex, positiveValue);
        parse(function, dcData, dcIndex, dontCareValue);
        
        return function;
    }

    public int[] parse(int[] function, char[] data, int currentIndex, int positiveValue) {
        if (data == null) {
            throw new ParsingException(
                    "Input document cannot be null.");
        }

        while (currentIndex < data.length) {
            if (Character.isWhitespace(data[currentIndex]) || data[currentIndex] == (',')){
                currentIndex++;
                continue;
            }

            else if (Character.isDigit(data[currentIndex])) {
                int index = 0;
                while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
                    index = index * 10;
                    index += Integer.parseInt(String.valueOf(data[currentIndex]));
                    currentIndex++;
                }
                if (index >= maxNumOfMinterm) {
                    throw new ParsingException(
                            "Ne postoji redak " + index + " kod funkcije " + numOfVars + " varijable.");
                }else{                    
                    if(function[index] != negativeValue){
                        throw new ParsingException("Vrijednost za redak " + index + " je zadana više od jedog puta.");
                    }
                    function[index]=positiveValue;
                }
            }else{
                throw new ParsingException("Znak " + data[currentIndex] + " nije dozvoljen u ovom obliku zadavanja funkcije.");
            }
        }
        
        return function;
    }

    @Override
    public String[] getVariableNames() {
        String[] varNames = new String[numOfVars];
        
        for(int i=0; i<numOfVars; i++){
            varNames[i]=DEFAULT_VAR_NAMES[i];
        }
        
        return varNames;
    }

}
