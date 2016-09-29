package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JComponent;

import Minimisation.TruthTableUtility;

public class TruthTable extends JComponent {
    
    private int numOfVars;
    private int numOfRows;
    
    private int[] correctFunction;
    private int[] givenFunction;
    private int selectedRow = -1;
    
    private int [][] table;
    private String[] varNames;
    private int positiveValue;
    
    private static final int DISTANCE = 20;
    
    
    private static final long serialVersionUID = -2494169391768146770L;
    
    public TruthTable(){
        
    }
    
    public void initTable(String[] varNames, int[] function, int positiveValue) {
        this.varNames = varNames;
        this.numOfVars = varNames.length;
        this.positiveValue = (positiveValue + 1)%2;
        this.numOfRows = (int) Math.pow(2, numOfVars);
        
        this.table = TruthTableUtility.getVarTable(numOfVars);
        this.correctFunction = function;
        
        givenFunction = new int[(int) Math.pow(2,numOfVars)];
        Arrays.fill(givenFunction, this.positiveValue);
        
        setPreferredSize(new Dimension((numOfVars+2)*DISTANCE, (numOfRows + 2) * DISTANCE));
        
        repaint();
        
    }
    
    public void setGivenFunction(int[] function){
        if(function == null){
            givenFunction = new int[(int) Math.pow(2,numOfVars)];
            Arrays.fill(givenFunction, positiveValue);
        }else{
            this.givenFunction = function;
        }
        
        
        repaint();
    }
    
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        removeAll();
        
        g.setColor(Color.BLACK);        
        
        if(table != null && varNames.length != 0){
             
            for(int i=0; i<numOfVars; i++){
                g.drawString(varNames[i], i*DISTANCE, DISTANCE);
            }
            
            g.drawString("f", numOfVars*DISTANCE, DISTANCE);
            g.drawString("f'", (numOfVars + 1) * DISTANCE, DISTANCE);
            
             for(int i=0; i<numOfRows; i++){
                 for(int j=0; j<numOfVars; j++){
                     g.drawString(String.valueOf(table[i][j]), j*DISTANCE, (i+2)*DISTANCE);
                 }
                 
                 g.drawString(String.valueOf(correctFunction[i]), numOfVars * DISTANCE, (i+2)*DISTANCE);
             }
             
             if(givenFunction != null){
                 
                 for(int i=0; i<givenFunction.length; i++){
                     
                     if(correctFunction[i] == 2){
                         g.setColor(Color.YELLOW);
                     }
                     else if(givenFunction[i] != correctFunction[i]){
                         g.setColor(Color.RED);                
                     }else{
                         g.setColor(Color.GREEN);
                     } 
                     
                     /////IZVADITI IZRAZE
                     g.fillRect((numOfVars+1)*DISTANCE - DISTANCE/2, (i+1)*DISTANCE + DISTANCE/2, DISTANCE, DISTANCE); 
                     g.setColor(Color.BLACK);
                     String functionValue = String.valueOf(givenFunction[i]);
                     if(givenFunction[i]==-1){
                         functionValue = "X";
                     }
                     g.drawString(functionValue, (numOfVars+1)*DISTANCE, (i+2)*DISTANCE);
                     
                 }
             } else{
                 for(int i=0; i<correctFunction.length; i++){
                     g.drawString("X", numOfVars*DISTANCE, (i+2)*DISTANCE);
                 } 
             }
             
             if(selectedRow >= 0){
            	 g.setColor(new Color(0,153,153,128));
                 g.fillRect(0, (int) Math.round((selectedRow + 1.5)*DISTANCE), 
                		 (int)Math.round((numOfVars + 1.5)*DISTANCE), DISTANCE);                 
             }
             
             
             g.setColor(Color.BLACK);
             int givenFunX = (int) Math.round((numOfVars + 0.5)*DISTANCE);
             int corrFunX = (int) Math.round((numOfVars - 0.5)*DISTANCE);
             int beginY = (int) Math.round(0.5*DISTANCE);
             int endY = (int) Math.round((correctFunction.length +1.5)*DISTANCE);
             //Vertical lines between functions
             g.drawLine(corrFunX, beginY, corrFunX, endY);
             g.drawLine(givenFunX, beginY, givenFunX, endY);
             
             int endX = (numOfVars + 2)*DISTANCE;
             int horLineY = (int) Math.round(1.5*DISTANCE) - 2;
             //Horizontal line below variable names
             g.drawLine(0,horLineY , endX, horLineY);
             
        }
    }
    


    
    
    
    

}
