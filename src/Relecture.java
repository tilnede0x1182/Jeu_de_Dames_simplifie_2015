import java.io.IOException;
import java.util.ArrayList;

public class Relecture {
	private String erreur_fic_incorrectS = "Le fichier de sauvegarde est incorrect";
	private String erreur_coup_incorrect = "Erreur : le coup est incorrect";
	private Main main = null;

	public Relecture (Main main) {
		this.main = main;
	}

	public Jeu relire (String nom_fichier) {
		return relire(nom_fichier, true);
	}

	/**
		Relire les coups du jeu dans le fichier nom_fichier <br>
		@param nom_fichier, coups
		@see Relecture#relire(String)
	**/
	public Jeu relire (String nom_fichier, boolean affiche_info) 
			throws GameLoadingException  {
		int i,j,k, cmp=0, nbr_lignes_min = 0, commence_analyse = 0;
		String typeJeu = "";
		ArrayList<String> tmpAL=null, tmpAL2=null;
		Jeu jeu = null;
		Joueur joueur = null;
		String [] coups_tmp = null;
		LectureEcriture le = null;

		try {
			le = new LectureEcriture(nom_fichier, 'r');
		}
		catch (IOException e) {
			throw new GameLoadingException(e.getMessage());
		}
		String sauvegardeS = le.lire();
		aff(sauvegardeS);
		String [] sauv = sauvegardeS.split("\n");
		sauv = nettoie_sauv(sauv);
		if (sauv==null) 
			throw new GameLoadingException(erreur_fic_incorrect(0, "sauv est null"));
		if (sauv.length<1)
			throw new GameLoadingException(erreur_fic_incorrect(0, "sauv.length<1"));
		typeJeu = sauv[0];
		nbr_lignes_min = 4;

// ############## Initialisation du jeu restauré ############################ //

		if (verifie_typeJeu(typeJeu)) {
			//aff("type jeu = "+typeJeu);
			nbr_lignes_min = 5;
			commence_analyse = 1;
			jeu = cree_jeu(typeJeu);
		}
		else {
			jeu  = new Jeu(main, true, true, true, false, true);
			// par défaut, on initialise la sauvegarde 
			// a un jeu d'humain contre humain, non-graphique.
			typeJeu = "";
		}
		Plateau plateau = jeu.getPlateau();
		plateau.clear();
		Mouvements mouv = jeu.getMouvements();
		CaseNonGraphique [][] cases = plateau.getPlateau();

// ########################################################################## //

		if (sauv.length<nbr_lignes_min)
			throw new GameLoadingException(erreur_fic_incorrect(0,
			 "sauv.length<nbr_lignes_min : "+sauv.length+"<"+nbr_lignes_min));
		String [] positions_joueurs = new String[4];
		String ligne_tmp="";

		j = 0;
		for (i=commence_analyse; i<4+commence_analyse; i++) {
			ligne_tmp = sauv[i].replace(" ", "");
			if (!verifie_ligne(ligne_tmp))
				throw new GameLoadingException(erreur_fic_incorrectS
					+" a la ligne de sauvegarde "
					+"des positions initiales "+(i+1)+" :\n"
					+'"'+ligne_tmp+'"');
			positions_joueurs[j] = ligne_tmp;
			j++;
		}

		// relecture du plateau initial
		for (i=0; i<2; i++) {
			cmp=0;
			for (j=1; j<cases[i].length-1; j++) {
				cases[i][j].ajoute_fantome(fatome_code(
					positions_joueurs[i].charAt(cmp), jeu.getJoueur(true)));

				cmp++;
			}

		}
		k=2;
		for (i=cases.length-2; i<cases.length; i++) {
			cmp=0;
			for (j=1; j<cases[i].length-1; j++) {
				cases[i][j].ajoute_fantome(fatome_code(
					positions_joueurs[k].charAt(cmp), jeu.getJoueur(false)));
				cmp++;
			}
			k++;
		}

		// relecture des coups
		for (i=4+commence_analyse; i<sauv.length; i++) {
			joueur = jeu.getJoueur(true);
			if (affiche_info) {
				jeu.affiche();
				affiche_coup(coups_tmp, joueur);
			}
			coups_tmp = separe_coups(i, sauv[i], true);
			if (coups_tmp!=null) {
				tmpAL = plateau.getCoordonnes_pions(joueur);
				if (tmpAL.contains(coups_tmp[0])) {
					tmpAL2 = mouv.cases_possibles(coups_tmp[0], true);
					if (tmpAL2.contains(coups_tmp[1])) {
						plateau.deplace_fantome(coups_tmp[0], coups_tmp[1]);
					}
					else 
						throw new GameLoadingException(
							erreur_coup_incorrect(i, sauv[i], 
							getCoup(i, sauv[i], true)));
				}
				else 
					throw new GameLoadingException(
						erreur_coup_incorrect(i, sauv[i], 
						getCoup(i, sauv[i], true)));
				jeu.ajoute_coup(true);
				// joueur 1
			}
			joueur = jeu.getJoueur(false);
			if (affiche_info) {
				jeu.affiche();
				affiche_coup(coups_tmp, joueur);
			}
			coups_tmp = separe_coups(i, sauv[i], false);
			if (coups_tmp!=null) {
				tmpAL = plateau.getCoordonnes_pions(joueur);
				if (tmpAL.contains(coups_tmp[0])) {
					tmpAL2 = mouv.cases_possibles(coups_tmp[0], false);
					if (tmpAL2.contains(coups_tmp[1])) {
						plateau.deplace_fantome(coups_tmp[0], coups_tmp[1]);
					}
					else 
						throw new GameLoadingException(
							erreur_coup_incorrect(i, sauv[i], 
							getCoup(i, sauv[i], false)));
				}
				else 
					throw new GameLoadingException(
						erreur_coup_incorrect(i, sauv[i], 
						getCoup(i, sauv[i], false)));
				jeu.ajoute_coup(false);
				jeu.incremente_numero_coup();
				// joueur 2
			}
		}

		if (!jeu.getJeu_non_graphique()) {
			jeu.met_a_jour_plateau_graphique();
		}
		// si le jeu est graphique

		return jeu;
	}

// ################### Fonctions utilitaires ################################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	public void affTab (String [] s1) {
		affTab(s1, "");
	}

	public void affTab (String [] s1, String nom_tmp) {
		String nom = nom_tmp;
		if (nom_tmp.isEmpty()) nom = "s1";
		for (int i=0; i<s1.length; i++) {
			aff(nom+"["+i+"] = "+s1[i]);
		}
	}

