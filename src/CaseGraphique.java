import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;


public class CaseGraphique extends JPanel implements Case {
	private Plateau plateau;
	private int [] adresse_CaseGraphique;
	private boolean selectionne;
	private boolean CaseGraphique_selection_initiale;
	private boolean fantome_present = true;
	private boolean case_sortie = false;
	private final Color couleur_de_fond_de_base = new Color(255, 206, 158);
	private Color couleur_de_fond = couleur_de_fond_de_base;
	private Color couleur_de_fond_tmp = couleur_de_fond_de_base;
	private Color couleur_des_sorties = new Color(209, 139, 71);
	private Color couleur_du_fantome = new Color(randInt(0, 255), randInt(0, 
		255), randInt(0, 255));
	private Color couleur_du_fantome_tmp = couleur_du_fantome;
	private Color couleur_du_fantome_indetermine = Color.gray;
	/**
		fantome_visible : <br>
			true : activé <br>
			false : désactivé
	**/
	private boolean fantome_visible = true;

	public CaseGraphique (Plateau plateau, int i, int j) {
		super();
		this.plateau = plateau;
		adresse_CaseGraphique = new int[2];
		adresse_CaseGraphique[0] = i;
		adresse_CaseGraphique[1] = j;
	}

// #################### Placement du fantôme s'il existe ###################### //

	/**
	 * Calcule les hauteurs de chaque rond 
	 * et du rectangle de la couleur du fond
	 * en fonction de la hauteur du rond 1.
	 */
	public int [] calcul_hr2_hr3_hr4_aux (int hr1, int tr1, int tr2, 
			int tr3) {
		int [] res = new int[3];
		int hr2=0, hr3=0, hr4=0;
		hr2 = (int)(hr1+tr1-(int)((41/100.0)*tr1));
		hr3 = (int)(hr2+tr2-(int)((44/100.0)*tr2));
		hr4 = (int)(hr3+tr3-(int)((30/100.0)*tr3));
		res[0] = hr2; res[1] = hr3; res[2] = hr4;
		return res;	
	}
	
	/**
	 * Utilise la fonction calcul_hr2_hr3_hr4_aux pour placer 
	 * le fantôme bien au milieu au niveau de la hauteur.
	 */
	public int [] calcul_hr2_hr3_hr4 (int tr1, int tr2, int tr3, 
			int getHeight) {
		int[] t1  = calcul_hr2_hr3_hr4_aux(0, tr1, tr2, tr3);
		int hauteur_rond1 = getHeight/2-t1[2]/2;
		t1 = calcul_hr2_hr3_hr4_aux(hauteur_rond1, tr1, tr2, tr3);
		int [] res = new int[4];
		res[0] = hauteur_rond1;	res[1] = t1[0]; res[2] = t1[1]; res[3] = 
			t1[2];
		return res;
	}
	
	public int [] calcule_place_fantome (int getHeight, int getWidth) {
		int taille_rond1=(int)(0.24*(Math.min(getWidth, getHeight)));
		int taille_rond2=(int)(0.4*(Math.min(getWidth, getHeight)));
		int taille_rond3=(int)(0.6*(Math.min(getWidth, getHeight)));
		int [] t1 = calcul_hr2_hr3_hr4(taille_rond1, taille_rond2, 
			taille_rond3, getHeight);
		int [] res = new int [7];
		res[0] = taille_rond1;
		res[1] = taille_rond2;
		res[2] = taille_rond3;
		res[3] = t1[0];	res[4] = t1[1]; res[5] = t1[2]; res[6] = t1[3];
		return res;
	}
	
