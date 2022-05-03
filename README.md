# Minesweeper
Github Repository: <https://github.com/jimmywengzj/Minesweeper>

### Qu'est-ce que Minesweeper ?
Minesweeper est un jeu de démineur dont le but est de localiser des mines cachées dans une grille représentant un champ de mines virtuel, avec pour seule indication le nombre de mines dans les zones adjacentes. 

### Comment jouer Minesweeper ?
Le joueur peut utiliser les indices des nombres de mines représentés dans le champ. 
On peut signaler les cases contenant des mines présumées par un drapeau en cliquant sur le bouton droit de la souris et de cliquer encore une fois sur le bouton droit pour changer le drapeau en marque de question. 

### Qu'est-ce qui rend notre jeu spécial ?
A part des fonctionnalités existantes dans Minesweeper classique, on a enrichi l’extensibilité et la fonction de ce jeu. On a aussi créé une IA pour vous donner des astuces !

### Comment paramétrer Minesweeper ?
  * Si vous voulez changer la difficulté du jeu, allez dans **Jeu** et sélectionner parmi les options **Débudant**, **Intermédiare**, **Expert**, ou **Personnalisé**. Dans l'option Personnalisé, vous pouvez changer la taille de la carte ainsi le nombre de mines.
  * Pour changer la langue, allez dans **Option-Language** et sélectionner la langue de préférence.
  * Si vous souhaitez essayer différents scénarios du jeu, accédez à **Option-Pack de Textures** de et sélectionnez les thèmes **Mario**, **Minecraft**, **XP** ou **Windows 7**.
  * Pour changer la taille de la fenêtre, allez dans **Option-Taille de la fenêtre** et changez la taille de la fenêtre selon vos besoins.

### Comment utiliser l'IA ?
Si vous êtes bloqué, cliquez sur le bouton IA dans la barre de menu, et cliquez sur le bouton Astuce. Une notification popup apparaîtra, soit en mettant en évidence la zone à prendre en compte, soit en indiquant qu'il n'y a pas de case à déterminer. S'il n'y a pas de cellule à déterminer, une autre fenêtre apparaîtra, indiquant le taux de probabilité de chaque cellule.  

### File System
```
Minesweeper
├───README.md
├───options.properties
├───src
|   ├───Minesweeper.java
|   ├───Gui.java
|   ├───Game.java
|   ├───Options.java
|   ├───Language.java
|   ├───Solver.java
|   └───util
|       ├───Pair.java
|       └───NameArg.java
├───resources
|   ├───xp
|   ├───win7
|   ├───mario
|   └───minecraft
└───language
    ├───En.properties
    ├───Fr.properties
    └───Zh.tpropertiesxt
```
