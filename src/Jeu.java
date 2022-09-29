import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

class Jeu {
	private Plateau plateau;
	private Tour_de_jeu tour;
	private Menu_non_graphique menu;
	private Sauvegarde sauv;
	private int gain_par_case_sortie_j1 = 0;
	private int gain_par_case_sortie_j2 = 0;
	private Mouvements mouvements;
	private boolean jeu_graphique_commence = false;
	private boolean jeu_non_graphique = true;
	private Fenetre fenetre;
	private Joueur j1;
	private Joueur j2;
	private ArrayList<String> liste_de_coups;
	private int numero_coup = 1;
	private String fichier_ouvert = "";
	private String typeJeu = "";
	private boolean jeu_humain=true;
	private boolean jeu_completement_humain=true;
	private boolean choix_pions_auto = false;
	private Main main = null;
	/**
		Désigne le joueur dont c'est le<br>
		tour de jouer durant le jeu graphique.<br>
		true : c'est le tour du joueur 1<br>
		false : c'est le tour du joueur 2
	**/
	private boolean joueur_du_tour_graphique = true;
	private String case1 = "";
	private int fin_du_jeu_graphique = -1;
	/**
		mode_triche : <br>
			true : activé <br>
			false : désactivé
	**/
	private boolean mode_triche = false;

	public Jeu (Main main, boolean j1B, boolean j2B, boolean jeu_non_graphique, 
			boolean mode_triche) 
			throws BoardGenerationException {
			this (main, j1B, j2B, jeu_non_graphique, 
				mode_triche, true);
	}

	public Jeu (Main main, boolean j1B, boolean j2B, boolean jeu_non_graphique, 
			boolean mode_triche, boolean choix_pions_auto) 
			throws BoardGenerationException {
		this.main = main;
		this.jeu_non_graphique = jeu_non_graphique;
		this.choix_pions_auto = choix_pions_auto;
		this.mode_triche = mode_triche;
		plateau = new Plateau(6, 6);
		initialisation_du_jeu(j1B, j2B, jeu_non_graphique);
	}

	public void initialisation_du_jeu (boolean j1B, boolean j2B, 
			boolean jeu_non_graphique) {
		/**
			Joueur : <br>
				H : humain <br>
				C : ordinateur (computer) <br>
			Jeu : <br>
				G : graphique <br>
				T : non graphique (terminal)
		**/
		if (j1B) typeJeu+="H";
		else typeJeu+="C";
		if (j2B) typeJeu+="H";
		else typeJeu+="C";
		if (jeu_non_graphique) typeJeu+="T";
		else  typeJeu+="G";

		Plateau_initial pi;
		liste_de_coups = new ArrayList<>();
		j1 = new Joueur(j1B, true);
		j2 = new Joueur(j2B, false);
		jeu_humain = (j1.getJoueur_humain_ordinateur() 
				|| j2.getJoueur_humain_ordinateur());
		jeu_completement_humain = (j1.getJoueur_humain_ordinateur() 
				&& j2.getJoueur_humain_ordinateur());
		// On vérifie s'il y a au moins 1 joueur humain
		sauv = new Sauvegarde();
		pi = new Plateau_initial(plateau.getPlateau(), j1, choix_pions_auto);
		plateau.setPlateau(pi.getPlateau());
		pi = new Plateau_initial(plateau.getPlateau(), j2, choix_pions_auto);
		String [] tmp = pi.sauve_coups_initiaux(plateau);
		liste_de_coups.add(tmp[0]);
		liste_de_coups.add(tmp[1]);
		plateau.setPlateau(pi.getPlateau());
		if (jeu_non_graphique) {
			tour = new Tour_de_jeu(plateau);
			mouvements = new Mouvements(plateau);
			menu = new Menu_non_graphique();
			if (!jeu_humain) affiche();
			if (mode_triche) plateau.setMode_triche(this, true);
		}
		else {
			fenetre = new Fenetre(plateau, this);
			mouvements = new Mouvements(plateau);
			tour = new Tour_de_jeu(plateau);
			setJeu_graphique_commence(true);
			if (mode_triche)
				fenetre.setMode_triche(true);
			else {
				if (!j1B)
					fenetre.mode_triche_desactive(
						getJoueur(false));
				else
					fenetre.mode_triche_desactive(
						getJoueur(true));
			}
		}
	}

// ############################# Exécution du jeu ############################# //

