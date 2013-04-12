#!/usr/bin/perl -w

#Ce script sert à supprimer des caractères inutiles qui sont oncompatibles avec notre librairie de decisionTree
#On supprime aussi l'attribut TOPICS="YES" de chaque balise <REUTERS> car sinon il y' a un conflit pour le parserXML de perl ( même nom que la sous-balise <TOPICS> de <REUTERS>)
#De plus, on rajoute des balises <xml></xml> en début et fin de fichier pour obtenir un fichier xml validant les normes du w3c. (Nécéssité d'une balise englobante)

my @liste_sgm = <*test.sgm>;


foreach (@liste_sgm) {
	#On ouvre les fichiers en lecture et en écriture, la variable $_ contient le nom du fichier en entrée,
	#et on attribue à $nom le nom du fichier en sortie, c est-à-dire avec l extension modifiée de ".sgm" à ".pure.sgm"
	open(FILE,"<$_") or die("Erreur lors de l ouverture du fichier $_.");
	open(SORTIE,">$_.pure") or die("Erreur lors de l ouverture du fichier $_.");

	print SORTIE "<xml>\n";
	while(my $ligne = <FILE>) {
		$ligne =~ s/TOPICS="YES" //g;
		$ligne =~ s/&#[0-9]*;//g;
		print SORTIE $ligne;	
	}
	print SORTIE "\n</xml>";


}
