import java.awt.Color;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Controleur implements ActionListener, MouseListener {
	private boolean utilisation_menu;
	private Fenetre fenetre;
	private int itemI = 0;
	private Main main = null;
	private Jeu jeu = null;
	private int autre_case=0;
	private CaseGraphique case0;
	private boolean selection_initiale;
	private JMenuItem [] items = null;
	private CaseGraphique [] selection_initialeT;
	private CaseGraphique [][] cases;

	public Controleur (Fenetre fenetre, int itemI) {
		utilisation_menu = true;
		this.fenetre = fenetre;
		initilaise_main();
		if (itemI>-1 && itemI<items.length)
			this.itemI = itemI;
	}

	public Controleur (Fenetre fenetre, Plateau plateau, 
			CaseGraphique [][] cases, CaseGraphique case0) {
		this.fenetre = fenetre;
		this.selection_initiale = false;	
		this.case0 = case0;
		this.cases = cases;
		initilaise_main();
	}
	
	public Controleur (CaseGraphique [] selection_initialeT, 
			CaseGraphique case0) {
		this.selection_initiale = true;
		this.case0 = case0;
		this.selection_initialeT = selection_initialeT;
		if (case0==selection_initialeT[0])
			autre_case = 1;
		initilaise_main();
	}

	public void initilaise_main () {
		if (fenetre!=null) {
			main = fenetre.getMain();
			items = fenetre.getItems();
			jeu = main.getJeu();
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
	}
	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent event) {
		if (selection_initiale) {
			case0.select_deselect();
			selection_initialeT[autre_case].select_deselect();
			for (int i = 0; i<2; i++) {
				CaseGraphique casetmp = selection_initialeT[i];
				if (casetmp.isSelectionne()) {
					if (casetmp.getCouleur_du_fantome()
					.equals(Color.red))
						aff("Fantôme rouge");
					else 
						aff("Fantôme bleu");
				}
			}
		}
		else {
			//case0.affiche_adresse();
			/*for (int i=0; i<cases.length; i++) {
				for (int j=0; j<cases[i].length; j++) {
					if (cases[i][j].isSelectionne() 
					&& cases[i][j]!=case0) {
						cases[i][j].deselect();
					}
				}
			}*/
			int [] coord_tmp = case0.getAdresse_CaseGraphique();
			if (case0.isSelectionne()) case0.deselect();
			else {
				jeu_graphique(case0);
				//case0.select();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent event) {
	}

// ########################### Jeu graphique ################################# //

	public void jeu_graphique (CaseGraphique case1) {
		fenetre.jeu_graphique(case1.getPosition());
	}

// ############## Mise à jour d'une caseGraphique ############################ //

	public void mise_a_jour_cases () {
		fenetre.mise_a_jour_cases();
	}

// ##################### Getteurs et setteurs ################################ //

// ##################### Fonctions utilitaires ############################### //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (utilisation_menu) {
			jeu = main.getJeu();
			if (itemI==0) { // iNouveauJeu
				fenetre.dispose();
				main.setJeu(new Jeu(main, true, true, false, false, true));
				while (main.getJeu_non_graphique()) main.jeu();
			}
			if (itemI==1) { // iOuvrirPartie
				main.relecture();
				while (main.getJeu_non_graphique()) main.jeu();
			}
			if (itemI==2) { // iEnregistrerPartie
				jeu.setFichier_ouvert(jeu.getSauv()
				.enregistrer(jeu.getFichier_ouvert(), jeu.getListe_de_coups(), jeu));
			}
			if (itemI==3) { // iEnregistrerPartieSous
				jeu.setFichier_ouvert(jeu.getSauv()
				.enregistrer("", jeu.getListe_de_coups(), jeu));
			}
			if (itemI==4) { // iMode_triche
				jeu.active_desactive_mode_triche();
			}
			if (itemI==5) { // iQuitter
				System.exit(0);
			}
		}
		//fenetre.setInitial_normal();
	}
}
