package GUI;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import Minimisation.Implicant;

public class ImplicantsPanel extends JPanel{
    
    private static final long serialVersionUID = -6656333010570340402L;
    
    private JTable implicantsTable;
    private String[] columnNames = new String[]{"Produkti/Sume","Indeksi", "DC", "PI","BPI"} ;
    private ListSelectionModel selectionModel;
    
    public ImplicantsPanel(){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initGUI();
    }

    /**
     * Inicijalizira praznu listu implikanata.
     */
    private void initGUI() {
        
        implicantsTable = new JTable(new ImplicantsTableModel(columnNames, null));     
        implicantsTable.setRowSelectionAllowed(true);
        setColumnWidths();
        implicantsTable.getTableHeader().setReorderingAllowed(false);
        
        selectionModel = implicantsTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(implicantsTable);
        implicantsTable.setFillsViewportHeight(true);
        scrollPane.setPreferredSize(new Dimension(230,450));
        add(scrollPane);
        
        add(new JLabel("DC = Don't care"));
        add(new JLabel("PI = Primarni implikant"));
        add(new JLabel("BPI = Bitni primarni implikant"));
        
    }

    /**
     * Postavlja sve implikante zadane funkcije u listu.
     * @param variableNames
     * @param implicants
     */
    public void setImplicants(String[] variableNames, List<Implicant> implicants) {
        
        implicantsTable.setModel(new ImplicantsTableModel(columnNames,
                implicants.toArray(new Implicant[implicants.size()])));
        setColumnWidths();
    }

    public void setImplicantsListener(ListSelectionListener listener){
        selectionModel.addListSelectionListener(listener);
    }
    
    public List<Implicant> getSelectedImplicants(){
        List<Implicant> impls = new ArrayList<>();
        
        int[] selectedRows = implicantsTable.getSelectedRows();
        
        for(int i=0; i<selectedRows.length; i++){
            impls.add((Implicant) implicantsTable.getValueAt(selectedRows[i], 0));
        }        
        
        return impls;
    }
    
    private void setColumnWidths() {
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = implicantsTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(90);
            } else if (i == 1){
                column.setPreferredWidth(50);
            } else {
                column.setPreferredWidth(30);
            }
        }        
    }
    
    private class ImplicantsTableModel extends AbstractTableModel{
        
        private static final long serialVersionUID = -952640114295250244L;
        
        private String[] columnNames;
        private Implicant[] implicants;
        
        public ImplicantsTableModel(String[] columnNames, Implicant[] implicants){
            this.columnNames = columnNames;
            this.implicants = implicants;
        }    
        
        
        @Override
        public boolean isCellEditable(int row, int col){ 
            return false; 
        }

        @Override
        public int getRowCount() {
            return implicants == null ? 0 : implicants.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Implicant impl = implicants[rowIndex];
            if(columnIndex == 0 ) return impl;
            else if(columnIndex == 1) return Arrays.toString(impl.getMinterms());
            else if(columnIndex == 2) return getString(impl.isDontCare());
            else if(columnIndex == 3) return getString(impl.isPrime());
            else if(columnIndex == 4) return getString(impl.isEssential());
            return "";
        }

        private String getString(boolean bool) {
            return bool ? "DA" : "NE";
        }
        
    }
 }
