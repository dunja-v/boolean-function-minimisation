package GUI.LogicCircuit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

public class JOrOperator extends JOperator {

    private static final long serialVersionUID = -6020454822338917047L;
    public static final int HEIGHT = 6*DIMENSION;
    public static final int WIDTH = 6*DIMENSION;

    public JOrOperator(List<JOperand> operands) {
        super(operands);
        setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));
        this.value = -1;
    }

    @Override
    public synchronized int getValue() {
        return value;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("TimesRoman", Font.PLAIN, DIMENSION*2));
        g.drawString("≥1",2*DIMENSION, (int) Math.round(3.5*DIMENSION));        
        
    }

    @Override
    public void calculateValue() {

        if (operands != null) {
            int sum = 0;
            for (JOperand operand : operands) {
                int opValue = operand.getValue();
                if(opValue == -1){
                    sum = -1;
                    break;
                }
                sum += opValue;
            }
            if(sum == -1) value = -1;
            else value = sum == 0 ? 0 : 1;
            
        }else{
            value = -1;
        }
    }
    
     

}
