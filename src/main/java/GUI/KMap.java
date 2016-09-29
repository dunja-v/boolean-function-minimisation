package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import Minimisation.Implicant;
import Minimisation.TruthTableUtility;

public class KMap extends JComponent {
    
    private static final long serialVersionUID = 6573165311088660197L;
    
    public static final int DIMENSION = 30;
    public static final int PADDING = DIMENSION;
    public static final int RADIUS = 26;
    public static final int OFFSET = 2;
    
    public static final int PADDING_TOP = (int) (0.5*DIMENSION);
    public static final int PADDING_LEFT = (int) (0.5*DIMENSION);
    
    public static final int COL_BEGIN = PADDING + DIMENSION;
    public static final int ROW_BEGIN = PADDING_TOP + DIMENSION;
    
    public static final int STRING_PADDING = (int) (0.5*DIMENSION);
    
    private int numOfVars;
    private String[] variableNames;
    private int[] correctFunction;
    private int[] givenFunction;
    private List<Implicant> selectedImplicants;
    private int selectedCell = -1;
    
    private int varNumRow;
    private int varNumColumn;
    private int numOfCols;
    private int numOfRows;
    
    private int positiveValue;
    
    public KMap() {
        super();
        
    }
    
    public void initKMap(String[] variableNames, int[] correctFunction, int positiveValue){        
        this.variableNames = variableNames;
        this.numOfVars = variableNames.length;
        this.correctFunction = correctFunction;
        this.positiveValue = (positiveValue + 1)%2;
        this.selectedCell = -1;
        
        varNumRow = numOfVars/2;
        varNumColumn = numOfVars/2 + numOfVars%2;
        
        numOfCols =(int) Math.pow(2, varNumColumn);
        numOfRows = (int) Math.pow(2, varNumRow);
        
        givenFunction = new int[(int) Math.pow(2,numOfVars)];
        Arrays.fill(givenFunction, this.positiveValue);
        this.selectedImplicants = null;
        
        setPreferredSize(new Dimension(DIMENSION*(numOfCols+1) + 2*PADDING, DIMENSION*(numOfRows+2) + 2*PADDING));
        
        repaint();
        
    }
    
    public void setSelectedFunction(int[] function, List<Implicant> selectedImplicants){
        if(function == null){
            givenFunction = new int[(int) Math.pow(2,numOfVars)];
            Arrays.fill(givenFunction, positiveValue);
            this.selectedImplicants = null;
        }else{
            this.givenFunction = function;
            this.selectedImplicants = selectedImplicants;
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        removeAll();
        
        if(numOfVars != 0){            
            
            if(givenFunction != null){
                drawFunction(g);
            }
            
            if(selectedImplicants != null){
                for(Implicant impl : selectedImplicants){
                    circleImplicant(g, impl);
                }
            }            
            drawGrid(g);
        }
    }

    private void drawFunction(Graphics g) {
        
        int currentCol = 0;
        int currentRow = 0;
        
               
        for(int i=0; i<givenFunction.length; i++){
            
            if(currentRow >= numOfRows){
                currentCol++;
                currentRow=0;
            }
            
            int x = COL_BEGIN + getKMapPosition(currentCol)*DIMENSION;
            int y = ROW_BEGIN + getKMapPosition(currentRow)*DIMENSION;
            
            if(correctFunction[i] == 2){
                g.setColor(Color.YELLOW);
            }
            else if(givenFunction[i] != correctFunction[i]){
                g.setColor(Color.RED);                
            }else{
                g.setColor(Color.GREEN);
            }           
            
            g.fillRect(x, y, DIMENSION, DIMENSION);
            
            Font f = g.getFont();
            
            if(i == selectedCell){            	
            	Font highlight = f.deriveFont(Font.BOLD, f.getSize() + 2);
            	g.setFont(highlight);
            }
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(givenFunction[i]), x + STRING_PADDING - 1, y + STRING_PADDING + 2);
            g.setFont(f);
            
            currentRow++;
            
        }
        
    }

