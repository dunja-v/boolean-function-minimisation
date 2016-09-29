package GUI.LogicCircuit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

public class JNotOperator extends JOperator {
    
    private static final long serialVersionUID = 9122672535855443369L;
    private static final int RADIUS = 2;
    
    public static final int HEIGHT = 2*DIMENSION + RADIUS;
    public static final int WIDTH = 2*DIMENSION;
    
    private int varIndex;

    public JNotOperator(List<JOperand> operands, int varIndex) {
        super(operands);
        this.varIndex = varIndex;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    public int getVarIndex(){
        return varIndex;
    }

    @Override
    public int getValue() {
        return value;        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 2*DIMENSION, 0);
        g.drawLine(0, 0, DIMENSION, 2*DIMENSION);
        g.drawLine(2*DIMENSION, 0, DIMENSION, 2*DIMENSION);
        g.drawOval(DIMENSION - RADIUS, 2*DIMENSION, 2*RADIUS, 2*RADIUS);
        
   }

    @Override
    public void calculateValue() {

        if (operands != null) {
            int opValue = operands.get(0).getValue();
            if (opValue == -1)
                value = -1;
            else
                value = (opValue + 1) % 2;
        } else {
            value = -1;
        }
    }

    

}
