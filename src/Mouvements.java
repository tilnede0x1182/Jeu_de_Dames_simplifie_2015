import java.util.List;
import java.util.ArrayList;

public class Mouvements extends Plateau {
	Plateau plateauP;
	CaseNonGraphique [][] plateau;

	public Mouvements (Plateau plateau) {
		plateauP = plateau;
		this.plateau = plateauP.getPlateau();
	}

// #################### Calcul des mouvements possibles ##################### //

	/**
		Retourne un tableau de booléens.
		@see Mouvements#cases_possibles
	**/
	public boolean [] teste_case (int i, int j, boolean joueur1_2) {
		boolean [] res = new boolean[4];

		if (i<0 || i>plateau.length || j<0 || j>plateau[0].length)
			return res;
		// #### Gauche #### //
		if (!(j-1<0)) {
			if (!plateau[i][j-1].fantome_joueur_present(joueur1_2))
				res[0] = true;
		}

		// #### Droite ##### //
		if (!(j+1>=plateau[0].length)) {
			if (!plateau[i][j+1].fantome_joueur_present(joueur1_2))
				res[1] = true;
		}

		// #### Bas #### //
		if (!(i+1>=plateau.length)) {
			if (!plateau[i+1][j].
				fantome_joueur_present(joueur1_2)) 
					res[2] = true;
		}

		// #### Haut #### //
		if (!(i-1<0)) {
			if (!plateau[i-1][j].
				fantome_joueur_present(joueur1_2)) 
					res[3] = true;
		}

		return res;
	}

	/**
		boolean [] cases_possibles[4] : <br>
			cases_possibles[0] : gauche <br>
			cases_possibles[1] : droite <br>
			cases_possibles[2] : bas <br>
			cases_possibles[3] : haut
	**/
	public ArrayList<String> cases_possibles (String position, 
			boolean joueur1_2) {
		ArrayList<String> res = new ArrayList<String>();
		int [] tmp1 = ConvertString_Coordonnees(position);
		int ii = tmp1[0];
		int jj = tmp1[1];
		boolean [] cases_possibles = teste_case(ii, jj, joueur1_2);

		if (cases_possibles[0]) 
			res.add(ConvertCoordonnees_String(ii, jj-1));
		if (cases_possibles[1]) 
			res.add(ConvertCoordonnees_String(ii, jj+1));
		if (cases_possibles[2]) 
			res.add(ConvertCoordonnees_String(ii+1, jj));
		if (cases_possibles[3]) 
			res.add(ConvertCoordonnees_String(ii-1, jj));
		return res;
	}

// ################### Fonctions utilitaires ################################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	public void afftab (Object [] tab, String nom) {
		for (int i=0; i<tab.length; i++) {
			aff(nom+"["+i+"] = "+tab[i]);
		}
	}

	public void afftab (Object [] tab) {
		for (int i=0; i<tab.length; i++) {
			aff("tab["+i+"] = "+tab[i]);
		}
	}

	public void setPlateau (Plateau plateau) {
		plateauP = plateau;
		this.plateau = plateau.getPlateau();
	}
}
