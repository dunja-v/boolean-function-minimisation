package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import GUI.LogicCircuit.JVariable;
import Minimisation.Implicant;

public class SimulationPanel extends JPanel {
    
    private static final long serialVersionUID = -7719022405538726384L;
    
    private double x;
    private double y;
    
    private LogicCircuitPanel logicPanel;
    private JPanel parametersPanel;    
    
    private JPanel varPanel;
    private JPanel delayPanel;
    private JTextField tfNot;
    private JTextField tfAnd;
    private JTextField tfOr;
    
    private JButton btnSimulate;
    
    private int numOfVars;
    private List<JVariable> variables;

    public SimulationPanel() {
        
        logicPanel = new LogicCircuitPanel();
        parametersPanel = new JPanel();
        parametersPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        setLayout(new BorderLayout());        
        add(logicPanel, BorderLayout.CENTER);
        add(parametersPanel, BorderLayout.PAGE_START);
        
        initMouseListeners();        
        initGUI();
        
    }
    
    public void setSelectedImplicants(List<Implicant> selectedImplicants){
        logicPanel.setSelectedImplicants(selectedImplicants);
    }
    
    public void initSimulation(String[] varNames, int[] function, boolean isSum) {
        
        numOfVars = varNames.length;                
        variables = new ArrayList<>();
        
        for(int i=0; i<numOfVars; i++){            
            JVariable var = new JVariable(varNames[i], i);
            variables.add(var);            
        }
        
        logicPanel.initLogicCircuit(numOfVars, variables,function, isSum);
        varPanel.removeAll();
        addVariables();
        
        btnSimulate.setEnabled(true);
        
    }

    private void initMouseListeners() {
        
        logicPanel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - x;
                double dy = e.getY() - y;

                x = e.getX();
                y = e.getY();

                logicPanel.increment(dx, dy);
                logicPanel.repaint();
            }
        });

        logicPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

        });

        logicPanel.addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoom = logicPanel.getZoom();

                if (e.getPreciseWheelRotation() > 0) {
                    zoom -= 0.1;
                } else {
                    zoom += 0.1;
                }

                if (zoom < 0.4) {
                    zoom = 0.4;
                }

                logicPanel.setZoom(zoom);
                repaint();
            }

        });
        
    }

    private void initGUI() {
        parametersPanel.setLayout(new GridLayout());
        
        varPanel = new JPanel();
        varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.PAGE_AXIS));
        addVariables();
        parametersPanel.add(varPanel);
        
        delayPanel = new JPanel();
        delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.PAGE_AXIS));
        addDelays();
        parametersPanel.add(delayPanel);
        
        btnSimulate = new JButton("Simuliraj");
        btnSimulate.setEnabled(false);
        delayPanel.add(btnSimulate);
        
        btnSimulate.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(variables != null){
                    int selectedRow = 0;
                    for(JVariable var : variables){
                        var.updateDisplayValue();
                        selectedRow *= 2;
                        selectedRow += var.getValue();
                    }
                    logicPanel.setSelectedRow(selectedRow);
                }
                
                try{
                    int notDelay = Integer.parseInt(tfNot.getText());
                    int orDelay = Integer.parseInt(tfOr.getText());
                    int andDelay = Integer.parseInt(tfAnd.getText());
                    
                    logicPanel.setDelays(notDelay, orDelay, andDelay);
                    logicPanel.calculateValues();
                    
                }catch(Exception ex){
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(SimulationPanel.this);
                    JOptionPane.showMessageDialog(topFrame,
                            "Kašnjenje mora biti nenegativan cijeli broj.",
                            "Pogreška prilikom unosa",
                            JOptionPane.ERROR_MESSAGE);
                }
                
                
            }
        });
        
        logicPanel.addSimulationButton(btnSimulate);
        
    }
    
    private void addDelays() {
        
        JPanel notBox = new JPanel(new FlowLayout());
        JLabel lNotDelay = new JLabel("Operator NE: ");
        tfNot = new JTextField();
        tfNot.setColumns(5);
        tfNot.setText("0");
        notBox.add(lNotDelay);
        notBox.add(tfNot);
        notBox.add(new JLabel("ms"));
        delayPanel.add(notBox);
        
        JPanel andBox = new JPanel(new FlowLayout());
        JLabel lAndDelay = new JLabel("Operator I: ");
        tfAnd = new JTextField();
        tfAnd.setColumns(5);
        tfAnd.setText("0");
        andBox.add(lAndDelay);
        andBox.add(tfAnd);
        andBox.add(new JLabel("ms"));
        delayPanel.add(andBox);
        
        JPanel orBox = new JPanel(new FlowLayout());
        JLabel lOrDelay = new JLabel("Operator ILI: ");
        tfOr = new JTextField();
        tfOr.setColumns(5);
        tfOr.setText("0");
        orBox.add(lOrDelay);
        orBox.add(tfOr);
        orBox.add(new JLabel("ms"));
        delayPanel.add(orBox);
        
        
    }

    private void addVariables() {
        
        if(variables != null){
            
            for(JVariable var : variables){
                JPanel varBox = new JPanel(new FlowLayout());
                
                JLabel lVar = new JLabel(var.getVarName());
                varBox.add(lVar);
                
                JButton btnVarValue = new JButton(String.valueOf(var.getChangedValue()));
                varBox.add(btnVarValue);                
                varPanel.add(varBox);
                
                btnVarValue.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int value = Integer.parseInt(btnVarValue.getText());
                        value = (value + 1) % 2;
                        
                        var.setChangedValue(value);
                        btnVarValue.setText(String.valueOf(value));
                        
                        repaint();
                        
                    }
                });
            }
        }
    }
    
    public void setCurrentFunction(int[] function, List<Implicant> selectedImplicants) {
        logicPanel.setCurrentFunction(function, selectedImplicants);
    }
    
    public void initKMap(String[] varNames, int[] function, int positiveValue){
        logicPanel.initKMap(varNames, function, positiveValue);
    }
    
    public void initTruthTable(String[] varNames, int[] function, int positiveValue){
        logicPanel.initTable(varNames, function, positiveValue);
    }

    

    

}
