
# Projet IA Gomoku
**Fonctionnalités de notre IA :**
- Negamax
- Coupe alpha beta
- Limitation de l'aire de jeu à une distance de 2 cases des pions
- Tri des coups dans le Negamax
- Recherche itérative (quand une profondeur est finie, on relance le negamax plus loin)¹


*¹ : Ici, le Negamax est relancé completement sans sauvegarder les calculs déjà effectués*


**Packages :**
- <> : contient la boucle de jeu
- benchmark : contient des benchmark pour la fonction d'eval et les minmax
- controllers : abstraction des controleurs
- controllers.ai : contient les versions des minmax
- controllers.eval : contient les fonctions d'évaluation
- gamecore : gestion du jeu (plateau)
- gamecore.enums : contient les enums utilisés (joueur, couleur..)
- genetic : contient un algo génétique permettant de calculer des poids pour la fonction d'évaluation
- test : classes de tests (fonction d'eval, minmax)





## Auteurs

- Chavasse-Leroy Clément
- Veysseire Ronan

Code de la gestion du jeu fourni par Cohen Thomas.

