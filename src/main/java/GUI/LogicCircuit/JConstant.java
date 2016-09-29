package GUI.LogicCircuit;

import java.util.List;

import javax.swing.JComponent;

public class JConstant extends JComponent implements JOperand {

    private static final long serialVersionUID = 1207174803476256352L;
    
    private int value;

    public JConstant(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void calculateValue() {
        
    }

    @Override
    public List<JOperand> getOperands() {
        return null;
    }

}
