package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Minimisation.Implicant;
import Minimisation.QuineMcCluskey;
import Parsing.FunctionInputMode;
import Parsing.MinimisationMode;
import Parsing.NumericalValuesParser;
import Parsing.Parser;
import Parsing.ParsingException;
import Parsing.AlgebarExpressionParsing.AlgebarParser;

public class FunctionMinimisationFrame extends JFrame {
    
    private static final long serialVersionUID = -8534162931011215192L;
    
    /**Panel za unos Booleove funkcije.**/
    private FunctionInputPanel functionInput;    
    /**Panel koji prikazuje popise implikanata zadane funkcije.*/
    private ImplicantsPanel implicants;
    /**Panel koji prikazuje logičku shemu za odabrane implikante,
     * tablicu istinitosti i K tablicu te omogućava 
     * simulaciju rada logičkih sklopova.*/
    private SimulationPanel simulation;
    
    private QuineMcCluskey qm;
    /**Zadana funkcija*/
    private int[] function;
    /**Vrijednost funkcije koju označava odabrani implikant; 1 za sumu produkata, 0 za produkt suma.*/
    private int positiveValue;
    
    public FunctionMinimisationFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);        
        setTitle("Minimizacija Booleovih funkcija (Autor: Dunja Vesinger)");
        initGUI();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initGUI() {
        
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());       
        
        functionInput = new FunctionInputPanel();        
        cp.add(functionInput, BorderLayout.PAGE_START);
        
        implicants = new ImplicantsPanel();
        implicants.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        cp.add(implicants, BorderLayout.LINE_START);
        
        
        simulation = new SimulationPanel();
        simulation.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(simulation, BorderLayout.CENTER);
        
        
        functionInput.addMinimiseListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {                
                minimise();
            }
        });
        
        implicants.setImplicantsListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                setSelectedFunction();
            }
        });
    }
    
    /**
     * Na temelju odabranih implikanata računa trenutno odabranu funkciju i osvježava prikaz u tablici
     * istinitosti, K-tablici i logičku shemu.
     */
    private void setSelectedFunction(){
        List<Implicant> selectedImplicants = implicants.getSelectedImplicants();
        int[] selectedFunction = null;
        
        if(!selectedImplicants.isEmpty()){
            selectedFunction = new int[function.length];
            
            Arrays.fill(selectedFunction, (positiveValue+1)%2);
            
            for(Implicant impl : selectedImplicants){
                for(int minterm : impl.getMinterms()){
                    selectedFunction[minterm] = positiveValue;
                }
            }
        }        
        
        simulation.setCurrentFunction(selectedFunction, selectedImplicants);
        simulation.setSelectedImplicants(selectedImplicants);
        simulation.repaint();
        simulation.revalidate();
    }
    
    /**
     * Parsira zadanu funkciju i provjerava njezinu ispravnost te za ispravno zadanu funkciju provodi 
     * QuineMcCluskey algoritam i stvara sve implikante dobivene njime, a za neispravno zadanu funkciju 
     * stvara dijalog s ispisom pogreške.
     */
    private void minimise(){
        
        Parser parser;
        
        try{
            
            FunctionInputMode inputMode = functionInput.getFunctionInputMode();
            if(inputMode == FunctionInputMode.SUM_OF_MINTERMS){
                parser = new NumericalValuesParser(functionInput.getVarNumber(), functionInput.getFunctionInputText(),
                        functionInput.getDontCareInput(),true);
            }else if(inputMode == FunctionInputMode.SUM_OF_PRODUCTS){
                parser = new NumericalValuesParser(functionInput.getVarNumber(), functionInput.getFunctionInputText(),
                        functionInput.getDontCareInput(), false);
            }else{
                parser = new AlgebarParser(functionInput.getFunctionInputText());                
            }
            
        
            function = parser.parse();
            MinimisationMode mode = functionInput.getFunctionMinimisationMode();
            boolean isSumOfProducts;
            if(mode == MinimisationMode.SUM_OF_PRODUCTS){
                isSumOfProducts = true;
                positiveValue = 1;
            }else{
                isSumOfProducts = false;
                positiveValue = 0;
            }
            
            simulation.initTruthTable(parser.getVariableNames(), function, positiveValue);
            simulation.initKMap(parser.getVariableNames(), function, positiveValue);
            
            simulation.initSimulation(parser.getVariableNames(), function, isSumOfProducts);
            simulation.repaint();
            simulation.revalidate();
            
            qm = new QuineMcCluskey(function, parser.getVariableNames(), isSumOfProducts);                    
            implicants.setImplicants(parser.getVariableNames(), qm.getImplicants());            
            
        }catch(ParsingException exc){
            JOptionPane.showMessageDialog(this,
                    exc.getMessage(),
                    "Pogreška prilikom prevođenja",
                    JOptionPane.ERROR_MESSAGE);
            
        } 
    }

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ignorable) {
        }

        SwingUtilities.invokeLater(FunctionMinimisationFrame::new);
    }

}
