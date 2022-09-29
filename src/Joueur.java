import java.awt.Color;

class Joueur {
	private String nom="";
	/**
		Joueur 1 ou 2 : <br>
			true : joueur 1<br>
			false : joueur 2
	**/
	private boolean joueur1_2;
	/**
		joueur_humain_ordinateur : <br>
			true : humain<br>
			false : ordinateur
	**/
	private boolean joueur_humain_ordinateur = false;
	private boolean gagne = false;
	private int nbr_de_fantomes_mauvais = 0;
	private int nbr_de_fantomes_bons = 0;
	private int bons_fantomes_captures = 0;
	private int mauvais_fantomes_captures = 0;

	public Joueur (boolean joueur_humain_ordinateur, boolean joueur1_2) {
		this.joueur_humain_ordinateur = joueur_humain_ordinateur;
		this.joueur1_2 = joueur1_2;
		if (nom.isEmpty()) {
			if (joueur1_2) this.nom = "Joueur 1";
			else this.nom = "Joueur 2";
		}
	}

	public Joueur (String nom, boolean joueur_humain_ordinateur, 
			boolean joueur1_2) {
		this(joueur_humain_ordinateur, joueur1_2);
		this.nom = nom;
	}

// ################### Fonctions utilitaires ################################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	/**
		Affiche des informations sur le joueur (pour le débuggage).
	**/
	public void aff_info () {
		aff("Nom : "+nom);
		aff("joueur_humain_ordinateur : "+joueur_humain_ordinateur);
		aff("nbr_de_fantomes_bons : "+nbr_de_fantomes_bons);
		aff("nbr_de_fantomes_mauvais : "+nbr_de_fantomes_mauvais);
		aff("bons_fantomes_captures : "+bons_fantomes_captures);
		aff("mauvais_fantomes_captures : "+mauvais_fantomes_captures);
		aff("gagne = "+gagne);
	}

// ################# Fantômes ########################################### //

	/**
		Bon  : true
		Mauvais : false
	**/
	public void capture_fatome (boolean bon_mauvais) {
		if (bon_mauvais) bons_fantomes_captures++;
		else mauvais_fantomes_captures++;
	}

	/**
		Bon  : true
		Mauvais : false
	**/
	public void se_fait_capturer_un_fatome (boolean bon_mauvais) {
		if (bon_mauvais) nbr_de_fantomes_bons--;
		else nbr_de_fantomes_mauvais--;
	}

// ############################ Fin du jeu ############################## //

	public void gagne () {
		gagne = true;
	}

// ######################### Fonction toString ########################## //

	public String toString () {
		return nom;
	}

// ############## Getteurs et Setteurs ################################## //

	public String getNom () {
		return this.nom;
	}
	public boolean getJoueur1_2 () {
		return this.joueur1_2;
	}
	public int getNbr_de_fantomes_bons() {
		return nbr_de_fantomes_bons;
	}
	public int getNbr_de_fantomes_mauvais() {
		return nbr_de_fantomes_mauvais;
	}
	public int getNbr_de_fantomes() {
		return nbr_de_fantomes_mauvais+nbr_de_fantomes_bons;
	}
	public boolean getJoueur_humain_ordinateur () {
		return joueur_humain_ordinateur;
	}

	public void setNbr_fantomes_bons(int tmp) {
		nbr_de_fantomes_bons = tmp;
	}
	public void setNbr_fantomes_mauvais(int tmp) {
		nbr_de_fantomes_mauvais = tmp;
	}
	public void setJoueur_humain_ordinateur (
			boolean joueur_humain_ordinateur) {
		this.joueur_humain_ordinateur = joueur_humain_ordinateur;
	}
	public int getBons_fantomes_captures () {
		return bons_fantomes_captures;
	}
	public int getMauvais_fantomes_captures () {
		return mauvais_fantomes_captures;
	}
	public void setBons_fantomes_captures (int tmp) {
		bons_fantomes_captures = tmp;
	}
	public void setMauvais_fantomes_captures (int tmp) {
		mauvais_fantomes_captures = tmp;
	}
	public boolean getGagne () {
		return gagne;
	}

}