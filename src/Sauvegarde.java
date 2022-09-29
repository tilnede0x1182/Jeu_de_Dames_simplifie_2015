import java.io.IOException;
import java.util.ArrayList;

public class Sauvegarde {
	/**
		Sauvegarde les coups du jeu dans le fichier nom_fichier <br>
		@param nom_fichier, coups
	**/
	public String enregistrer (String nom_fichier, ArrayList<String> coups, Jeu jeu) {
		int i=0, coups_size = coups.size();
		String res = "";
		LectureEcriture le = null;

		try {
			le = new LectureEcriture('w');
		}
		catch (IOException e) {
			//e.printStackTrace();
			aff("\n"+e.getMessage()+"\n");
			return "";
		}

		nom_fichier = le.getNom_du_fichier();
		res+=jeu.getTypeJeu()+"\n";
		res+="# Player 1\n";
		res+=coups.get(0)+"\n\n";
		res+="# Player 2\n";
		res+=coups.get(1)+"\n\n";
		res+="# Move\n";

		for (i=2; i<coups_size-1; i+=2) {
			res+=coups.get(i);
			res+=coups.get(i+1)+"\n";
		}
		if(i==coups_size-1)
			res+=coups.get(i);
		if (!le.ecrire(res, true)) return "";
		le.fermer();
		aff("\n"+res+"\n");

		return nom_fichier;
	}

// ################### Fonctions utilitaires ################################# //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}
}
