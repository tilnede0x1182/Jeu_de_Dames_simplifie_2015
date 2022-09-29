import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class Tour_de_jeu extends Plateau {
	private Plateau plateau;

	public Tour_de_jeu(Plateau plateau) {
		this.plateau = plateau;
	}

// ########################## Tour de jeu non graphique ######################################## //

	public void joue_humain (Joueur joueur, Mouvements mouv) {
		String case_finale = "", case_initiale = "";
		Case [][] plateau_tmp = plateau.getPlateau();
		ArrayList<String> tmpAL=null, tmpAL2=null;
		tmpAL = plateau.getCoordonnes_pions(joueur);
		boolean ok = false;
		while (!ok) {
			try {
				case_initiale = 
				plateau.Entre_une_case(tmpAL, false, false);
			} catch (NullPositionException e){ aff(e.getMessage()); }
			tmpAL2 = mouv.cases_possibles(
				case_initiale, joueur.getJoueur1_2());
			aff("Case de destination : ");
			try {
			case_finale  = 
				plateau.Entre_une_case_aux(tmpAL2, false);
			} catch (NullPositionException e){ aff(e.getMessage()); }
			ok = tmpAL2.contains(case_finale);
		}
		plateau.deplace_fantome(case_initiale, case_finale);
	}

	public String [] joue_ordi_aux (Joueur joueur, Mouvements mouv, boolean graphique) {
		String case_finale = "", case_initiale = "";
		ArrayList<String> tmpAL=null, tmpAL2=null;
		tmpAL = plateau.getCoordonnes_pions(joueur);
		case_initiale = ""; case_finale = "";
		while (case_initiale.isEmpty() || case_finale.isEmpty()) {
			case_initiale = plateau.entre_case_ordi(tmpAL);
			tmpAL2 = mouv.cases_possibles(case_initiale, 
				joueur.getJoueur1_2());
			case_finale = plateau.entre_case_ordi(tmpAL2);
			if (graphique) aff("Case initiale = "+case_initiale);
			if (graphique) aff("Case finale = "+case_finale);
		}
		String [] res = new String[2];
		res[0] = case_initiale; res[1] = case_finale;
		return res;
	}

	public void joue_ordi (Joueur joueur, Mouvements mouv, boolean graphique) {
		String [] tmp = joue_ordi_aux(joueur, mouv, graphique);
		if (tmp==null) return;
		if (tmp.length!=2) return;
		for (int i=0; i<tmp.length; i++) {
			if (tmp[i]==null) return;
			if (tmp[i].isEmpty()) return;
		}
		// gestion des erreurs potentielles.

		plateau.deplace_fantome(tmp[0], tmp[1]);
	}

	public void joue (Joueur joueur, Mouvements mouv) {
		if (joueur.getJoueur_humain_ordinateur())
			joue_humain(joueur, mouv);
		else
			joue_ordi(joueur, mouv, false);
	}

// ############################## Tour de jeu graphique ######################################## //

	/**
		Retourne :<br>
		0 en cas de correspondance<br>
		-1 si la case 2 est incorrecte<br>
		-2 si la case 1 est incorrecte
	**/
	public int tour_de_jeu_graphique (Joueur joueur, Mouvements mouv, 
			String case1, String case2, boolean tour) {
		ArrayList<String> tmpAL = null, tmpAL2 = null;

		//aff(""+joueur);
		//aff("tourB = "+tour);

		tmpAL = plateau.getCoordonnes_pions(joueur);
		if (tmpAL.contains(case1)) {
			if (tour) return 0;
		}
		else return -2;
		if (!tour) {
			tmpAL2 = mouv.cases_possibles(case1, 
				joueur.getJoueur1_2());
			if (tmpAL2.contains(case2)) return 0;
			else return -1;
		}
		return 0;
	}

// ################### Fonctions utilitaires ################################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}
}