	public void place_fantome (Graphics g, int getHeight, int getWidth) {
		int [] hr1 = calcule_place_fantome(getHeight, getWidth);
		// Augmente l'épaisseur du trait en fonction de la taille ***# //
		// de la fenetre #*******************************************# //
		int epaisseur_de_trait = 1;
		if (Math.min(getHeight, getWidth)>50) epaisseur_de_trait = 2;
		if (Math.min(getHeight, getWidth)>100) epaisseur_de_trait = 3;
		// #*********************************************************# //
		int taille_rond1 = hr1[0];
		int taille_rond2 = hr1[1];
		int taille_rond3 = hr1[2];			
		int hauteur_r1 = hr1[3];
		int hauteur_r2 = hr1[4];
		int hauteur_r3 = hr1[5];
		int hauteur_r4 = hr1[6];
		int rayon_r3 = taille_rond3/2;
		int distance_v = hauteur_r4-(hauteur_r3+rayon_r3);
		int mesure_trait_cache = 
				(int)(Math.sqrt(Math.pow(rayon_r3, 2)
				 - (Math.pow(distance_v, 2))))*2;
		int distance_supplementaire_trait = rayon_r3-mesure_trait_cache/2;

		g.setColor(Color.black);
		g.fillOval(getWidth/2-taille_rond1/2, hauteur_r1, taille_rond1,	
			taille_rond1);
		g.fillOval(getWidth/2-taille_rond2/2, hauteur_r2, taille_rond2, 
			taille_rond2);
		g.fillOval(getWidth/2-taille_rond3/2, hauteur_r3, taille_rond3, 
			taille_rond3);
		g.setColor(couleur_du_fantome);
		g.fillOval(getWidth/2-taille_rond1/2+epaisseur_de_trait,
				hauteur_r1+epaisseur_de_trait,
				taille_rond1-epaisseur_de_trait*2,
				taille_rond1-epaisseur_de_trait*2);
		g.fillOval(getWidth/2-taille_rond2/2+epaisseur_de_trait, 
				hauteur_r2+epaisseur_de_trait, 
				taille_rond2-epaisseur_de_trait*2, 
				taille_rond2-epaisseur_de_trait*2);
		g.fillOval(getWidth/2-taille_rond3/2+epaisseur_de_trait, 
				hauteur_r3+epaisseur_de_trait, 
				taille_rond3-epaisseur_de_trait*2, 
				taille_rond3-epaisseur_de_trait*2);
		g.setColor(couleur_de_fond);
		g.fillRect(0, hauteur_r4, getWidth, getHeight);
		g.setColor(Color.black);
		g.fillRect(getWidth/2-taille_rond3/2
				+distance_supplementaire_trait,
				hauteur_r4, mesure_trait_cache+2, 
				epaisseur_de_trait);
	}

// ######################### Sélection-désélection ############################ //
	
	public void select_deselect () {
		if (!selectionne) select();
		else deselect();		
	}
	
	public void inverse_couleur_fond () {
		int rouge = 255-couleur_de_fond.getRed();
		int vert = 255-couleur_de_fond.getGreen();
		int bleu = 255-couleur_de_fond.getBlue();
		couleur_de_fond = new Color(rouge, vert, bleu);
		repaint();
	}
	
	public void select () {
		if (selectionne) return;
		selectionne = true;
		inverse_couleur_fond();
	}
	public void deselect () {
		if (!selectionne) return;
		selectionne = false;
		couleur_de_fond = couleur_de_fond_tmp;
		repaint();
	}

// #################### Mse à jour de la case ################################# //

	public void mise_a_jour (CaseNonGraphique case1) {
		this.fantome_present = case1.fantome_present();
		//aff("mise a jour de la case : "+case1);
		if (fantome_present) {
			this.couleur_du_fantome_tmp = case1.getCouleur_du_fantome();
			mise_a_jour_couleur_du_fantome();
		}
		setCase_sortie(case1.getCase_sortie());
		repaint();
	}

	public void mise_a_jour_du_fond () {
		couleur_de_fond = couleur_de_fond_tmp;
		repaint();
	}

	public void mise_a_jour_couleur_du_fantome () {
		if (fantome_visible)
			couleur_du_fantome = couleur_du_fantome_tmp;
		else couleur_du_fantome = couleur_du_fantome_indetermine;
		repaint();
	}

// ########################## Dessin du panel ################################# //
	
