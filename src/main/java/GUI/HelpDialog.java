package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Dijalog koji sadrži objašnjenja o pravilnom načinu zadavanja funkcija.
 * 
 * @author Dunja Vesinger
 * @version 1.0
 *
 */
public class HelpDialog extends JDialog {
    
    private static final long serialVersionUID = -900736723301924006L;

    public HelpDialog(){
        super();        
        setTitle("Način zadavanja Booleovih funkcija");
        setIconImage(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        cp.add(container, BorderLayout.CENTER);
        
        JLabel title = new JLabel("Produkt suma ili suma produkata");
        Font font = title.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        title.setFont(boldFont);
        container.add(title);
        
        container.add(new JLabel("Potrabno je odabrati broj varijabli funkcije koja se zadaje."));
        container.add(new JLabel("Funkcija se zadaje tako da se željeni mintermi/makstermi i don't care polja zadaju "
                + "brojčanim vrijednostima odvojenim zarezom ili razmakom(brojčane vrijednosti kreću od 0)."));
        container.add(new JLabel(" "));
        container.add(new JLabel("Primjer: 0,2,3"));

        
        JLabel titleAlg = new JLabel("Algebarski zapis");
        titleAlg.setFont(boldFont);
        titleAlg.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        container.add(titleAlg);
        
        container.add(new JLabel("Funkcija se zadaje tako da se u polje upiše odgovarajući algebarski izraz."));
        container.add(new JLabel("Izraz može sadržavati operatore I, ILI i NE te oble zagrade, a može se sastojati od 1 do 4 varijable."));
        container.add(new JLabel("Ime varijable može biti bilo koje slovo abecede (velika i mala slova se NE razlikuju)."));
        container.add(new JLabel("Za operator ILI koristi se znak + , za operator NE koristi se "
                + "znak \', a operator I unosi se tako da se između izraza ne stavi nikakav znak."));
        container.add(new JLabel(" "));
        container.add(new JLabel("Primjer: a + bc\' "));
            
    }

}
