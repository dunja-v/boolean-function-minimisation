package Minimisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuineMcCluskey {
    
    private int[] function;
    private int numOfVars;
    private int value = 0;
    private int dontCareValue = 2;
    
    private boolean isSum;
    private String[] varNames;
    
    private List<Implicant> implicants;    
    
    public QuineMcCluskey(int[] function, String[] varNames, boolean isSum){
        this.function = function;
        this.numOfVars = (int) Math.round(Math.log(function.length)/Math.log(2));
        
        this.varNames = varNames;
        
        this.isSum = isSum;        
        if(isSum){
            value = 1;            
        }
        
        implicants = new ArrayList<>();
        
        createImplicants();
        getAllImplicants(implicants);
        determineEssentialPrimeImplicants();
    }
    
    public List<Implicant> getImplicants() {
        return implicants;
    }
    
    public int getNumOfVars() {
        return numOfVars;
    }
    
    private void createImplicants() {
        
        if(numOfVars == 0){
            return;            
        }else{
            int[][] varTable = TruthTableUtility.getVarTable(numOfVars);
            
            for(int i=0; i<function.length; i++){
                if(function[i] == value || function[i] == dontCareValue){                      
                    int[] implNum = new int[1];
                    implNum[0] = i;
                    
                    boolean dontCare = function[i] == dontCareValue ? true : false; 
                    Implicant impl = new Implicant(isSum, varNames, varTable[i], implNum, dontCare, true, false);
                    implicants.add(impl);
                }
            }            
        }
    }


    private void getAllImplicants(List<Implicant> currentStageImplicants) {
        
        List<Implicant> nextStageImplicants = new ArrayList<>();
        
        for(int i=0; i<currentStageImplicants.size(); i++){            
            Implicant first = currentStageImplicants.get(i);
            
            for(int j=i+1; j<currentStageImplicants.size(); j++){                
                Implicant second = currentStageImplicants.get(j);
                int index = Implicant.getDifferingIndex(first, second);
                
                if(index != -1){
                    int[] varValues = first.valuesWithCommonIndex(index);
                    
                    int[] minterms = Implicant.combineMinterms(first, second);
                    
                    Implicant impl = new Implicant(isSum, varNames, varValues, minterms, first.isDontCare() && second.isDontCare(), true, false);
                    if(!nextStageImplicants.contains(impl)){
                        nextStageImplicants.add(impl);
                    }                    
                    
                    first.setPrime(false);                    
                    second.setPrime(false);
                }
            }
        }
        
        implicants.addAll(nextStageImplicants);
        
        if(nextStageImplicants.size() > 1){
            getAllImplicants(nextStageImplicants);
        }
        
    }
    
    private void determineEssentialPrimeImplicants(){
        HashMap<Integer,List<Implicant>> minterms = new HashMap<>();
        for(int i = 0; i<function.length; i++){
            if(function[i] == value){
                minterms.put(Integer.valueOf(i), new ArrayList<Implicant>());
            }
        } 
        
        for(Implicant impl : implicants){
            if(impl.isPrime()){
                minterms.forEach((minterm, mList)->{
                    if(impl.coversMinterm(minterm)){
                        mList.add(impl);
                    }
                 }); 
            }            
        }
        
        minterms.forEach((minterm, mList)->{
            if(mList.size()==1){
                Implicant impl = mList.get(0);
                impl.setEssential(true);
            }
        });
    }
    
    
}
