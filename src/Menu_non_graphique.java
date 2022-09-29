import java.util.Scanner;
import java.util.ArrayList;

public class Menu_non_graphique {
	/**
		Retourne -1 en cas d'erreur.
	**/
	public int menu (String nom, String message, 
			ArrayList<ArrayList<String>> menu) {
		int menu_size = menu.size();

		if (menu==null) {
			aff("Le tableau du menu est null");
			return -1;
		}
		if (menu.size()==0) {
			aff("Le tableau du menu est vide");
			return -1;
		}
		for (int i=0; i<menu_size; i++) {
			if (menu.get(i).size()!=2)  {
				aff("Le tableau interne de la case "+i+
				" n'a pas un taille de 2.");
				return -1;
			}
			if (!isInteger(menu.get(i).get(1)))  {
				aff("Le tableau interne de la case "+i+
				" ne contient pas un entier en case 1.");
				return -1;
			}
		}// On vérifie que le tableau contient bien ce que l'on veut : 
		// des String dans les cases menu.get(i).get(0)
		// et des int dans les cases menu.get(i).get(1).

		if (!nom.isEmpty()) nom+=" ";
		int cmp=0, res = -1;
		while (res<1 || res>menu_size) {
			if (cmp%10==0) {
				if (cmp!=0) aff("");
				if (!nom.isEmpty())
					aff("Menu "+nom+":");
				if (message.isEmpty()) aff("");
				if (!message.isEmpty())
					aff(message+"\n");
				for (int i=0; i<menu_size; i++) {
					aff((i+1)+" : "+menu.get(i).get(0));
				}
			}
			res = entrer_entier();
			cmp++;
		}
		res--;
		return (Integer.parseInt(menu.get(res).get(1)));
	}

	public int menu (String nom, ArrayList<ArrayList<String>> menu) {
		return menu(nom, "", menu);
	}

// ################### Fonctions utilitaires ################################## //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}

	public boolean isInteger (String n0) {
		try {
			int n1 = Integer.parseInt(n0);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public int entrer_entier () {
		affnn("? = ");
		Scanner sc = new Scanner(System.in);
		String res = "";

		res = sc.nextLine();
		while (!isInteger(res)) {
			aff("Veuillez entrer en entier : ");
			affnn("? = ");
			res = sc.nextLine();
		}

		return Integer.parseInt(res);
	}

// ##################### Génération du menu ################################## //

	public int menu_principal () {
		ArrayList<ArrayList<String>> menuL = 
			new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp = new ArrayList<>();
		tmp.add("Nouveau jeu");
		tmp.add("1");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Ouvrir une partie sauvegardee");
		tmp.add("2");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Quitter");
		tmp.add("3");
		menuL.add(tmp);
		return menu("principal", menuL);
	}	

	public int menu_nouveau_jeu () {
		ArrayList<ArrayList<String>> menuL = 
			new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp = new ArrayList<>();
		tmp.add("Jeu solo");
		tmp.add("1");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Jeu deux joueurs");
		tmp.add("2");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Voir l'ordinateur s'affronter lui-meme "+
		"(niveau aleatoire)");
		tmp.add("3");
		menuL.add(tmp);
		return menu("nouveau jeu", menuL);
	}

	public int menu_haut_bas () {
		ArrayList<ArrayList<String>> menuL = 
			new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp = new ArrayList<>();
		tmp.add("En haut du plateau");
		tmp.add("1");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("En bas du plateau");
		tmp.add("2");
		menuL.add(tmp);
		return menu("", "Voulez-vous etre situe : ", menuL);
	}

	public int menu_tour_de_jeu () {
		ArrayList<ArrayList<String>> menuL = 
			new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp = new ArrayList<>();
		tmp.add("Jouer");
		tmp.add("1");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Sauvegarder la partie");
		tmp.add("2");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Quitter la partie");
		tmp.add("3");
		menuL.add(tmp);
		return menu("", "Voulez-vous : ", menuL);
	}

	public int menu_mode_triche () {
		ArrayList<ArrayList<String>> menuL = 
			new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp = new ArrayList<>();
		tmp.add("Non");
		tmp.add("2");
		menuL.add(tmp);
		tmp = new ArrayList<>();
		tmp.add("Oui");
		tmp.add("1");
		menuL.add(tmp);
		return menu("", "Voulez-vous activer le mode triche ?", menuL);
	}
}