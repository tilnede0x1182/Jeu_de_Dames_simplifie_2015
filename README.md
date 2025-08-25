# Jeu des Fantômes

Ce projet est une implémentation complète du jeu de plateau **Les Fantômes**, avec deux modes :
- une interface graphique interactive en **Java Swing**
- un mode non-graphique en **console**

## Règles du jeu

- Deux joueurs s'affrontent sur un plateau de **6x6 cases**
- Chaque joueur dispose de **4 bons fantômes** et **4 mauvais fantômes**, positionnés sur les deux premières rangées de son côté
- Les **bons fantômes** doivent sortir par l'une des **deux cases de sortie** (coin bas gauche et bas droit)
- Les **mauvais fantômes** sont un piège : s'ils sortent, cela fait perdre le joueur
- Un joueur gagne s’il parvient à :
  - faire sortir **3 de ses bons fantômes**
  - ou capturer les **4 bons fantômes adverses**
- Les joueurs ne connaissent pas la nature (bonne/mauvaise) des fantômes adverses
- Le jeu peut être joué **contre un autre joueur** ou **contre l'ordinateur**

## Fonctionnalités principales

- Interface graphique **Java Swing** avec icônes et couleurs personnalisées
- Mode triche pour afficher la nature des fantômes de l’adversaire
- Choix manuel ou automatique des bons/mauvais fantômes au départ
- Système de **sauvegarde et chargement** de parties
- Mode console sans interface graphique (utilisable dans un terminal)
- Menu interactif avec raccourcis clavier

## Architecture du projet

- `Jeu.java` : logique principale du déroulement d'une partie
- `Plateau.java` : structure du plateau, placements et mouvements
- `Case.java` : cases du plateau, avec état et coordonnées
- `Joueur.java` : gestion des pions d’un joueur et interactions
- `Fantome.java` : représentation des fantômes (bons ou mauvais)
- `Main.java` : point d'entrée de l’application graphique
- `InterfaceGraphique.java` : construction de la fenêtre Swing
- `SauvegardeManager.java` : sérialisation/chargement de parties

## Technologies utilisées

| Technologie           | Version utilisée |
|-----------------------|------------------|
| Java (JDK)            | 17               |
| Swing (Java SE)       | Standard          |
| AWT (Abstract Window Toolkit) | Standard  |
| Java I/O              | Standard          |

## Lancer le projet

### En mode graphique :

```bash
javac *.java
java Main
```

### En mode console (non graphique) :

```bash
javac *.java
java Jeu
```

## Raccourcis clavier (dans l’interface graphique)

| Touche        | Fonction                                      |
|---------------|-----------------------------------------------|
| `S`           | Sauvegarder la partie en cours                |
| `L`           | Charger une partie sauvegardée                |
| `R`           | Rejouer une nouvelle partie                   |
| `C`           | Activer/désactiver le mode triche             |
| `Échap`       | Quitter le jeu                                |

## Stratégies de jeu

1. **Bluffer en exposant volontairement des mauvais fantômes**
   Avancer des mauvais fantômes vers les sorties peut induire l’adversaire en erreur.

2. **Observer les réactions de l’adversaire**
   Un joueur prudent qui protège certains pions indique souvent qu’ils sont bons.

3. **Occuper les sorties tôt**
   Bloquer temporairement les sorties adverses avec ses propres mauvais fantômes peut ralentir leur victoire.

4. **Équilibrer attaque et défense**
   Garder au moins un ou deux bons fantômes en protection pendant qu’un ou deux autres tentent de sortir.

5. **Provoquer une prise volontaire**
   Laisser un bon fantôme en position vulnérable pour observer la réaction de l’adversaire et en déduire sa stratégie.

## Cadre du projet

Projet réalisé dans le cadre d’un exercice d'approfondissement Java orienté objet.

---
**Remarque** : Le projet ne nécessite aucune dépendance externe. Il peut être compilé et exécuté avec n'importe quelle version standard de Java 1.8 ou supérieure.
