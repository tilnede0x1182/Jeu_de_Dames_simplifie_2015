import java.io.IOException;

class Main {
	private Jeu jeu = null;
	private String [] args = null;
	private boolean jeu_non_graphique = false;
	private boolean choix_pions_auto = false;
	private boolean mode_triche = false;
	private LectureEcriture le = null;

	public void lance_jeu () {
		jeu();
		while (jeu_non_graphique) jeu();
	}

	public void jeu () {
		int q1 = 0, q2=0, q3=0, haut_bas = 0;
		Menu_non_graphique menu = new Menu_non_graphique();
		q1 = menu.menu_principal();
		if (q1==1) {
			aff("");
			q2 = menu.menu_nouveau_jeu();
			mode_triche = false;
			if (q2!=3) {
				aff("");
				q3 =  menu.menu_mode_triche();
				if (q3!=2) mode_triche = true;
			}
			if (q2==1) {
				aff("");
				haut_bas = menu.menu_haut_bas();
				aff("");
				if (haut_bas==1) {
					jeu = new Jeu(this, true, false, 
					jeu_non_graphique, mode_triche, 
					choix_pions_auto);
				}				
				else {
					jeu = new Jeu(this, false, true, 
					jeu_non_graphique, mode_triche, 
					choix_pions_auto);
				}
			}
			if (q2==2) {
				aff("Le joueur 1 est en haut "+
				"et le joueur 2 est en bas.");
				
				jeu = new Jeu(this, true, true,
				jeu_non_graphique, mode_triche, 
				choix_pions_auto);
			}
			if (q2==3) {
				jeu = new Jeu(this, false, false,
				jeu_non_graphique, true, 
				choix_pions_auto);
			}
			if (jeu_non_graphique) jeu_non_graphique();
		}
		if (q1==2) {
			relecture();
		}
		if (q1==3) {
			System.exit(0);
		}
		aff("\n\n");
	}

	public void jeu_non_graphique () {
		if (jeu!=null) {
			int res = jeu.jeu_non_graphique();
			if (res!=0) jeu.fin_du_jeu(res);
		}
		else {
			aff("Erreur : le jeu est null");
		}
	}

	public void relecture () {
		Fenetre ftmp = null;
		Relecture relecture = null;
		try {
			le = new LectureEcriture('r');
		}
		catch (IOException e) {
			aff(e.getMessage());
			return;
		}
		if (jeu!=null)
		if (!jeu.getJeu_non_graphique())
		ftmp = jeu.getFenetre();
		try {
			relecture = new Relecture (this);
			jeu = relecture.relire(le.getNom_du_fichier());
		}
		catch (GameLoadingException e) {
			aff(e.getMessage());
			return;
		}

		if (jeu.getJeu_non_graphique()) {
			jeu_non_graphique = true;
			if (ftmp!=null) ftmp.dispose();
		}
		jeu_non_graphique();
	}

// ########################### Fonction main ################################# //

	public static void main (String [] args) {
		Main main = new Main();
		main.initialise_main(main, args);
	}

// #################### Analyse des argmuents ################################ //

	public boolean [] analyse_args (String [] args) {
		if (args==null) return null;
		boolean [] res = new boolean[2];
		int i=0;
		for (i=0; i<args.length; i++) {
			if (args[i]!=null) {
				if (args[i].contains("-choix_auto")
				|| args[i].contains("-cha"))
					res[0] = true;
				if (args[i].contains("-jeu_non_graphique")
				|| args[i].contains("-jng"))
				res[1] = true;
			}
		}
		return res;
	}

	/**
		Initiliase les options en fonction des argmuments 
		donnés en ligne de commande.
	**/
	public void initialise_options_args (String [] args) {
		boolean [] tmp = analyse_args(args);
		if (tmp[0]) choix_pions_auto = true;
		if (tmp[1]) jeu_non_graphique = true;
	}

// ##################### Getteurs et setteurs ################################ //

	public Jeu getJeu () {
		return jeu;
	}
	public void setJeu (Jeu jeu) {
		this.jeu = jeu;
	}
	public boolean getJeu_non_graphique () {
		return jeu_non_graphique;
	}
	public void setJeu_non_graphique (boolean tmp) {
		jeu_non_graphique = tmp;
	}
	public void setArgs (String [] tmp) {
		args = tmp;
	}
	public String [] getArgs () {
		return args;
	}

// ####################### Lancement du jeu ################################ //

	public void initialise_main (String [] args) {
		Main main = new Main();
		initialise_main(main, args);
	}

	public void initialise_main (Main main, String [] args) {
		this.args = args;
		if (args!=null) {	
			main.initialise_options_args(args);
		}
		/// tmp pour le test ///
		//main.setJeu_non_graphique(true);
		/// ################ ///
		main.lance_jeu();
	}

// ##################### Fonctions utilitaires ############################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}
}
