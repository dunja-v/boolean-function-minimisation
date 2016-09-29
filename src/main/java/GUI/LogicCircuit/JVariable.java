package GUI.LogicCircuit;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class JVariable extends JComponent implements JOperand {
    
    private static final long serialVersionUID = 64677455229223134L;
    
    private String varName;
    private int varValue;
    private int changedValue;
    private int index;
    private JLabel lValue;
    private JLabel lVarName;

    public JVariable(String varName, int index) {        
        this.varName = varName;
        this.index = index;
        varValue = -1;
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));        
        
        this.lValue = new JLabel("X");
        lValue.setForeground(Color.BLACK);
        this.lVarName = new JLabel(varName);
        lVarName.setForeground(Color.BLACK);
        
        lVarName.setAlignmentX(CENTER_ALIGNMENT);
        lValue.setAlignmentX(CENTER_ALIGNMENT);        

        add(lVarName);
        add(lValue);
    }
    
    public int getIndex() {
        return index;
    }
    
    public int getChangedValue() {
        return changedValue;
    }
    public void setChangedValue(int changedValue) {
        this.changedValue = changedValue;
    }
    
    @Override
    public int getValue() {        
        return varValue;
    }
    
    public void setValue(int value){
        this.varValue = value;
    }
    
    
    public String getVarName() {
        return varName;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
    }

    @Override
    public void calculateValue() {        
        
    }

    public void updateDisplayValue() {
        varValue = changedValue;
        lValue.setText(String.valueOf(varValue));        
    }

    @Override
    public List<JOperand> getOperands() {
        return null;
    }
    
    

}
