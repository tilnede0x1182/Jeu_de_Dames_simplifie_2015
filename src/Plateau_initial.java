import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class Plateau_initial extends Plateau {
	Joueur joueur;
	int nbr_bon_fantomes = 0;
	int nbr_mauvais_fantomes = 0;
	boolean fantome_plus=true;

	public Plateau_initial (CaseNonGraphique [][] plateau, Joueur jtmp,
			boolean choix_pions_auto) {
		joueur = jtmp;
		fantome_plus = randboolean();
		setPlateau(plateau);
		if (choix_pions_auto) place_fantomes(false);
		else place_fantomes(joueur.getJoueur_humain_ordinateur());
	}

// ################## Positions initialles ################################### //

	/**
		Retourne un ArrayList des positions possibles selon le joueur : 
	
		j1 : b6-e5
		j2 : b2-e1
	**/
	public ArrayList<String> positions_possibles () {
		ArrayList<String> res = new ArrayList<String>();
		char i='b';
		int j=0;
		CaseNonGraphique [][] plateau = getPlateau();
		boolean j1_j2 = getJoueur1_2();

		if (j1_j2) {
			for (i='b'; i<'f'; i++) {
				for (j=6; j>4; j--) {
					res.add(""+(char)(i)+""+j);
				}
			}
		}
		else {
			for (i='b'; i<'f'; i++) {
				for (j=2; j>0; j--) {
					res.add(""+(char)(i)+""+j);
				}
			}
		}

		/*for (String s : res) {
			aff(s);
		}*/

		return res;
	}

// ################# Sauvegarde des couops initiaux ######################### //
	
	public String[] sauve_coups_initiaux (Plateau plateauP) {
		int i,j;
		String [] res = new String[2];
		CaseNonGraphique [][] plateau = plateauP.getPlateau();
		String tmp = "";
		char tmpC = '0';
		
		for (i=0; i<2; i++){
			for (j=1; j<plateau[i].length-1; j++) {
				tmp+=""+code_fatome(plateau[i][j]);
				if (j<plateau[i].length-2) tmp+=" ";
			}
			if (i<2-1) tmp+="\n";
		}
		res[0] = tmp;
		tmp="";
		for (i=plateau.length-2; i<plateau.length; i++){
			for (j=1; j<plateau[i].length-1; j++) {
				tmp+=""+code_fatome(plateau[i][j]);
				if (j<plateau[i].length-2) tmp+=" ";
			}
			if (i<plateau.length-1) tmp+="\n";
		}
		res[1] = tmp;

		return res;
	}

// ###################### Choisis fantôme ################################### //

	/**
		@see Plateau_initial#choisis_fantome
	**/
	public boolean choisis_fantome_humain () {
		int res=0;
		while (res<1 || res>2) {
			aff("Le fantome est :\n1 : bon\n2 : mauvais");
			res = entrer_entier();
		}
		return ((res==1)?true:false);
	}

	/**
		@see Plateau_initial#choisis_fantome
	**/
	public boolean choisis_fantome_ordi () {
		return randboolean();
	}

	/**
		ordi_humain:<br>
			true : humain<br>
			false : ordinateur<br><br>

		Retourne un boolean qui détermine <br>
		si le fantome est bon ou mauvais : <br>
			true : bon<br>
			false : mauvais<br><br>

		S'il reste un fantome à placer et
		que le nombre de fantome max est impair, 
		la nature du fantôme est attribuée aléatoirement 
		(la valeur sera la même pour les deux joueurs par 
		soucis d'égalité).
	**/
	public boolean choisis_fantome_aux (boolean ordi_humain) {
		int nbppj = super.getNbr_pions_par_joueurs();
		if (nbppj%2!=0) {
			if (nbr_bon_fantomes==nbppj
				&& nbr_mauvais_fantomes==nbppj/2-1)
			return false;
			if (nbr_mauvais_fantomes==nbppj
				&& nbr_bon_fantomes==nbppj/2-1)
			return true;
		}
		else {
			if (nbr_bon_fantomes==nbppj/2)
				return false;
			if (nbr_mauvais_fantomes==nbppj/2)
				return true;
		}

		if (ordi_humain) return choisis_fantome_humain();
		else {
			return choisis_fantome_ordi();
		}
	}

	public boolean choisis_fantome (boolean ordi_humain) {
		boolean res = choisis_fantome_aux(ordi_humain);
		if (res) nbr_bon_fantomes++;
		else nbr_mauvais_fantomes++;

		return res;
	}

// ############################ Place fanôme ################################## //

	/**
		ordi_humain:
			true : humain
			false : ordinateur
	**/
	public void place_fantomes (boolean ordi_humain) {
		if (ordi_humain) {
			aff("C'est a "+joueur+" de placer ses fantomes :");
			place_fantomes_humain();
		}
		else {
			place_fantomes_ordi();
			aff("Le joueur "+joueur+" a place ses fantomes.");
		}
		joueur.setNbr_fantomes_bons(nbr_bon_fantomes);
		joueur.setNbr_fantomes_mauvais(nbr_mauvais_fantomes);
	}

	public void place_fantomes_ordi () {
		CaseNonGraphique [][] plateau = super.getPlateau();
		ArrayList<String> positions_possibles = positions_possibles();

		for (String position : positions_possibles) {
			int [] tmp1 = ConvertString_Coordonnees(position);
			int ii = tmp1[0];
			int jj = tmp1[1];
			boolean btmp = choisis_fantome(false);
			Ghost ghosttmp = new Ghost (joueur, btmp);
			if (jj!=-1) plateau[ii][jj].ajoute_fantome(ghosttmp);	
		}
	}

	public void place_fantomes_humain () {
		int i;
		CaseNonGraphique [][] plateau = super.getPlateau();
		ArrayList<String> positions_possibles = positions_possibles();
		int places = positions_possibles.size();

		for (i=0; i<places; i++) {
			String position = 
				super.Entre_une_case(positions_possibles, true);
			int [] tmp1 = ConvertString_Coordonnees(position);
			int ii = tmp1[0];
			int jj = tmp1[1];
			boolean btmp = choisis_fantome(true);
			Ghost ghosttmp = new Ghost (joueur, btmp);
			if (jj!=-1) plateau[ii][jj].ajoute_fantome(ghosttmp);
		}
	}
	
// ######################## Getteurs et Setteurs ########################### //

	public boolean getJoueur1_2 () {
		return joueur.getJoueur1_2();
	}
	public int getNbr_bon_fantomes () {
		return nbr_bon_fantomes;
	}
	public int getNbr_mauvais_fantomes () {
		return nbr_mauvais_fantomes;
	}
	public int getNbr_fantomes () {
		return nbr_bon_fantomes+nbr_mauvais_fantomes;
	}
	public CaseNonGraphique[][] getPlateau () {
		return super.getPlateau();
	}

	public void setPlateau (CaseNonGraphique[][] plateau) {
		super.setPlateau(plateau);
	}


// ##################### Fonctions utilitaires ############################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	/**
		Returns true if s1 is an Integer.
		Manages the case where s1 is empty.
	**/
	public boolean isInteger (String s1) {
		try {
			Integer.parseInt(s1);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	public int entrer_entier () {
		affnn("? = ");
		Scanner sc = new Scanner(System.in);
		String res = "";

		res = sc.nextLine();
		while (!isInteger(res)) {
			aff("Veuillez entrer en entier : ");
			affnn("? = ");
			res = sc.nextLine();
		}

		return Integer.parseInt(res);
	}

	public static boolean randboolean () {
		int tmp = randInt(0, 49);
		return ((tmp<25)?true:false);
	}

	public static int randInt (int min, int max) {
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}

// ##***************** Fonctions utilitaires spécialisées ****************## //

	public char code_fatome (CaseNonGraphique case0) {
		Ghost g1 = case0.getGhost();
		if (g1==null) return 'N';
		if (g1.getBon_mauvais()) return 'G';
		else return 'B';
	}
}