    private void drawGrid(Graphics g) {
        
        String colNames = getVarNames(0, varNumColumn);
        String rowNames = getVarNames(varNumColumn, variableNames.length);
        
        //drawing variable names
        g.drawString(colNames, PADDING, PADDING_TOP);
        g.drawString(rowNames, PADDING_LEFT, PADDING);        
        
        
        int endOfGridX = numOfCols*DIMENSION + COL_BEGIN;
        int endOfGridY = numOfRows*DIMENSION + ROW_BEGIN;        
        
        //drawing columns and column variable values
        for(int i=0; i<numOfCols; i++){            
            
            int lineDistance = COL_BEGIN + i*DIMENSION;
            int numDistance = lineDistance + STRING_PADDING;            
            
            String greyCode = TruthTableUtility.toGreyCode(i, varNumColumn);
            
            g.drawLine(lineDistance, PADDING_TOP, lineDistance, endOfGridY);
            g.drawString(greyCode, numDistance, PADDING);            
        }
        
        int leftPadding = varNumRow == 0 ? COL_BEGIN : PADDING_LEFT;
        
      //drawing rows and row variable values
        for(int i=0; i<numOfRows; i++){
            
            int lineDistance = ROW_BEGIN + i*DIMENSION;
            int numDistance = lineDistance + STRING_PADDING;           
            
            String greyCode = varNumRow == 0 ? "" : TruthTableUtility.toGreyCode(i, varNumRow);
            
            g.drawLine(leftPadding, lineDistance, endOfGridX, lineDistance);
            g.drawString(greyCode, PADDING, numDistance);
        }
        
        //drawing bottom edge of the grid
        g.drawLine(leftPadding, endOfGridY, endOfGridX, endOfGridY);
        //drawing right edge of the grid
        g.drawLine(endOfGridX, PADDING_TOP, endOfGridX, endOfGridY);
        
    }

    private void circleImplicant(Graphics g, Implicant impl) {
        int[] minterms = impl.getMinterms();
        g.setColor(Color.BLACK);
        if(minterms.length == 1) circleRound(g, minterms[0]);
        else if(minterms.length == 2) circleHalf(g, minterms);
        else circleEdge(g, minterms);
    }

    private void circleRound(Graphics g, int minterm) {
        int row = getRow(minterm);
        int col = getColumn(minterm);

        int cornerX = COL_BEGIN + col*DIMENSION + OFFSET;
        int cornerY = ROW_BEGIN + row*DIMENSION + OFFSET;        
        g.drawOval(cornerX, cornerY, RADIUS, RADIUS);
    }
    
    private void circleHalf(Graphics g, int[] minterms) {
        int first = minterms[0];
        int second = minterms[1];
        int firstX = COL_BEGIN + getColumn(first)*DIMENSION;
        int firstY = ROW_BEGIN + getRow(first)*DIMENSION;
        int secondX = COL_BEGIN + getColumn(second)*DIMENSION;
        int secondY = ROW_BEGIN + getRow(second)*DIMENSION;
        
        if (hasRight(first, minterms)){
            circleLeftHalf(g, firstX, firstY);
            circleRightHalf(g, secondX, secondY);            
            
        }else if(hasLeft(first, minterms)){
            circleRightHalf(g, firstX, firstY);
            circleLeftHalf(g, secondX, secondY);
            
        }else if(hasBottom(first, minterms)){
            circleTopHalf(g, firstX, firstY);
            circleBottomHalf(g, secondX, secondY);
            
        }else{
            circleBottomHalf(g, firstX, firstY);
            circleTopHalf(g, secondX, secondY);
        }
        
    }
    
    private void circleBottomHalf(Graphics g, int x, int y) {
        g.drawArc(x + OFFSET, y - DIMENSION + OFFSET, DIMENSION - 2*OFFSET, 2*(DIMENSION - OFFSET), 180, 180);        
    }

    private void circleTopHalf(Graphics g, int x, int y) {
        g.drawArc(x + OFFSET, y + OFFSET, DIMENSION - 2*OFFSET, 2*(DIMENSION - OFFSET), 180, -180);        
    }

    private void circleRightHalf(Graphics g, int x, int y) {
        g.drawArc(x - DIMENSION + OFFSET, y + OFFSET, 2*DIMENSION - OFFSET, DIMENSION - 2*OFFSET, 270, 180);
    }