	/**
		Exécute le jeu non graphique.<br>
		Renvoie un int : <br>
		1 : le joueur 1 gagne<br>
		2 : le joueur 2 gagne
	**/
	public int jeu_non_graphique () {
		int res = detecte_fin_jeu();
		int menu_tmp = 0;
		while (res==0) {
			if (!mode_triche && j1.getJoueur_humain_ordinateur())
				plateau.mode_triche_desactive(this, j1);
			if (jeu_humain) affiche(j1);
			aff("");
			menu_tmp = menu_jeu_non_graphique(
				j1.getJoueur_humain_ordinateur());
			if (menu_tmp==-1) return 0;
			if (menu_tmp==1) {
				tour.joue(j1, mouvements);
				res = detecte_fin_jeu();
				if (!jeu_humain) affiche(j1);
				ajoute_coup(true);
			}
			if (!mode_triche && j2.getJoueur_humain_ordinateur())
				plateau.mode_triche_desactive(this, j2);
			if (jeu_humain) affiche(j2);
			aff("");
			menu_tmp = menu_jeu_non_graphique(
				j2.getJoueur_humain_ordinateur());
			if (menu_tmp==-1) return 0;
			if (res==0 && menu_tmp==1) {
				// le jeu s'arrête ici si le joueur 1 a gagné.
				tour.joue(j2, mouvements);
				res = detecte_fin_jeu();
				if (!jeu_humain) affiche(j2);
				ajoute_coup(false);
			}
			incremente_numero_coup();
		}
		return res;
	}

	public void jeu_graphique (String case1) {
		int tour=-1;
		fin_du_jeu_graphique = -1;
		boolean tour_de_jeuB = false;
		if (this.case1.isEmpty()) tour_de_jeuB = true;
		int [] coord_tmp_case1 = plateau.ConvertString_Coordonnees(case1);
		if (!tour_de_jeuB)
			coord_tmp_case1 = plateau.ConvertString_Coordonnees(this.case1);
		int [] coord_tmp_case2 = plateau.ConvertString_Coordonnees(case1);

		Joueur joueur_tmp = j1;
		if (!joueur_du_tour_graphique) joueur_tmp = j2;
		// on décide le joueur qui jouera au tour suivant

		if (tour_de_jeuB) this.case1 = case1;
		// ######## Affichage pour le débuggage ###### //
		/*affnn("\ncase1 = ");
		if (tour_de_jeuB) aff(case1);
		else {
			aff(this.case1);
			aff("case2 = "+case1);
		}*/
		// ########################################### //
		tour = this.tour.tour_de_jeu_graphique(joueur_tmp, mouvements, 
			this.case1, case1, tour_de_jeuB);
		if (tour==-1) {
			this.case1="";
			fenetre.getCase(coord_tmp_case1[0], coord_tmp_case1[1]).deselect();
		}
		tour = this.tour.tour_de_jeu_graphique(joueur_tmp, mouvements, 
			this.case1, case1, tour_de_jeuB);
		//aff("tour = "+tour);
		if (tour==0) {
			fenetre.getCase(coord_tmp_case2[0], coord_tmp_case2[1]).select();
		}
		if (tour==0 && !tour_de_jeuB) {
			if (!mode_triche && getJoueur(!joueur_tmp.getJoueur1_2())
				.getJoueur_humain_ordinateur()) 
				fenetre.mode_triche_desactive(getJoueur(!joueur_tmp.getJoueur1_2()));
			//aff("joueur en cours : "+joueur_tmp);
			deplace_pion_graphique(this.case1, case1);
			deselect_case(this.case1);
			deselect_case(case1);
			if (joueur_du_tour_graphique) joueur_du_tour_graphique = false;
			else joueur_du_tour_graphique = true;
			this.case1 = "";
			//aff("joueur_du_tour_graphique : "+joueur_du_tour_graphique);
			if (j1.getJoueur_humain_ordinateur() 
					&& !joueur_du_tour_graphique) {
				ajoute_coup(true);
			}
			String coup_joue_tmp = plateau.getCoup_joue();
			joueur_du_tour_graphique = 
				joue_ordi_aux(joueur_du_tour_graphique, true);
			if (j2.getJoueur_humain_ordinateur() 
					&& ((!jeu_completement_humain 
					&& !joueur_du_tour_graphique)
					|| (jeu_completement_humain 
					&& joueur_du_tour_graphique))) {
				ajoute_coup(coup_joue_tmp, false);
				incremente_numero_coup();
			}
			fin_du_jeu_graphique = detecte_fin_jeu();
			if (fin_du_jeu_graphique!=0) {
				fin_du_jeu(fin_du_jeu_graphique);
				setJeu_graphique_commence(false);
			}
		}
		if (tour==0 && tour_de_jeuB) {
			this.case1 = case1;
			fenetre.getCase(coord_tmp_case1[0], coord_tmp_case1[1]).select();
		}
		if (tour==-2) {
			this.case1="";
			fenetre.getCase(coord_tmp_case1[0], coord_tmp_case1[1]).deselect();
		}
	}

