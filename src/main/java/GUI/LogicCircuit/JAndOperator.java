package GUI.LogicCircuit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

public class JAndOperator extends JOperator {
    
    private static final long serialVersionUID = -5802534363093464217L;
    public static final int HEIGHT = 6*DIMENSION;
    public static final int WIDTH = 6*DIMENSION;

    public JAndOperator(List<JOperand> operands) {
        super(operands);   
        setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));
        this.value = -1;
    }

    @Override
    public int getValue() {
        return value;       
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("TimesRoman", Font.PLAIN, DIMENSION*2));
        g.drawString("&",(int) Math.round(2.5*DIMENSION), (int) Math.round(3.5*DIMENSION));
        
    }

    @Override
    public void calculateValue() {
        
        if (operands != null) {
            int product = 1;
            for (JOperand operand : operands) {
                int opValue = operand.getValue();
                if(opValue == -1){
                    product = -1;
                    break;
                }
                product *= opValue;
            }
            value = product;
        }else{
            value = -1;
        }
    }

}
