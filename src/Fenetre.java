import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.event.WindowEvent;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;

public class Fenetre extends JFrame {
	int i, j;
	private int hauteur = 6;
	private int largeur = 6;
	private boolean initialB = false;
	
	private char sep = File.separatorChar;
	//private String images = ".."+sep+"images"+sep;
	private String images = ".."+sep+".."+sep
			+"version de demonstration"+sep+"images"+sep;
	private Plateau plateau;
	private CaseGraphique [][] cases;
	private CaseGraphique [] selection_initiale;
	private Controleur [][] controleur;
	private Controleur [] controleur_CaseGraphique_initiale;
	private JMenuItem [] items = null;
	private Jeu jeu;

	private JMenuBar menuBar;
	private JMenu mJeu;
	private JMenuItem iNouveauJeu;
	private JMenuItem iOuvrirPartie;
	private JMenuItem iEnregistrerPartie;
	private JMenuItem iEnregistrerPartieSous;
	private JMenuItem iMode_triche;
	private JMenuItem iQuitter;
	
	private GridLayout g2;
	
	private JPanel p1;
	private JPanel p2;
	private JPanel p3;
	private JPanel p4;
	private JPanel p5;

	private JLabel lb_errors;
	private ArrayList<String> liste_derreurs;

	private WindowAdapter wa;
	
	public Fenetre (Plateau plateau, Jeu jeu) {
		super("Jeu des fantômes");
		//#* Instanciation des champs *#//
		this.jeu = jeu;
		this.plateau = plateau;
		lb_errors = new JLabel();
		g2 = new GridLayout(1, 1);

		//#* Instanciation des panels *#//
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		p5 = new JPanel();
		p4.add(lb_errors);
		p5.setLayout(g2);
		p5.add(p4);

		//#* Mise en place de la bande de messages *#//
		liste_derreurs = new ArrayList<>();
		selection_initiale = new CaseGraphique [2];
		cases = new CaseGraphique [hauteur][];
		controleur = new Controleur[hauteur][];
		controleur_CaseGraphique_initiale = new Controleur[2];
		GridLayout g1 = new GridLayout(hauteur, largeur);

		//#* Mise en place du panel de sélection initiale *#//
		p3.setLayout(g1);
		BorderLayout bd1 = new BorderLayout();
		p1.setLayout(bd1);
		p1.add(p2, BorderLayout.NORTH);
		p1.add(p3, BorderLayout.SOUTH);

		//#* Mise ne place du plateau de sélection initiale *#//
		for (i=0; i<2; i++) {
			selection_initiale[i] = new CaseGraphique(plateau, i, 0);
			controleur_CaseGraphique_initiale[i] = new Controleur(
				selection_initiale, selection_initiale[i]);
			selection_initiale[i].
				setCaseGraphique_selection_initiale(true);
			selection_initiale[i].setPreferredSize(new Dimension(30, 
				30));
			selection_initiale[i].addMouseListener(
				controleur_CaseGraphique_initiale[i]);
			p2.add(selection_initiale[i]);
		}

		//#* Mise en place du panel de jeu *#//
		selection_initiale[0].setCouleur_du_fantome(Color.red);
		selection_initiale[0].select();
		selection_initiale[1].setCouleur_du_fantome(Color.blue);

		//#* Mise ne place du plateau de jeu graphique *#//
		for (i=0; i<hauteur; i++) {
			cases[i] = new CaseGraphique[hauteur];
			controleur[i] = new Controleur[hauteur];
			for (j=0; j<largeur; j++) {
				cases[i][j] = new CaseGraphique(plateau, i, j);
				cases[i][j].setBackground(Color.WHITE);
				controleur[i][j] = new Controleur(this, 
					plateau, cases, 
					cases[i][j]);
				cases[i][j].addMouseListener(
					controleur[i][j]);
				p3.add(cases[i][j]);
			}
		}
		p1.add(p5, BorderLayout.NORTH);
		p1.add(p3, BorderLayout.CENTER);
		this.add(p1);
		
		//#* Initialisation de la barre de menu *#//
		menuBar = new JMenuBar();
		mJeu = new JMenu("Jeu");
		mJeu.setMnemonic('j');
		//mEdition = new JMenu("Edition");

		//#* Initialisation et ajout des boutons dans la barre de menus *#//
		iNouveauJeu = new JMenuItem("Nouveau jeu");
		iOuvrirPartie = new JMenuItem("Ouvrir une patie");
		iEnregistrerPartie = new JMenuItem("Enregistrer une partie");
		iEnregistrerPartieSous = new JMenuItem(
			"Enregistrer une partie sous");
		iMode_triche = new JMenuItem("Mode triche : activer/désactiver");
		iQuitter = new JMenuItem("Quitter");
		construit_tab_items();
		iNouveauJeu.setAccelerator(KeyStroke.getKeyStroke
			(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		iOuvrirPartie.setAccelerator(KeyStroke.getKeyStroke
			(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		iEnregistrerPartie.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		iMode_triche.setAccelerator(KeyStroke.getKeyStroke
			(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
		initialise_JMenuItems();
		
		//#* Ajout des boutons à la barre de menus *#//
		this.menuBar.add(mJeu);
		//this.menuBar.add(mEdition);
		//#* Ajout de la barre de menus à la fenêtre *#//
		this.setJMenuBar(menuBar);
		
		//#* Mise en place de l'icone de la fenêtre *#//
		try {
			this.setIconImage(new ImageIcon(images+"Logo1.png").getImage());
		}
		catch (Exception e) {
			aff("Impossible de charger l'image d'icone de la fenetre.");
		}

		/**
			Actions à effectuer lorsque l'on 
			ferme la fenetre de jeu.
		**/
		final Jeu jeuFinal = jeu;
		wa = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				jeuFinal.reinitialise_main();
			}
		};
		addWindowListener(wa);

		/**
			Réglage de la taille, de la localisation  des 
			options de fermeture et affichage de la fenêtre
		**/
		this.setMinimumSize(new Dimension(300, 336));
		//this.setPreferredSize(new Dimension(640, 480));
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mise_a_jour_cases();
		revalidate();
		this.setVisible(true);
	}

// ########################## Menu graphiques ############################### //


	/**
		Recherche un JMenuItem dans le tableau donné par la fonction 
		getItems(). <br>
		Retourne -1 si ne trouve pas. <br>
		@see Fenetre#getItems
	**/
	public int recherche_item (JMenuItem tmp) {
		JMenuItem [] tab = getItems();
		for (int i = 0; i<tab.length; i++) {
			if (tab[i]==tmp) return i;
		}
		return -1;
	}

	public void initialise_controleur_JMenuItem (JMenuItem tmp) {
		tmp.addActionListener(new Controleur(this, recherche_item(tmp)));
	}

	public void initialise_JMenuItems () {
		for (int i=0; i<items.length; i++) {
			initialise_controleur_JMenuItem(items[i]);
			this.mJeu.add(items[i]);
		}
	}

// ######################### Selection initiale ############################# //

	public void setInitial_normal () {
		if (initialB) setNormal();
		else setInitial();
	}
	
	public void setInitial () {
		initialB = true;
		g2 = new GridLayout(2, 1);
		p5.setLayout(g2);
		p5.add(p2);
		revalidate();
		repaint();
	}

	public void setNormal () {
		initialB = false;
		p5.remove(p2);
		g2 = new GridLayout(1, 1);
		p5.setLayout(g2);
		revalidate();
		repaint();
	}

// ############## Afficher une erreure dans le Label ####################### //

	/**
		Ajoute le message message dans le label prévus à cet effet.
	**/
	public void add_message (String message) {
		liste_derreurs.add(message);
		met_a_jour_lb_errors();
	}

	/**
		Enlève le message message du label prévus à cet effet.
	**/
	public void remove_message (String message) {
		liste_derreurs.remove(message);
		met_a_jour_lb_errors();
	}

	public void remove_all_messages () {
		liste_derreurs.clear();
		met_a_jour_lb_errors();
	}

	/**
		Met à jour le label d'affichage des erreures en<br>
		fonction de liste_derreurs.<br>
		@see Fenetre#liste_derreurs
	**/
	public void met_a_jour_lb_errors () {
		// On ajoute le html pour le saut de lignes 
		// (sinon, ça ne marche pas).
		String res = "<html>";
		for (String s1 : liste_derreurs) {
			res+=s1+"<br>";
		}
		res+="</html>";

		lb_errors.setText(res);
		revalidate();
		repaint();
	}

// ########################## Jeu graphique ################################ //

	public void jeu_graphique (String case1) {
		jeu.jeu_graphique(case1);
	}

	public void mode_triche_active () {
		ArrayList<String> AL1;
		AL1 = plateau.getCoordonnes_pions(jeu.getJoueur(true));
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(true);
		}
		AL1 = plateau.getCoordonnes_pions(jeu.getJoueur(false));
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(true);
		}
	}

	public void mode_triche_desactive (Joueur joueur) {
		ArrayList<String> AL1;
		AL1 = plateau.getCoordonnes_pions(joueur);
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(true);
		}
		//aff("joueur : "+joueur);
		//aff("joueur oppposé : "+jeu.getJoueur(!joueur.getJoueur1_2()));
		AL1 = plateau.getCoordonnes_pions(jeu.getJoueur(!joueur.getJoueur1_2()));
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(false);
		}
	}

// ##################### Mise à jour des cases ############################# //

