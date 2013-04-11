#!/usr/bin/perl -w

my @liste_sgm = <*test.sgm>;

print @liste_sgm;

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
