
# Tetris

#Etudiant: 
Fares IBEGAZENE TCSE3


#Présentation:

* Ce programme est écrit en java sous un système Linux Ubuntu 15.10
* Le programme que nous avons réalisé est une reproduction du jeu tetris conçu par Alekseï Pajitnov à partir de juin 1984 



#Fonctionmment:

* Le principe de tetris consiste à poser des pièces de différentes formes dans une grille.
* Les lignes complétées sont supprimées et l'espace qu'elles occupent est libéré.
* Un score est calculé en fonction de nombre de lignes completée (chaque ligne completée vaut 50 points)
* Un fichier est utilisé pour stocker les meilleurs scores, ce fichier est chargé à chaque nouvelle partie et est mis à jour à chaque fin de partie
* Le joueur perd la partie quand il ne reste plus de place dans la grille pour mettre une nouvelle pièce en jeu. 

#Génération du jar:

Pour générer le jar sous linux:
* Nous nous plaçons dans le répértoire de projet en utilisant la commande cd ...../src/
* Nous lançons la commande jar cvf tetris.jar Tetris.class 

#Lancement du jeu:

Pour lancer l'application sous linux:
* Nous nous plaçons dans le répértoire de projet en utilisant la commande cd ...../src/
* Nous lançons la commande java Tetris 

#Architecture

L'architecture que nous avons adopté est une architecture modulaire constituée de trois modules principaux qui sont:
* Module Forme: Ce module contient la définition et la création des différents types de formes utilisées dans le jeu.

* Module Surface: Contient la definition de la surface de jeu, ainsi que les différentes méthodes de contrôle et de manipulation de jeu, ainsi que les évenements clavier résponsables de manipulation des pièces mise en jeu. Il permet aussi de gérer et de contrôler la grille.

* Module Graphique: Ce module permet de créer le design principal de jeu et de manipuler la gestion des scores. 


#Les commandes de manipulation:

Les commandes de manipulation et de contrôle de jeu sont réalisées à partir des touches de clavier, les différentes touches permettant de réaliser ces manipulations sont les suivantes :

* Pour deplacer à gauche appuyez sur <--
* Pour deplacer à doite appuyez sur -->
* Pour accélerer le défilement de la pièce vers le bas apuyez sur la touche « d »
* Pour déposer la pièce directement sur la grille appuyez sur la flèche « bas » .
* Pour retourner la pièce appuyez sur la flèche « haut ».
* Pour suspendre le jeux appuyez sur la touche "p".

#SOLID

Dans notre conception nous avons appliqué certains principes « SOLID » que nous pouvons résumer dans les points suivants :

* Principe de cohésion

Les différents modules que nous avons programmé sont complétement cohésifs, ainsi chaque module est déstiné à réaliser une seule tâche et a une résponsabilité unique.

* Principe de couplage

Les différents modules de notre programme ont un faible niveau de couplage, ainsi chaque module est totalement indépendant des autres modules, ce qui facilite la modification individuelle de chaque module sans être obligé de modifier les autres. 

#Diagramme Classes


PS: si lien ne fonctionne pas voir l'image dans le GitHub

<img src = "https://drive.google.com/file/d/0B6grC72fbryRdUIxWlZqZUNFd0E/view?usp=sharing" title = "Diagrame classes" alt = "diagramme classes">

<img src = "https://drive.google.com/open?id=0B6grC72fbryRMnE5M3FRNVBDLXc" title = "diagramme classes" alt = "Diagramme classes">

<img src = "https://drive.google.com/open?id=0B6grC72fbryROEtJOUF2dnRKVmM" title = "diagramme classes" alt = "Diagramme classes">

