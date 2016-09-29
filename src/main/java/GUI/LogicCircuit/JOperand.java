package GUI.LogicCircuit;

import java.util.List;

public interface JOperand {
    
    public static final int DIMENSION = 10;
    
    public int getValue();

    public void calculateValue();

    public List<JOperand> getOperands();

}
