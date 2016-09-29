package GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Parsing.FunctionInputMode;
import Parsing.MinimisationMode;

/**
 * Omogućava odabir načina zadavanja Booleove funkcije i unos zadane funkcije te odabir načina
 * prikaza zadane funkcije.
 * 
 * @author Dunja Vesinger
 * @version 1.0
 */
public class FunctionInputPanel extends JPanel {
    
    private static final long serialVersionUID = 6805003393118187398L;
    
    /**Broj varijabli zadane funkcije.*/
    private JComboBox<Integer> cbVarNum;
    /**Način zadavanja funkcije.*/
    private JComboBox<FunctionInputMode> cbFunctionMode;
    /**Polja u koja korisnik upisuje željenu funkciju.*/
    private UserInputPanel userInput;
    /**Način minimizacije funkcije.*/
    private JComboBox<MinimisationMode> cbMinimisedFunctionMode;
    
    /**Gumb koji pokreće minimizaciju.*/
    private JButton btnMinimise;
    
    public FunctionInputPanel(){
        
        initGUI();
    }
    
    public MinimisationMode getFunctionMinimisationMode(){
        return (MinimisationMode) cbMinimisedFunctionMode.getSelectedItem();
    }
    
    public void addMinimiseListener(ActionListener l){
        btnMinimise.addActionListener(l);
    }
    
    public String getFunctionInputText(){
        return userInput.getFunctionInput();
    }
    
    public String getDontCareInput(){
        return userInput.getDontCare();
    }
    
    public FunctionInputMode getFunctionInputMode(){
        return (FunctionInputMode) cbFunctionMode.getSelectedItem();
    }
    
    public Integer getVarNumber(){
        return (Integer) cbVarNum.getSelectedItem();
    }
    
    private void initGUI() {
        setLayout(new GridLayout(2,1));
        
        JPanel topPanel = new JPanel();
        add(topPanel);
        JPanel bottomPanel = new JPanel();
        add(bottomPanel);
        
        JLabel lVarNum = new JLabel("Broj varijabli:");
        topPanel.add(lVarNum);
        
        cbVarNum = new JComboBox<>(new Integer[]{1,2,3,4});
        topPanel.add(cbVarNum);
        
        JLabel lFunctionMode = new JLabel("Način zadavanja funkcije:");
        topPanel.add(lFunctionMode);
        
        cbFunctionMode = new JComboBox<>(FunctionInputMode.values());      
        topPanel.add(cbFunctionMode);
        
        userInput = new UserInputPanel((FunctionInputMode) cbFunctionMode.getSelectedItem());
        topPanel.add(userInput);
        
        cbFunctionMode.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                FunctionInputMode mode = (FunctionInputMode) cbFunctionMode.getSelectedItem();
                userInput.changeMode(mode);
                
                if(mode.equals(FunctionInputMode.ALGEBAR)){
                    cbVarNum.setEnabled(false);
                }else{
                    cbVarNum.setEnabled(true);
                }
            }
        });
        
        JButton btnHelp = new JButton("Pomoć");
        btnHelp.setToolTipText("Kako ispravno zadati funkciju");
        bottomPanel.add(btnHelp);
        
        btnHelp.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                HelpDialog help = new HelpDialog();
                help.pack();
                help.setVisible(true);
            }
        });
        
        JLabel lMinmisedFunctionMode = new JLabel("Prikaži kao:");
        bottomPanel.add(lMinmisedFunctionMode);
        
        cbMinimisedFunctionMode = new JComboBox<>(MinimisationMode.values());
        bottomPanel.add(cbMinimisedFunctionMode);
        
        btnMinimise = new JButton("Odredi implikante");
        bottomPanel.add(btnMinimise);
        
    }

}
