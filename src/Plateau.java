import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


class Plateau {
	private CaseNonGraphique [][] plateau;
	private int longueur;
	private int hauteur;
	private int nbr_pions_par_joueurs;
	private String coup_joue;

	public Plateau () {
		this.longueur = 6;
		this.hauteur = 6;
		this.nbr_pions_par_joueurs = 8;
		genere_plateau();
	}

	public Plateau (int longueur, int hauteur) 
			throws BoardGenerationException {
		if (longueur<1 || longueur>26) {
			throw new BoardGenerationException("Impossible "
			+"de generer un plateau de plus de 26 cases de "
			+"longueur.");
		}
		if (hauteur<4) {
			throw new BoardGenerationException("Impossible "
			+"de generer un plateau de moins de 4 lignes de "
			+"hauteur, \ncar chaque joueur dispose de deux "
			+"lignes de fantomes.");
		}
		this.longueur = longueur;
		this.hauteur = hauteur;
		this.nbr_pions_par_joueurs = 2*(longueur-2);
		genere_plateau();
	}

// ########################## Generation du plateau ########################## //

	public void genere_plateau () {
		int i, j;

		plateau = new CaseNonGraphique [hauteur][];
		for (i=0; i<plateau.length; i++) {
			plateau[i] = new CaseNonGraphique[longueur];
		}
		for (i=0; i<plateau.length; i++) {
			for (j=0; j<plateau[i].length; j++) {
				plateau[i][j] = new CaseNonGraphique();
			}
		}
		// ###### Initialisation des cases de sortie ################ //
		plateau[0][0].setCase_sortie(true, false);
		plateau[0][plateau[0].length-1].setCase_sortie(true, false);
		plateau[plateau.length-1][0].setCase_sortie(true, true);
		plateau[plateau.length-1][plateau[plateau.length-1]
				.length-1].setCase_sortie(true, true);
	}

// ####################### Déplacements de fantômes ######################## //

	/**
		Effectue le deplacement d'un fantôme entre la 
		position 1 et la position 2.
	**/
	public void deplace_fantome (String position1, String position2) {
		int [] tmpCaseInitiale = ConvertString_Coordonnees(position1);
		int [] tmpCaseFinale = ConvertString_Coordonnees(position2);
		plateau[tmpCaseFinale[0]][tmpCaseFinale[1]].
		place_fantome(plateau[tmpCaseInitiale[0]][tmpCaseInitiale[1]]
		.getGhost());
		plateau[tmpCaseInitiale[0]][tmpCaseInitiale[1]].
		retire_fantome();
		coup_joue=position1+" , "+position2;

		CaseNonGraphique caseInit = plateau[tmpCaseInitiale[0]][tmpCaseInitiale[1]];
		CaseNonGraphique caseFin = plateau[tmpCaseFinale[0]][tmpCaseFinale[1]];

		/**aff("");
		if (caseInit.getCase_sortie()) {
			aff("Case initiale");
			caseInit.affiche_info();
		}
		if (caseFin.getCase_sortie()) {
			aff("Case finale");
			caseFin.affiche_info();
		}**/
	}

// ####################### Entre une case ################################### //

	public String Entre_une_case_aux (ArrayList<String> positions_possibles, 
			boolean initial) throws NullPositionException {
		if (positions_possibles.isEmpty()) 
			throw new NullPositionException();
		Scanner sc = new Scanner(System.in);
		if (initial) aff("\nChoisissez l'une des cases restantes : ");
		else {
			affnn("Entrez les coordonees d'un pion de ce joueur");
			aff(" parmi les coordonees suivantes : ");
		}
		int tmp0=0;
		for (String s0 : positions_possibles) {
			affnn(s0);
			if (tmp0<positions_possibles.size()-1)
				affnn(", ");
			tmp0++;
		}
		affnn(" : \n? = ");
		return (sc.nextLine());
	}

	/**
		Permet à l'utilisateur d'entrer un case en fonction
		du tableau de cases possibles passé en paramètres.
	**/
	public String Entre_une_case (ArrayList<String> positions_possibles, 
			boolean retire, boolean initial) 
			throws NullPositionException {
		if (positions_possibles.isEmpty()) 
			throw new NullPositionException();
		Scanner sc = new Scanner(System.in);
		String rep="";
		boolean ok=false;
		while (!ok) {
			rep = Entre_une_case_aux(positions_possibles, initial);
			if (positions_possibles.contains(rep)) {
				if (retire) positions_possibles.remove(rep);
				ok=true;
			}
		}
		return rep;
	}

	public String Entre_une_case (ArrayList<String> positions_possibles, 
			boolean initial) {
		return Entre_une_case(positions_possibles, true, initial);
	}

	public String entre_case_ordi (ArrayList<String> positions_possibles) {
		if (positions_possibles==null) return "";
		if (positions_possibles.isEmpty()) return "";
		int tmp = randInt(0, positions_possibles.size()-1);
		return positions_possibles.get(tmp);
	}

// ############################# Mode triche ################################ //