	public void paintComponent (Graphics g) {
		int getWidth = this.getWidth();
		int getHeight = this.getHeight();
				
		Graphics2D g2 = (Graphics2D) g;
		
		/* Dessin du fond */
		g.setColor(couleur_de_fond);	
		g.fillRect(0,0,getWidth,getHeight);	
	
		/* Dessin du fantome si présent */
		if (fantome_present) {
			place_fantome(g, getHeight, getWidth);
		}
		
		/* Dessin des bords */
		g.setColor(Color.black);
		int epaisseur_des_traits_droite_et_bas = 0;
		if (CaseGraphique_selection_initiale) 
			epaisseur_des_traits_droite_et_bas = 1;
		g.drawRect(0, 0, getWidth-epaisseur_des_traits_droite_et_bas,
				getHeight-epaisseur_des_traits_droite_et_bas);
	}
	
// ########################## Getteurs et setteurs ############################ //

	public Color getCouleur_de_fond() {
		return couleur_de_fond;
	}
	public Color getCouleur_des_sorties() {
		return couleur_des_sorties;
	}
	public Color getCouleur_du_fantome() {
		return couleur_du_fantome;
	}
	public void setCouleur_de_fond(Color couleur_de_fond) {
		this.couleur_de_fond = couleur_de_fond;
		repaint();
	}
	public void setCouleur_des_sorties(Color couleur_des_sorties) {
		this.couleur_des_sorties = couleur_des_sorties;
		repaint();
	}
	public void setCouleur_du_fantome(Color couleur_du_fantome) {
		this.couleur_du_fantome = couleur_du_fantome;
		couleur_du_fantome_tmp = couleur_du_fantome;
		repaint();
	}
	public int [] getAdresse_CaseGraphique () {
		return adresse_CaseGraphique;
	}
	public boolean isCaseGraphique_selection_initiale () {
		return CaseGraphique_selection_initiale;
	}
	public void setCaseGraphique_selection_initiale(
			boolean CaseGraphique_selection_initiale) {
		this.CaseGraphique_selection_initiale = 
			CaseGraphique_selection_initiale;
	}
	public boolean isSelectionne() {
		return selectionne;
	}
	public String getPosition () {
		return (plateau.ConvertCoordonnees_String(adresse_CaseGraphique[0],
				adresse_CaseGraphique[1]));
	}
	public void setCase_sortie (boolean case_sortie) {
		if (this.case_sortie==case_sortie) return;
		this.case_sortie = case_sortie;
		if (case_sortie) {
			couleur_de_fond_tmp = couleur_des_sorties;
		}
		else {
			couleur_de_fond_tmp = couleur_de_fond_de_base;
		}
		mise_a_jour_du_fond();
	}
	public boolean getFantome_visible () {
		return fantome_visible;
	}
	public void setFantome_visible (boolean fantome_visible_tmp) {
		if (this.fantome_visible==fantome_visible_tmp) return;
		if (fantome_visible_tmp)
			couleur_du_fantome = couleur_du_fantome_tmp;
		else 
			couleur_du_fantome = couleur_du_fantome_indetermine;
		this.fantome_visible = fantome_visible_tmp;

		repaint();
	}
	public Color getCouleur_du_fantome_indetermine () {
		return couleur_du_fantome_indetermine;
	}
	public void setCouleur_du_fantome_indetermine (Color tmp) {
		couleur_du_fantome_indetermine = tmp;
	}

// ####################### Affichages de débuggage ############################ //
	
	public void affiche_adresse () {
		afftab(adresse_CaseGraphique);
		aff("CaseGraphique sélectionnée : "+selectionne);
	}
	
// ################### Fonctions utilitaires ################################## //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	public void afftab (int [] tab, String nom) {
		for (int i=0; i<tab.length; i++) {
			aff(nom+"["+i+"] = "+tab[i]);
		}
	}

	public void afftab (int [] tab) {
		for (int i=0; i<tab.length; i++) {
			aff("tab["+i+"] = "+tab[i]);
		}
	}

	public static int randInt (int min, int max) {
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}
}
