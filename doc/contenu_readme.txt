I) Format des fichiers .sgm

Les fichiers .sgm sont des fichiers XML.
Les éléments principaux de chaque fichier sont contenus dans des balises <REUTERS ... ></REUTERS>.

Cette balise comprends plusieurs attributs (5 ou 6):
- TOPICS : Valeurs possibles = {YES,NO,BYPASS}. REprésente s'il y avait un topic dans les articles originaux. NE VEUT PAS DIRE qu'il y a un topic dans cet élément REUTERS. (quelques exemples négatifs dû à des erreurs de saisie originelle). BYPASS dit que l'élément n'est pas catégorisé ( = pas trop utile pour nous).
- LEWISSPLIT : Valeurs possibles = {TRAINING,TEST,NOT-USED} représente dans quel ensemble est l'élément pour les travaux de LEWIS. 
- CGISPLIT : Valeur possibles = {TRAINING-SET,PUBLISHED-TESTSET} représente la même chose pour les travaux de HAYES.
- OLDID : ID ancien (Pas intéressant)
- ID : ID courant
- Parfois un sixième attributs qui ne sert à rien.

Chaque document REUTERS contient un certain nombre de balise
- <DATE></DATE> La date sous la forme JJ-MMM-AAAA HH:MM:SS (Le mois est constitué des 3 premières lettres anglaises (FEB)) Parfois un peu de texte non-relevant après.
- <MKNOTES> : Complètement inutiles (notes manuscrites de blabla)
- <TOPICS><D>fromage</D></TOPICS> : Listes des catégories : CHAQUE catégorie est dans des balises <D></D>
- <PLACES><D>insa</D></PLACES> : pareil, mais pour les lieux
- <PEOPLE><D>charles</D></PEOPLES> : pareil, mais pour les personnes
- <ORGS><D>worldbank</D></ORGS> : pareil, mais pour les orgnisations (wut ?)
- <EXCHANGES><D>nyse</D></EXCHANGES> : pareil, mais pour les marchés boursiers (nasdaq) (wut ?)
- <COMPANIES></COMPANIES> : jamais rien dans ces balises.
- <UNKNOWN></UNKNOWN> : Que du texte non-relevant dans ces baliseS.
- <TEXT>Lorem ipsum .. </TEXT> : Le texte du document, en anglais parfait (à tokeniser donc).

Chaque balise <TEXT> peut avoir un attribut (<TEXT TYPE="NORM">)
- TYPE : Valeurs possibles = {BRIEF,NORM,UNPROC} représente le "format" du texte. NORM est un texte normal, BRIEF est un texte de 1 ou 2 lignes, et UNPROC est un texte sans phrase (exemple : tableau)

Chaque balise TEXT peut avoir plusieurs sous-balises (pas forcément toutes définies
- <TITLE> :
- <AUTHOR> :
- <DATELINE> : lieu + date (exemple : HOUSTON, Feb 26 )
- <TEXT> : l'élément principal le plus intéréssant du texte

II) CATEGORIE

Plusieurs types de categorie possibles :
              Nombre de    Nombre de Categories   Nombre de Categories 
Category Set  Categories   avec 1+ textes dedans  avec 20+ textes dedans  
************  **********   ********************   ******************** 
EXCHANGES        39                32                       7
ORGS             56                32                       9
PEOPLE          267               114                      15
PLACES          175               147                      60
TOPICS          135               120                      57

La listes des categories est dispo dans les fichiers all-XXXX-strings.lc.txt dans le dossier reuters.

Beaucoup de categories apparaissent dans zeo documents mais il faut les garder pour mesurer l'efficacité de notre catégorisation.