// ##***************** Fonctions utilitaires spécialisées *****************## //

	public void affiche_coup (String [] coup_tmp, Joueur joueur) {
		if (coup_tmp==null) return;
		if (coup_tmp.length!=2) return;
		aff("C'est a "+joueur+" de jouer :");
		aff("Coup : "+coup_tmp[0]+" , "+coup_tmp[1]+" : ");
	}

	/**
		Vérifie si la variable type jeu a le bon format : <br>
			Joueur : <br>
				H : humain <br>
				C : ordinateur (computer) <br>
			Jeu : <br>
				G : graphique <br>
				T : non graphique (terminal)
	**/
	public boolean verifie_typeJeu (String str) {
		int i;

		if (str==null) {
			aff("verifie_typeJeu : str est null");
			return false;
		}
		if (str.length()!=3) {
			aff("verifie_typeJeu : str.length()!=3");
			return false;
		}
		for (i=0; i<2; i++) {
			if (str.charAt(i)!='H'
			&& str.charAt(i)!='C')
				return false;
		}
		if (str.charAt(2)!='G'
		&& str.charAt(2)!='T') return false;
		return true;
	}

	/**
		Crée un jeu en fonction du type de jeu donné 
		en argmument (chaîne de char).
	**/
	public Jeu cree_jeu (String str) {
		if (!verifie_typeJeu(str)) {
			aff("Impossible de creer le jeu car le str est invalide.");
			return null;
		}
		boolean j1B = true;
		if (str.charAt(0)=='C') j1B = false;
		boolean j2B = true;
		if (str.charAt(1)=='C') j2B = false;
		boolean jeu_non_graphique = false;
		if (str.charAt(2)=='T') jeu_non_graphique = true;
		return new Jeu(main, j1B, j2B, jeu_non_graphique, false, true);
	}

	/**
		Retourne un fantôme à partir du code fourni par l'énoncé 
		pour la sauvegarde.
	**/
	public Ghost fatome_code (char c1, Joueur joueur) {
		if (c1 !='G' && c1 != 'B')
			return null;
		boolean bon_mauvais = true;
		if (c1=='B')
			bon_mauvais = false;
		Ghost res = new Ghost(joueur, bon_mauvais);
		return res;
	}

	public String [] nettoie_sauv (String [] sauv) {
		int i,j, cmp=0;
		String [] res = null;

		for (j=0; j<2; j++) {
			cmp=0;
			for (i=0; i<sauv.length; i++) {
				if (sauv[i]!=null &&!sauv[i].isEmpty() 
						&& !sauv[i].contains("#")) {
					if (j==1) {
						res[cmp] = sauv[i];
					}
					cmp++;

				}
			}
			if (j==0) res = new String[cmp];
		}
		return res;
	}

	public boolean verifie_ligne (String ligne) {
		int i, ligne_length = ligne.length();

		for (i=0; i<ligne_length; i++) {
			if (ligne.charAt(i)!='G' 
				&& ligne.charAt(i)!='B')
					return false;
		}
		return true;
	}

	public String get_numero_coup (int numero_ligne, String coup0) {
		String [] coups = coup0.split("-");

		if (coups==null) 
			throw new GameLoadingException(
				erreur_fic_incorrect(numero_ligne, coup0));
		if (coups.length<2)
			throw new GameLoadingException(
				erreur_fic_incorrect(numero_ligne, coup0));
		return coups[0];
	}

	public String getCoup (int numero_coup, String coup0, boolean coup_1_2) 
			throws GameLoadingException {
		String [] coups_tmp = separe_coups(numero_coup, coup0, coup_1_2);
		return coups_tmp[0]+", "+coups_tmp[1];
	}
	
	public String [] separe_coups (int numero_ligne, String coup0, boolean coup_1_2) 
			throws GameLoadingException {
		String [] res = null;

		String [] coups = coup0.split("-");
		if (coups==null) 
			throw new GameLoadingException(
				erreur_fic_incorrect(numero_ligne, coup0));
		if (coups.length<2)
			throw new GameLoadingException(
				erreur_fic_incorrect(numero_ligne, coup0));
		if (coup_1_2) res = coups[1].split(",");
		else {
			if (coups.length==3) res = coups[2].split(",");
			else return null;
		}

		if (res==null) 
			throw new GameLoadingException(
				erreur_fic_incorrect(numero_ligne, coup0));
		if (res.length!=2)
			throw new GameLoadingException(
				erreur_fic_incorrect(numero_ligne, coup0));

		res[0] = res[0].replace(" ", "");
		res[1] = res[1].replace(" ", "");

		return res;
	}

// ######################### Erreurs ########################################### //

	public String erreur_coup_incorrect (int numero_coup, String coups_entier, String mouvement) {
		return "Erreur : le coup "+get_numero_coup(numero_coup, coups_entier)+"("
		+mouvement+") est incorrect.";
	}

	public String erreur_fic_incorrect (int numero_ligne, String ligne) {
		return "Le fichier de sauvegarde est incorrect a la ligne "
		+numero_ligne+",\n ("+ligne+")";
	}
}
