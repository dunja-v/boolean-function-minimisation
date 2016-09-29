package GUI.LogicCircuit;

import java.util.List;

import javax.swing.JComponent;

public abstract class JOperator extends JComponent implements JOperand{
    
    private static final long serialVersionUID = -7451469646815771866L;
    
    protected List<JOperand> operands;
    protected int delay;
    protected int value = -1;
    
    public JOperator(List<JOperand> operands){        
        this.operands = operands;
    }
    
    @Override
    public List<JOperand> getOperands() {
        return operands;
    }

    public void setDelay(int delay){
        this.delay = delay;
    }
    
    public int getDelay(){
        return delay;
    }
    
    public abstract void calculateValue();

   
    
    

}
