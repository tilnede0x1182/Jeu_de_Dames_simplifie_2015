import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;

class CaseNonGraphique implements Case {
	private final Color couleur_de_fond_de_base = new Color(255, 206, 158);
	private final Color couleur_de_fond_Case_sortie = 
		new Color(209, 139, 71);

	boolean Case_sortie = false;
	/**
		true : joueur1
		false : joueur2
	**/
	private boolean joueur1_2 = false;
	private Color couleur_de_fond =  couleur_de_fond_de_base;
	/**
		fantome_visible : <br>
			true : activé <br>
			false : désactivé
	**/
	private boolean fantome_visible = true;
	private Ghost ghost;

	public CaseNonGraphique () {
		super();
	}

	public CaseNonGraphique (Ghost ghost) {
		this();
		this.ghost = ghost;
	}

// ######################## Gestion du fantôme ############################## //

	public boolean ajoute_fantome (Ghost ghost_tmp) {
		if (ghost!=null) return false;
		else {
			ghost = ghost_tmp;
			return true;
		}
	}

	public boolean retire_fantome () {
		if (ghost==null) return false;
		else {
			ghost = null;
			return true;
		}
	}

// ####################### Confrontation de fantômes ######################## //

	/**
		Capture le fantôme par g2.
	**/
	public boolean capture_fantome (Ghost g2) {
		if (!fantome_present()) return false;
		else {
			String nature_du_fantome = "bon";
			if (!ghost.getBon_mauvais()) nature_du_fantome = "mauvais";
			Joueur joueur_attaquant = g2.getJoueur();
			Joueur joueur_attaque = ghost.getJoueur();
			aff("Le fantome caputre est "+nature_du_fantome+".");	
			joueur_attaquant.capture_fatome(ghost.getBon_mauvais());
			joueur_attaque.se_fait_capturer_un_fatome(
				ghost.getBon_mauvais());
			retire_fantome();
			ajoute_fantome(g2);
			return true;
		}
	}

// ######################## Deplacement de fantômes ######################## //

	public void place_fantome (Ghost ghost_tmp) {
		if (Case_sortie) {
			Joueur joueur_attaquant = ghost_tmp.getJoueur();
			if (joueur1_2==joueur_attaquant.getJoueur1_2()
				&& ghost_tmp.getBon_mauvais())
			// teste si le fantôme appartient au bon joueur
			// et que c'est un bon fantôme
				joueur_attaquant.gagne();
		}
		if (ghost==null) ajoute_fantome(ghost_tmp);
		else capture_fantome(ghost_tmp);
	}

// ########################## Fonctions toString ########################### //

	public String toString () {
		String res="";
		if (Case_sortie && !fantome_present()) {
			res+="exit ";
		}
		else if (fantome_present()) {
			String joueur="j1";
			if (!ghost.getJoueur().getJoueur1_2 ()) joueur="j2";
			String fantome="fb";
			if (!ghost.getBon_mauvais ()) fantome="fm";
			if (fantome_visible) res+=joueur+" "+fantome;
			else res+=joueur+"   ";
		}
		else res+="vide ";
		return res;
	}

// ######################### Getteurs et Setteurs ########################## //

	public boolean getCase_sortie () {
		return Case_sortie;
	}
	public void setCase_sortie (boolean cs, boolean joueur1_2) {
		Case_sortie = cs;

		if (cs) {
			couleur_de_fond = couleur_de_fond_Case_sortie;
			this.joueur1_2 = joueur1_2;
		}
		else {
			couleur_de_fond = couleur_de_fond_de_base;
		}
		//repaint();
	}
	/**
		true : le fantôme appartient au joueur 1<br>
		false : le fantôme n'est pas présent ou appartient au joueur 2.<br>
		Utiliser la fonction fantome_present ()
		pour lever l'ambiguité sur l'existance du fantôme.
		@see CaseNonGraphique#fantome_present
	**/
	public boolean getJoueur1_2() {
		if (ghost==null) return false;
		return ghost.getJoueur().getJoueur1_2();
	}
	public boolean getJoueur1_2_Case() {
		return joueur1_2;
	}
	public Ghost getGhost () {
		return ghost;
	}
	public Color getCouleur_du_fantome () {
		if (fantome_present())
			return ghost.getCouleur();
		else return null;
	}
	public boolean getFantome_visible () {
		return fantome_visible;
	}
	public void setFantome_visible (boolean fantome_visible_tmp) {
		this.fantome_visible = fantome_visible_tmp;
	}

// ######################## Fonctions utilitaires ########################## //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	public boolean fantome_present () {
		return (ghost!=null);
	}

	public void affiche_info () {
		aff("Case_sortie : "+Case_sortie);
		aff("fantome_present : "+fantome_present());
		if (Case_sortie)
			aff("Case de sotie pour le joueur : "+joueur1_2);
	}
	
	public boolean fantome_joueur_present (boolean joueur1_2) {
		if (fantome_present())
			return (ghost.getJoueur().getJoueur1_2()==joueur1_2);
		else return false;
	}

	public boolean fantome_joueur_adverse_present (boolean joueur1_2) {
		if (fantome_present())
			return (ghost.getJoueur().getJoueur1_2()!=joueur1_2);
		else return false;
	}

	public static int randInt (int min, int max) {
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}
}