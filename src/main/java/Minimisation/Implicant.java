package Minimisation;

import java.util.Arrays;

public class Implicant implements Comparable<Implicant>{
    
    private int[] varValues;
    private int[] minterms;
    private String[] variableNames;
    
    private boolean isSum;
    
    private boolean dontCare;
    private boolean isPrime;
    private boolean isEssential;
    
    public Implicant(boolean isSum, String[] varNames, int[] varValues, int[] minterms, boolean dontCare, boolean isPrime, boolean isEssential){
        this.isSum = isSum;
        this.varValues = varValues;
        this.minterms = minterms;
        this.variableNames = varNames;
        this.dontCare = dontCare;
        this.isPrime = isPrime;
        this.isEssential = isEssential;
    }
    
    public int[] getMinterms() {
        return minterms;
    }
    
    public boolean isDontCare() {
        return dontCare;
    }
    
    public boolean isPrime() {
        return isPrime;
    }
    
    public boolean isEssential(){
        return isEssential;
    }

    public int[] getVarValues() {
        return varValues;
    }

    public void setPrime(boolean isPrime) {
        this.isPrime = isPrime;
    }
    
    public void setEssential(boolean isEssential){
        this.isEssential = isEssential;
    }
    
    public boolean coversMinterm(int minterm){
        for (int i=0; i<minterms.length; i++){
            if(minterms[i] == minterm){
                return true;
            }
        }
        
        return false;
    }
    
    public int[] valuesWithCommonIndex(int index){
        int[] newValues = new int[varValues.length];
        System.arraycopy(varValues, 0, newValues, 0, varValues.length);
        
        newValues[index] = -1;
        
        return newValues;
    }

    public static int getDifferingIndex(Implicant first, Implicant second){
        int diffCounter = 0;
        int diffIndex = -1;
        
        for(int i=0; i < first.varValues.length; i++){
            if(first.varValues[i] != second.varValues[i]){
                diffCounter++;
                diffIndex = i;
            }
        }
        
        if(diffCounter != 1) return -1;
        
        return diffIndex;
    }
    
    public static int[] combineMinterms(Implicant first, Implicant second){
        
        int[] min1 = first.minterms;
        int[] min2 = second.minterms;
        
        int[] minterms = new int[min1.length + min2.length];
        System.arraycopy(min1, 0, minterms, 0, min1.length);
        System.arraycopy(min2, 0, minterms, min1.length, min2.length);
        Arrays.sort(minterms);
        
        return minterms;
    }
    
    public static int variablesNeeded(Implicant impl){
        int count = impl.varValues.length;
        for(int value : impl.varValues){
            if(value != -1) count--;
        }        
        return count;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i < varValues.length; i++){
            
            if(varValues[i] == -1) continue;
            
            else{
                if(!isSum){
                    if(sb.length() != 0) sb.append("+");
                }
                
                if((varValues[i] == 1 && isSum) || (varValues[i]==0 && !isSum)){
                    sb.append(variableNames[i]);
                }else{
                    sb.append(variableNames[i] + "\'");
                }
            }
            
        }
        
        if(sb.length() == 0){
            if(isSum) sb.append("1");
            else sb.append("0");
        }
        
        return sb.toString();
        
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + Arrays.hashCode(varValues);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Implicant other = (Implicant) obj;
        if (!Arrays.equals(varValues, other.varValues))
            return false;
        return true;
    }

    @Override
    public int compareTo(Implicant impl) {
        if(impl.minterms.length < this.minterms.length) return -1;
        
        for(int i=0; i<minterms.length; i++){
            if(impl.minterms[i] < this.minterms[i]) return -1;
        }
    
    return 1;
    }

}