    private void circleLeftHalf(Graphics g, int x, int y) {
        g.drawArc(x + OFFSET, y + OFFSET, 2*(DIMENSION - OFFSET), DIMENSION - 2*OFFSET, 270, -180);        
    }

    private void circleEdge(Graphics g, int[] minterms) {
        for(int min : minterms){
            boolean hasRight = hasRight(min, minterms);
            boolean hasLeft = hasLeft(min, minterms);
            boolean hasTop = hasTop(min, minterms);
            boolean hasBottom = hasBottom(min, minterms);
            
            int row = getRow(min);
            int col = getColumn(min);
            int beginY = ROW_BEGIN + row*DIMENSION;
            int beginX = COL_BEGIN + col*DIMENSION;
            
            if(hasRight && hasBottom && (!hasTop || row==0) && (!hasLeft || col==0)){
                circleTopLeftCorner(g, beginX, beginY);
                
            }else if(hasLeft && hasBottom && (!hasTop || row==0) && (!hasRight || col==(numOfCols-1))){
                circleTopRightCorner(g, beginX, beginY);
                
            }else if(hasTop && hasRight && (!hasBottom || row==(numOfRows-1)) && (!hasLeft || col==0)){
                circleBottomLeftCorner(g, beginX, beginY);
                
            }else if(hasTop && hasLeft && (!hasBottom || row==(numOfRows-1)) && (!hasRight || col==(numOfCols-1))){
                circleBottomRightCorner(g, beginX, beginY);
                
            }else if(hasRight && (!hasLeft || col==0) && !hasBottom && !hasTop){
                circleLeftHalf(g, beginX, beginY);
                
            }else if(hasLeft && !hasTop && !hasBottom && (!hasRight || col==(numOfCols-1))){
                circleRightHalf(g, beginX, beginY);
                
            }else if(hasBottom && (!hasTop || row==0) && !hasRight && !hasLeft){ 
                circleTopHalf(g, beginX, beginY);
                
            }else if(hasTop && (!hasBottom||row==(numOfRows-1)) && !hasLeft && !hasRight){
                circleBottomHalf(g, beginX, beginY);
                
            }else if(!hasTop && !hasBottom && hasLeft && hasRight){
                circleTopEdge(g, beginX, beginY);
                circleBottomEdge(g, beginX, beginY);
                
            }else if(!hasLeft && !hasRight && hasTop && hasBottom){
                circleLeftEdge(g, beginX, beginY);
                circleRightEdge(g, beginX, beginY);
                
            }else if(!hasLeft && hasRight && hasTop && hasBottom){
                circleLeftEdge(g, beginX, beginY);
                
            }else if(!hasRight && hasTop && hasBottom && hasLeft){
                circleRightEdge(g, beginX, beginY);
                
            }else if(!hasTop && hasLeft && hasBottom && hasRight){
                circleTopEdge(g, beginX, beginY);
                
            }else if(!hasBottom && hasTop && hasLeft && hasRight){
                circleBottomEdge(g, beginX, beginY);
                
            }else{
                if(row == 0) circleTopEdge(g, beginX, beginY);
                else if(row == (numOfRows -1)) circleBottomEdge(g, beginX, beginY);
                else if(col == 0) circleLeftEdge(g, beginX, beginY);
                else if(col == (numOfCols - 1)) circleRightEdge(g, beginX, beginY);
            }
        }
        
    }
    

    private void circleTopLeftCorner(Graphics g, int x, int y) {
        int lineLength = (int) Math.round(2*DIMENSION/3.);
        int arcLength = DIMENSION - lineLength;
        g.drawLine(x + arcLength, y + OFFSET, x + DIMENSION, y + OFFSET);
        g.drawLine(x + OFFSET, y + arcLength, x + OFFSET, y + DIMENSION);
        g.drawArc(x + OFFSET, y + OFFSET, 2*arcLength, 2*arcLength, 90, 90);        
    }
    
    private void circleTopRightCorner(Graphics g, int x, int y) {
        int lineLength = (int) Math.round(2*DIMENSION/3.);
        int arcLength = DIMENSION - lineLength;
        g.drawLine(x, y + OFFSET, x + DIMENSION - arcLength, y + OFFSET);
        g.drawLine(x + DIMENSION - OFFSET, y + arcLength, x + DIMENSION - OFFSET, y + DIMENSION);
        g.drawArc(x + arcLength - OFFSET, y + OFFSET, 2*arcLength, 2*arcLength, 90, -90);
    }
    