	public void jeu_ordinateur_graphique (Joueur joueur, Mouvements mouv) {
		String [] tmp = tour.joue_ordi_aux(joueur, mouv, false);
		// n'affiche pas le coup joué (boolean graphique = false).
		deplace_pion_graphique(tmp[0], tmp[1]);
		fin_du_jeu_graphique = detecte_fin_jeu();
		if (fin_du_jeu_graphique!=0) {
			fin_du_jeu(fin_du_jeu_graphique);
			setJeu_graphique_commence(false);
		}
	}

// ##************# Fonction utilitaires pour le jeu graphique #**************## //

	public void select_case (String case1) {
		if (fenetre==null) return;
		int [] coord_tmp_case = plateau.ConvertString_Coordonnees(case1);
		fenetre.getCase(coord_tmp_case[0], coord_tmp_case[1]).select();
	}

	public void deselect_case (String case1) {
		if (fenetre==null) return;
		int [] coord_tmp_case = plateau.ConvertString_Coordonnees(case1);
		fenetre.getCase(coord_tmp_case[0], coord_tmp_case[1]).deselect();
	}

	public void change_message (String message) {
		if (fenetre==null) return;
		fenetre.remove_all_messages();
		fenetre.add_message(message);
	}

	/**
		Effectue un déplace un pion et met à jour le plateau graphique en conséquences.
	**/
	public void deplace_pion_graphique (String case1, String case2) {
		plateau.deplace_fantome(case1, case2);
		if (!mode_triche)
			fenetre.mode_triche_desactive(getJoueur(!joueur_du_tour_graphique));
		met_a_jour_deux_cases(case1, case2);
	}

	/**
		Met à jour deux cases sur le plateau graphique 
		(pour les déplacements).
	**/
	public void met_a_jour_deux_cases (String case1, String case2) {
		int [] coord_tmp_case = plateau.ConvertString_Coordonnees(case1);
		fenetre.getCase(coord_tmp_case[0], coord_tmp_case[1])
		.mise_a_jour(plateau.getCase(coord_tmp_case[0], coord_tmp_case[1]));

		coord_tmp_case = plateau.ConvertString_Coordonnees(case2);
		fenetre.getCase(coord_tmp_case[0], coord_tmp_case[1])
		.mise_a_jour(plateau.getCase(coord_tmp_case[0], coord_tmp_case[1]));
	}

	public void change_message_joueur (Joueur joueur) {
		change_message("C'est à "+joueur+" de jouer");
	}

	public Joueur echange_joueur (boolean joueur_du_tour_graphique) {
		Joueur joueur = j1;
		if (!joueur_du_tour_graphique) {
			joueur = j2;
		}
		/*if (!mode_triche && joueur.getJoueur_humain_ordinateur()) 
			fenetre.mode_triche_desactive(joueur);*/
		return joueur;
	}

