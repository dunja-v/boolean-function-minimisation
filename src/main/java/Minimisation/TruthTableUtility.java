package Minimisation;

public class TruthTableUtility {
    
    private TruthTableUtility(){
    }
    
    public static int[][] getVarTable(int numOfVars){
        
        int rows = (int)Math.pow(2, numOfVars);
        
        int[][] table = new int[rows][numOfVars];
        
        for(int i=0; i<rows; i++){
            for (int j=numOfVars-1; j>=0; j--) {
                table[i][numOfVars - 1 - j]=(i/(int) Math.pow(2, j))%2;
            }
        }
        
        return table;
    }
    
    public static String toGreyCode(int num, int numOfVariables){
        
        int decimalGreyCode = num ^ num >> 1;        
        int binaryGreyCode = Integer.parseInt(Integer.toBinaryString(decimalGreyCode));
        
        return String.format("%0" + numOfVariables + "d", binaryGreyCode);
    }

}