    private void circleBottomLeftCorner(Graphics g, int x, int y) {
        int lineLength = (int) Math.round(2*DIMENSION/3.);
        int arcLength = DIMENSION - lineLength;
        g.drawLine(x + arcLength, y + DIMENSION - OFFSET, x + DIMENSION, y + DIMENSION - OFFSET);
        g.drawLine(x + OFFSET, y, x + OFFSET, y + lineLength);
        g.drawArc(x + OFFSET, y + arcLength - OFFSET, 2*arcLength, 2*arcLength, -90, -90);
    }

    private void circleBottomRightCorner(Graphics g, int x, int y) {
        int lineLength = (int) Math.round(2*DIMENSION/3.);
        int arcLength = DIMENSION - lineLength;
        g.drawLine(x, y + DIMENSION - OFFSET, x + lineLength, y + DIMENSION - OFFSET);
        g.drawLine(x + DIMENSION - OFFSET, y, x + DIMENSION - OFFSET, y + lineLength);
        g.drawArc(x + arcLength - OFFSET, y + arcLength - OFFSET, 2*arcLength, 2*arcLength, -90, 90);        
    }

    private void circleLeftEdge(Graphics g, int x, int y) {
        g.drawLine(x + OFFSET, y, x + OFFSET, y + DIMENSION);        
    }

    private void circleRightEdge(Graphics g, int x, int y) {
        g.drawLine(x + DIMENSION - OFFSET, y, x + DIMENSION - OFFSET, y + DIMENSION);        
    }
    
    private void circleTopEdge(Graphics g, int x, int y) {
        g.drawLine(x, y + OFFSET, x + DIMENSION, y + OFFSET);        
    }

    private void circleBottomEdge(Graphics g, int x, int y) {
        g.drawLine(x, y + DIMENSION - OFFSET, x + DIMENSION, y + DIMENSION - OFFSET);
    }

    private int getKMapPosition(int n){
        return Math.abs(-(n + 1)%3 + 4*((n + 1)/3)) - 1;
    }
    
    private int getRow(int minterm){
        int row = minterm;
        
        if(row >= numOfRows){
            row = row%numOfRows;
        }        
        return getKMapPosition(row);
    }
    
    private int getColumn(int minterm) {
        int col = 0;
        
        if(minterm >= numOfRows){
            col = minterm/numOfRows;
        }
        return getKMapPosition(col);
    }
    
    private boolean hasRight(int first, int[] minterms) {
        int row = getRow(first);
        int column = (getColumn(first) + 1) % numOfCols;
        for(int min : minterms){
            if(getRow(min) == row && getColumn(min) == column) return true;
        }
        return false;
    }

    private boolean hasLeft(int first, int[] minterms) {
        int row = getRow(first);        
        int column = getColumn(first) - 1;        
        if(column < 0 ) column = numOfCols + column;
        
        for(int min : minterms){
            if(getRow(min) == row && getColumn(min) == column) return true;
        }
        return false;
    }

    private boolean hasBottom(int first, int[] minterms) {
        int row = (getRow(first) + 1) % numOfRows;
        int column = getColumn(first);
        for(int min : minterms){
            if(getRow(min) == row && getColumn(min)==column) return true;
        }
        return false;
    }

    private boolean hasTop(int first, int[] minterms) {
        int row = (getRow(first) - 1) % numOfRows;
        if(row < 0) row = numOfRows + row;
        int column = getColumn(first);
        
        for(int min : minterms){
            if(getRow(min) == row && getColumn(min)==column) return true;
        }
        return false;
    }
    
    private String getVarNames(int beginIndex, int endIndex) {
        
        StringBuilder sb = new StringBuilder();
        for(int i=beginIndex; i<endIndex; i++){
            sb.append(variableNames[i]);
        }        
        return sb.toString();
    }

	public void setSelectedCell(int selectedCell) {
		this.selectedCell = selectedCell;
		
	}

    
    

}