	public boolean joue_ordi_aux (boolean joueur_du_tour_graphique,
			boolean echange_au_debut) {
		int i=0;
		boolean joueur_ordi = false;
		Joueur joueur_t1 = j1;
		Joueur joueur_t2 = j2;
		joueur_t2 = echange_joueur (joueur_du_tour_graphique);
		if (joueur_t2==j1) joueur_t1 = j2;
		//aff("C'est a "+joueur_t2+" de jouer");
		//aff("joueur_du_tour_graphique : "+joueur_du_tour_graphique);
		do {
			if (echange_au_debut) change_message_joueur(joueur_t2);
			if (!joueur_t2.getJoueur_humain_ordinateur()) {
				jeu_ordinateur_graphique(joueur_t2, mouvements);
				if (!j1.getJoueur_humain_ordinateur()) {
					ajoute_coup(true);
				}
				if(!j2.getJoueur_humain_ordinateur()) {
					ajoute_coup(false);
					incremente_numero_coup();
				}
				if (joueur_du_tour_graphique)
					joueur_du_tour_graphique = false;
				else joueur_du_tour_graphique = true;
				joueur_t2 = echange_joueur (joueur_du_tour_graphique);
				if (joueur_t2==j1) joueur_t1 = j2;
				change_message_joueur(joueur_t2);
				joueur_ordi = !joueur_t2.getJoueur_humain_ordinateur();
			}
			fin_du_jeu_graphique = detecte_fin_jeu();
			if (fin_du_jeu_graphique!=0) {
				//aff("fin du jeu");
				fin_du_jeu(fin_du_jeu_graphique);
				setJeu_graphique_commence(false);
				return joueur_du_tour_graphique;
			}

		} while (joueur_ordi && jeu_graphique_commence);
		return joueur_du_tour_graphique;
	}

	public void active_desactive_mode_triche () {
		if (mode_triche) setMode_triche(false);
		else setMode_triche(true);
	}

// ##******************# Menu pour le jeu non graphique #********************## //

	/**
		return -1 en cas d'erreur.
	**/
	public int menu_jeu_non_graphique (boolean joueur_humain) {
		if (!jeu_non_graphique) return -1;
		int menu_tmp=0;
		if (!joueur_humain)
			return 1;
		else {
			while (menu_tmp!=1) {
				menu_tmp = menu.menu_tour_de_jeu();
				if (menu_tmp==2) {
					//sauvegarder la partie
					fichier_ouvert = sauv.enregistrer(
						fichier_ouvert, liste_de_coups, this);
				}
				if (menu_tmp==3) {
					return -1;
				}
			}
		}
		return menu_tmp;
	}

// ######################## Affichage graphique ############################### //

	/**
		Met à jour le plateau graphique 
		si le jeu est graphique.
	**/
	public void met_a_jour_plateau_graphique () {
		if (!jeu_non_graphique && fenetre!=null) {
			fenetre.mise_a_jour_cases();
		}
	}


// ######################### Affichage non graphique ########################## //

	/**
		Affiche le plateau de jeu dans le terminal.
	**/
	public void affiche () {
		aff(""+plateau);
	}

	/**
		Affiche le nom du joueur qui joue ansi
		que le plateau de jeu dans le terminal.
	**/
	public void affiche (Joueur joueur) {
		aff(""+plateau);
		aff("C'est a "+joueur+" de jouer.");
	}

// ############################ Sauvegarde des coups ########################## //

	public void ajoute_coup (String coup_joue, boolean premier_coup) {
		String numero_coup_Str = ""+numero_coup;
		if (!premier_coup) numero_coup_Str="";
		String res = numero_coup_Str+" - "+coup_joue;
		//aff(res);
		liste_de_coups.add(res);
	}
	
	public void ajoute_coup (boolean premier_coup) {
		String numero_coup_Str = ""+numero_coup;
		if (!premier_coup) numero_coup_Str="";
		String res = numero_coup_Str+" - "+plateau.getCoup_joue();
		//aff(res);
		liste_de_coups.add(res);
	}

// ################################ Fin du jeu ################################ //

	/**
		Compte un tour avant d'attribuer la victoire<br>
		grâce au placement d'un bon fantôme sur une case<br>
		sortie adverse.
	**/
	public int detecte_gain_par_case_sortie () {
		if (j1.getGagne()) gain_par_case_sortie_j1++;
		if (j2.getGagne()) gain_par_case_sortie_j2++;
		// ça doit être > 2 car on vérifie après chaque joueur
		// (on exécute cette fonction après chaque joueur).
		if (gain_par_case_sortie_j1>2) return 1;
		if (gain_par_case_sortie_j2>2) return 2;
		return 0;
	}