	public void mode_triche_active (Jeu jeu) {
		ArrayList<String> AL1;
		AL1 = getCoordonnes_pions(jeu.getJoueur(true));
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(true);
		}
		AL1 = getCoordonnes_pions(jeu.getJoueur(false));
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(true);
		}
	}

	public void mode_triche_desactive (Jeu jeu, Joueur joueur) {
		ArrayList<String> AL1;
		AL1 = getCoordonnes_pions(joueur);
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(true);
		}
		AL1 = getCoordonnes_pions(jeu.getJoueur(!joueur.getJoueur1_2()));
		for (String s1 : AL1) {
			getCase(s1).setFantome_visible(false);
		}
	}

// ######################### Fonctions toString ############################# //

	public String toString () {
		int i,j;
		String res="";

		res+="\n   ";
		for (i=0; i<plateau[0].length; i++) {
			res+="  "+(char)('A'+i)+"    ";
		}
		res+="\n\n";
		for (i=0; i<plateau.length; i++) {
			res+=""+(plateau.length-i)+"  ";
			for(j=0; j<plateau[i].length; j++) {
				res+=plateau[i][j]+"  ";
			}
			if (i<plateau.length-1) res+="\n\n";
		}
		res+="\n\n";

		return res;
	}

// ######################## Getteurs et Setteurs ########################### //

	public int getLongueur () {
		return longueur;
	}
	public int getHauteur () {
		return hauteur;
	}
	public int getNbr_pions_par_joueurs () {
		return nbr_pions_par_joueurs;
	}
	public CaseNonGraphique [][] getPlateau () {
		return plateau;
	}
	public String getCoup_joue(){
		return coup_joue;
	}
	public void setPlateau (CaseNonGraphique[][] plateau) {
		this.plateau = plateau;
	}
	public Plateau clone() {
		return this;
	}
	/*public void setCase (CaseNonGraphique case, int i, int j) {
		if (i<0 || i>=hauteur) return null;
		if (j<0 || j>=longueur) return null;
		plateau[i][j] = case;
	}*/
	public CaseNonGraphique getCase (int i, int j) {
		if (i<0 || i>=hauteur) return null;
		if (j<0 || j>=longueur) return null;
		return plateau[i][j];
	}
	public CaseNonGraphique getCase (String case1) {
		int [] tmp = ConvertString_Coordonnees(case1);
		if (tmp==null) return null;
		if (tmp.length!=2) return null;
		int i = tmp[0], j = tmp[1];
		if (i<0 || i>=plateau.length 
			|| j<0 || j>=plateau[0].length) 
				return null;
		return getCase(i, j);
	}
	public void setMode_triche (Jeu jeu, boolean mode_triche_tmp) {
		if (mode_triche_tmp)
			mode_triche_active(jeu);
	}

// ######################## Fonctions utilitaires ######################### //

	public ArrayList<String> getCoordonnes_pions (Joueur jtmp) {
		ArrayList<String> res = new ArrayList<String>();
		boolean j1_2 = jtmp.getJoueur1_2();

		for (int i=0; i<plateau.length; i++) {
			for (int j=0; j<plateau[i].length; j++) {
				CaseNonGraphique tmpC = plateau[i][j];
				if (tmpC.fantome_present() &&
					tmpC.getJoueur1_2()==j1_2) {
					res.add(""+convert_colonne_lettre(j)+
						convert_chiffre_ligne(i));
				}
			}
		}
		return res;
	}

	/**
		Supprime tou les fantômes du plateau.
	**/
	public void clear () {
		for (int i=0; i<plateau.length; i++) {
			for (int j=0; j<plateau[i].length; j++) {
				plateau[i][j].retire_fantome();
			}
		}
	}

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

// #*************** Fonctions utiles pour le plateau initial ****************# //

	/**
		Convertit une ligne en numéro de case dans le plateau ou le 
		contraire (opération d'inversion).
	**/
	public int convert_chiffre_ligne (int n1) {
		CaseNonGraphique [][] plateau = getPlateau();
		return plateau.length-n1;
	}

	public char convert_colonne_lettre (int n0) {
		return (char)('a'+n0);
	}

	public int convert_lettre_colonne (char cc) {
		CaseNonGraphique [][] plateau = getPlateau();
		char aa = 'a';
		for (int i=0; i<plateau.length; i++) {
			if (aa==cc) {
				return i;
			}
			aa++;
		}
		return -1;
	}

	public String ConvertCoordonnees_String (int i, int j) {
		return (""+convert_colonne_lettre(j)+
				convert_chiffre_ligne(i));
	}

	public int [] ConvertString_Coordonnees (String position) {
		int [] res = new int[2];
		res[0] = convert_chiffre_ligne(
				Integer.parseInt(position.substring(1, 
					position.length())));
		res[1] = convert_lettre_colonne(position.charAt(0));

		return res;
	}

	public static int randInt (int min, int max) {
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}
}
