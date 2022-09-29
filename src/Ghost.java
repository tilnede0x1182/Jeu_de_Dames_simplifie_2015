import java.awt.Color;

class Ghost {
	private Joueur joueur;
	private Color couleur;
	/**
		Fantôme bon ou mauvais : <br>
			true : bon <br>
			false : mauvais
	**/
	private boolean bon_mauvais;

	public Ghost (Joueur j1, boolean bon_mauvais) {
		this.joueur = j1;
		this.bon_mauvais = bon_mauvais;
		if (bon_mauvais) this.couleur = Color.blue;
		else this.couleur = Color.red;
	}

	/**
		Pour les extensions
	**/
	public Ghost (Joueur j1, boolean bon_mauvais, Color couleur) {
		this(j1, bon_mauvais);
		this.couleur = couleur;
	}

// ######## fonction toString ########################################### //

	public String toString () {
		String res = "";
		if(joueur.getJoueur1_2()) {
			res="X";
			if (!bon_mauvais) res="x";
		}
		else {
			res="Y";
			if (!bon_mauvais) res="y";
		}
		return res;
	}

// ##### Getteurs et Setteurs ########################################### //

	public Color getCouleur () {
		return this.couleur;
	}

	public boolean getBon_mauvais () {
		return bon_mauvais;
	}

	public Joueur getJoueur () {
		return joueur;
	}
}