	/**
		0 : le jeu continue<br>
		1 : le joueur 1 a gagne<br>
		2 : le joueur 2 a gagne
	**/
	public int detecte_fin_jeu () {
		int tmp = detecte_gain_par_case_sortie();
		if (tmp!=0) return tmp;
		if (j1.getNbr_de_fantomes_mauvais()==0) return 1;
		if (j2.getNbr_de_fantomes_mauvais()==0) return 2;
		if (j1.getNbr_de_fantomes_bons()==0) return 2;
		if (j2.getNbr_de_fantomes_bons()==0) return 1;
		return 0;
	}

	/**
		Si gagnant = 1 : le joueur gagnant est le joueur 1<br>
		si gagnant = 2 : le joueur gagnant est le joueur 2
	**/
	public void fin_du_jeu (int gagnant) {
		if (gagnant!=1 && gagnant!=2) return;
		if (!jeu_graphique_commence) return;
		String joueur = ""+j2, message="";
		if (gagnant==1) joueur = ""+j1;
		message = "Le gagnant est "+joueur+".";

		if (jeu_non_graphique) {
			aff(message);
		}
		else {
			JOptionPane jop1;
			jop1 = new JOptionPane();
			jop1.showMessageDialog(null, 
				message, 
				"Fin du jeu", 
				JOptionPane.INFORMATION_MESSAGE);
		}
	}

// ##################### Getteurs et setteurs ################################# //

	public String getTypeJeu () {
		return typeJeu;
	}

	public Plateau getPlateau () {
		return plateau;
	}

	public Fenetre getFenetre () {
		return fenetre;
	}

	public boolean getJeu_non_graphique () {
		return jeu_non_graphique;
	}

	public boolean getJeu_graphique_commence () {
		return this.jeu_graphique_commence;
	}

	public void setJeu_graphique_commence (boolean jeu_commence) {
		jeu_graphique_commence = jeu_commence;
		if (jeu_commence) {
			case1 = "";
			joueur_du_tour_graphique = true;
			if (joueur_du_tour_graphique)
				fenetre.add_message("C'est à "+j1+" de jouer");
			else 
				fenetre.add_message("C'est à "+j2+" de jouer");
			if (!j1.getJoueur_humain_ordinateur()) {
				joueur_du_tour_graphique = 
					joue_ordi_aux(joueur_du_tour_graphique, false);
			}
		}
		else {
			reinitialise_main();
		}
	}

	/**
		Retourne un Joueur.
		joueur1_2 : <br>
			true : joueur 1<br>
			false : joueur 2
	**/
	public Joueur getJoueur (boolean joueur1_2) {
		if (joueur1_2) return j1;
		else return j2;
	}

	public Mouvements getMouvements () {
		return mouvements;
	}

	public Main getMain () {
		return main;
	}

	public ArrayList<String> getListe_de_coups () {
		return liste_de_coups;
	}

	public String getFichier_ouvert () {
		return fichier_ouvert;
	}

	public void setFichier_ouvert (String tmp) {
		fichier_ouvert = tmp;
	}

	public Sauvegarde getSauv () {
		return sauv;
	}

	public boolean getMode_triche () {
		return mode_triche;
	}

	public void setMode_triche (boolean tmp) {
		mode_triche = tmp;
		if (mode_triche) {
			fenetre.mode_triche_active();
		}
		else {
			Joueur joueur_tmp = j1;
			if (!joueur_du_tour_graphique) joueur_tmp = j2;
			fenetre.mode_triche_desactive(joueur_tmp);
		}
	}

// ################### Fonctions utilitaires ################################## //

	public void reinitialise_main () {
		if (fenetre!=null) fenetre.dispose();
		main.initialise_main(main.getArgs());
	}

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

// #************ Fonctions utilitaires pour la sauvegarde des coups *********# //

	public void incremente_numero_coup () {
		numero_coup++;
	}
}
