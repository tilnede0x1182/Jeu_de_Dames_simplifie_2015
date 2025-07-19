# Jeu des Fantômes

- Ce projet est une implémentation complète du jeu de plateau "Les Fantômes", conçu pour être joué en mode graphique ou non-graphique.
- Deux joueurs s'affrontent sur un plateau 6x6 en déplaçant des pions (fantômes) bons ou mauvais.
- Le but du jeu est de capturer tous les bons fantômes adverses ou de faire sortir ses bons fantômes par les cases de sortie.
- Une interface graphique Swing permet une interaction visuelle fluide avec les composants du jeu.
- Le projet inclut une IA rudimentaire pour permettre des parties contre l'ordinateur.
- Un système de sauvegarde et de chargement de parties est intégré, avec une interface de dialogue graphique.
- Un "mode triche" peut être activé pour afficher les fantômes de l'adversaire.
- L'interface graphique gère la sélection initiale des pions, les déplacements et les retours visuels (couleurs, icônes).
- Le code est structuré autour de classes modulaires : `Jeu`, `Plateau`, `Case`, `Joueur`, `Fantôme`, etc.
- Des tests simples de fonctionnement peuvent être exécutés en mode terminal ou via une relance par l'interface graphique.

## Technologies utilisées

| Technologie           | Version utilisée |
|-----------------------|------------------|
| Java (JDK)            | 17               |
| Swing (Java SE)       | Standard          |
| AWT (Abstract Window Toolkit) | Standard  |
| I/O Java              | Standard          |

## Fonctionnalités

- Plateau de jeu 6x6 avec gestion des coordonnées en notation échiquéenne (ex: a1, f6)
- Deux modes de jeu : graphique (interface Swing) et non-graphique (console)
- Choix des fantômes (bons/mauvais) en mode manuel ou automatique
- Déplacement des pions avec détection des mouvements valides
- Confrontation entre fantômes avec règles de capture (en fonction de leur nature)
- Victoire par sortie de trois bons fantômes ou élimination adverse
- Sauvegarde et chargement de parties avec format texte structuré
- Interface graphique dynamique : gestion des couleurs, bordures, états de sélection
- Mode triche : permet d'afficher les fantômes adverses
- Interface de menu avec raccourcis clavier pour les opérations fréquentes
