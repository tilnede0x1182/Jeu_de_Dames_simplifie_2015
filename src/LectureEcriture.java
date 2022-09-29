import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
	Cette classe permet de lire et d'ecrire dans un fichier.
	@author Cyrille & Fatou
**/
public class LectureEcriture {
	private File file = null;
	/** Gère la lecture dans le ficher nom_du_fichier **/
	private BufferedReader lire_fichier;
	/** Gère l'écriture dans le ficher nom_du_fichier **/
	private BufferedWriter ecrire_fichier;
	/** Nom et chemin d'accès du fichier **/
	private String nom_du_fichier = "";
	/** mode d'ouverture : <br>
		r : read <br>
		w : write
	**/
	private char mode; 

	/**
		boolean ouvrir_enregistrer : <br>
		true : ouvrir <br>
		false : enregistrer
	**/
	public LectureEcriture (char mode) throws IOException {
		this("", mode);
	}

	public LectureEcriture (String nom_du_fichier, char mode) throws IOException {
		if (nom_du_fichier.isEmpty()) {
			if (mode!='r' && mode!='w') throw new IOException();
			if (mode=='r') {
				initialise_LE (chemin_file(true), mode);
			}
			else {
				initialise_LE (chemin_file(false), mode);
			}
		}
		else initialise_LE (nom_du_fichier, mode);
	}

	/**
		Initialise LectureEcriture.
		@param nom_du_fichier
		@param mode
	**/
	public void initialise_LE (String nom_du_fichier, char mode) throws IOException  {
		String message_derreur = "Aucun fichier selectionne.";
		if (nom_du_fichier==null) {
			throw new IOException(message_derreur);
		}
		if (nom_du_fichier.equals("null")) {
			throw new IOException(message_derreur);
		}
		if (nom_du_fichier.isEmpty()) {
			throw new IOException(message_derreur);
		}
		if (mode!='r' && mode!='w')
			throw new IOException("Mode de lecture/ecriture incorrect");
		this.nom_du_fichier = nom_du_fichier;
		this.mode  = mode;
		file = new File(nom_du_fichier);
		ouvrir();		
	}
	
// ################### Fonctions utilitaires de sauvegarde ##################### //

	/**
		boolean ouvrir_enregistrer : <br>
		true : ouvrir <br>
		false : enregistrer <br><br>
		
		@return le chemin du fichier selectionné.
	**/
	public String chemin_file (boolean ouvrir_enregistrer) {
		boolean bf1 = true;
		//String chaine="", ligne="";
		String tmp1="", tmp2="";
		String sep = ""+File.separatorChar;
		JFileChooser dialogue = null;
		FileDialog fDial = null;

		if (ouvrir_enregistrer) {
			File chosen_file=null;
			// création de la boîte de dialogue
			if (bf1) dialogue = new JFileChooser();
			//else fDial = new FileDialog(f2, "Ouvrir", FileDialog.LOAD);
			
			// affichage
			if (bf1) dialogue.showOpenDialog(null);
			else fDial.setVisible(true);
			
			// récupération du fichier sélectionné
			if (bf1) chosen_file = dialogue.getSelectedFile();
			else {
				tmp1 = fDial.getFile();
				if (tmp1!=null) tmp2 = fDial.getDirectory();
				chosen_file = new File(tmp2+sep+tmp1);
			}
			//aff("chosen_file = "+chosen_file);
			return ""+chosen_file;
		}
		else {
			File chosen_file = null;
			// création de la boîte de dialogue d'enregistrement d'un fichier
			JFileChooser dialogue2 = new JFileChooser();
				//dialogue2.setSelectedFile(f);
			// affichage de la boîte de dialogue2
			dialogue2.showSaveDialog(null);

			// récupération du fichier sélectionné par l'utilisateur
			chosen_file = dialogue2.getSelectedFile();
			//f2.setFichier_ouvert(chosen_file);
			//aff("Fichier choisi : " + chosen_file);
			return ""+chosen_file;
		}
	}
	
	/**
		Permet d'ouvrir le fichier en fonction du mode d'ouverture passer 
		en paramètre en prenant en compte le mode d'ouverture (r ou w).

		@param mode
	**/
	private void ouvrir () {
		try {
			if (this.mode=='r')
				lire_fichier = new BufferedReader(new FileReader(file));
			else if (this.mode=='w') {
				if (!file.exists()) {
					file.createNewFile();
				}
				ecrire_fichier = new BufferedWriter(new FileWriter(file));
			}
			else
				aff("le mode de lecture du fichier incorecte! ");	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
		Lit dans le fichier ouvert.
	**/
	public String lire () {
		String res = "", buffer ="";
		if (lire_fichier==null) {
			return "";
		}
		try {
			do {
				buffer = lire_fichier.readLine();
				if (buffer!=null)
					res+=buffer+"\n";
			}
			while (buffer!=null);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
		Ecrit dans le fichier.
		@param s0, flush
	**/
	public boolean ecrire (String s0, boolean flush) {
		if (s0==null) {
			aff("Attention, le buffer et null");
			return false;
		}
		if (s0.isEmpty()) {
			aff("Attention, le buffer et vide");
			return false;
		}
		// Gestion des erreurs

		try {
			ecrire_fichier.write(s0, 0, s0.length());
			if (!flush) return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			ecrire_fichier.flush();
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
		Ferme le fichier ouvert.
	**/
	public boolean fermer () {
		String error = "Impossible de fermer le fichier a l'adresse"+nom_du_fichier;
		if (this.mode=='r') {
			try {
				lire_fichier.close();
				return true;
			}
			catch (IOException e) {
				aff(error);
				e.printStackTrace();
				return false;
			}
		}
		else if (this.mode=='w') {
			try {
				ecrire_fichier.close();
				return true;
			}
			catch (IOException e) {
				
				aff(error);
				e.printStackTrace();
				return false;
			}
		}
		else return false;
	}

// ######################### Getteurs et setteurs ############################ //

	public String getNom_du_fichier () {
		return nom_du_fichier;
	}
	
// ######################## Fonctions utilitaires ############################ //

	public void aff (String s1) {
		System.out.println(s1);
	}

	public void affnn (String s1) {
		System.out.print(s1);
	}
}