	public void mise_a_jour_cases () {
		for (int i=0; i<cases.length; i++) {
			for (int j=0; j<cases[i].length; j++) {
				cases[i][j].mise_a_jour(plateau.getCase(i, j));
			}
		}
	}

// ####################### Fonctions utilitaires ########################### //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}
	
	public static int randInt (int min, int max) {
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}
	
// ####################### Getteurs et setteurs ############################ //

	public Main getMain () {
		if (jeu==null) return null;
		return jeu.getMain();
	}

	public boolean isInitialB() {
		return initialB;
	}

	public void setInitialB(boolean initialB) {
		if (initialB) setInitial();
		else setNormal();
	}

	public CaseGraphique [][] getPlateau () {
		return cases;
	}

	public CaseGraphique getCase (int i, int j) {
		if (i<0 || i>=cases.length) return null;
		if (j<0 || j>=cases[0].length) return null;
		return cases[i][j];
	}

	public CaseGraphique getCase (String case1) {
		int [] tmp = plateau.ConvertString_Coordonnees(case1);
		if (tmp==null) return null;
		if (tmp.length!=2) return null;
		int i = tmp[0], j = tmp[1];
		if (i<0 || i>=cases.length 
			|| j<0 || j>=cases[0].length) 
				return null;
		return getCase(i, j);
	}

	public void setMode_triche (boolean mode_triche_tmp) {
		if (mode_triche_tmp)
			mode_triche_active();
	}

	public JMenuItem [] getItems () {
		return items;
	}

	public void construit_tab_items () {
		items = new JMenuItem[6];

		items[0] = iNouveauJeu;
		items[1] = iOuvrirPartie;
		items[2] = iEnregistrerPartie;
		items[3] = iEnregistrerPartieSous;
		items[4] = iMode_triche;
		items[5] = iQuitter;
	}
}
