package GUI;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Parsing.FunctionInputMode;

/**
 * Polja u koja korisnik unosi Booleovu funkciju. Njihov je broj i izgled promjenjiv
 * i ovisi o odabranom načinu zadavanja funkcije.
 * 
 * @author Dunja Vesinger
 * @version 1.0
 */
public class UserInputPanel extends JPanel {
    
    private static final long serialVersionUID = -3972315919550233405L;
    /**Broj stupaca polja za unos.*/
    private static final int INPUT_WIDTH = 10;
    private static final int SECOND_INPUT_WIDTH = 8;
    
    /**Polje za korisnikov unos.*/
    private JTextField input;
    /**Polje za unos don't care polja(ne postoji u algebarskom načinu zadavanja).*/
    private JTextField secondInput;
    
    /**Labela koja se ispisuje prije prvog polja za unos.*/
    private JLabel beforeInput;
    /**Labela između dva polja za unos(ne postoji u algebarskom načinu zadavanja).*/
    private JLabel inbetweenInputs;
    /**Labela nakon zadnjeg polja za unos.*/
    private JLabel afterInput;
    
    

    public UserInputPanel(FunctionInputMode mode) {
        
        setLayout(new FlowLayout());
        input = new JTextField();
        secondInput = new JTextField();
        input.setColumns(INPUT_WIDTH);
        secondInput.setColumns(SECOND_INPUT_WIDTH);
        beforeInput = new JLabel();
        inbetweenInputs = new JLabel();
        afterInput = new JLabel();
        
        add(new JLabel("f = "));
        
        changeMode(mode);
        
    }

    public void changeMode(FunctionInputMode mode) {        
        
        if(mode.equals(FunctionInputMode.SUM_OF_MINTERMS)) sumOfMinterms();
        else if(mode.equals(FunctionInputMode.SUM_OF_PRODUCTS)) sumOfProducts();
        else algebar();
    }

    private void algebar() {        
        beforeInput.setText("");        
        afterInput.setText("");
        
        input.setColumns(INPUT_WIDTH * 2);
        
        add(beforeInput);
        add(input);        
        add(afterInput);
        
        remove(secondInput);
        remove(inbetweenInputs);
    }

    private void sumOfProducts() {
        beforeInput.setText("∏ M(");
        inbetweenInputs.setText(") + ∏ D(");
        afterInput.setText(")");
        
        input.setColumns(INPUT_WIDTH);
        
        add(beforeInput);
        add(input);
        add(inbetweenInputs);
        add(secondInput);
        add(afterInput);
        
    }

    private void sumOfMinterms() {
        beforeInput.setText("∑ m(");
        inbetweenInputs.setText(") + ∑ d(");
        afterInput.setText(")");
        
        input.setColumns(INPUT_WIDTH);
        
        add(beforeInput);
        add(input);
        add(inbetweenInputs);
        add(secondInput);
        add(afterInput);
        
    }

    public String getFunctionInput() {
        return input==null ? null : input.getText();
    }

    public String getDontCare(){
        return secondInput==null ? null : secondInput.getText();
    }
    
    

}
