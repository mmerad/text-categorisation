#!/usr/bin/perl


use Data::Dumper;
#print Dumper  @liste_sgm;


#On récupère la liste des fichiers SGM à purifier (le nom est : n importe quoi suivi un chiffre suivi de ".sgm",
#ce qui fonctionne puisqu on changera l extension des fichiers modifiées de ".sgm" à ".pure.sgm")
my @liste_sgm = <*[0-9].sgm>;


#On vérifie si on a bien les droits en lecture sur les fichiers SGM et en écriture sur le dossier courant.
foreach (@liste_sgm) {
	die("Erreur : le programme n a pas autorisation en lecture sur $_.") if(!(-r $_));
}
die("Erreur : le programme n a pas autorisation en ecriture sur le dossier courant.") if(!( -w "./" ));

#On parcourt tous les fichiers pour en créer une version purifiée
foreach (@liste_sgm) {
	
	my $nom = $_;
	#On ouvre les fichiers en lecture et en écriture, la variable $_ contient le nom du fichier en entrée,
	#et on attribue à $nom le nom du fichier en sortie, c est-à-dire avec l extension modifiée de ".sgm" à ".pure.sgm"
	open(ENTREE,"<$_") or die("Erreur lors de l ouverture du fichier $_.");
	my $test = $nom;
	my $train = $nom;

	print $nom."\n";

	$test =~ s/\.sgm/.test.sgm/;
	open(TEST,">$test") or die("Erreur lors de la creation du fichier $nom.");
	$train =~ s/\.sgm/.train.sgm/;
	open(TRAIN,">$train") or die("Erreur lors de la creation du fichier $nom.");
	
	#Définition de variables pour la boucle qui suit
	my $printtest = 0;	#Valeur a 1 si le texte devra être purifié, 0 sinon
	my $printtrain = 0;	#Valeur a 1 si le texte devra être purifié, 0 sinon
	my $debut;			#Valeur tampon pour le début une ligne splitée
	my @reste;			#Tableau tampon pour le reste une ligne splitée
	#On parcourt toutes les lignes du fichier entrée
	while(my $ligne = <ENTREE>) {
		if($ligne =~ m/<REUTERS TOPICS="YES" LEWISSPLIT="TRAIN"/) {
			$printtrain = 1;
		}
		elsif($ligne =~ m/<REUTERS TOPICS="YES" LEWISSPLIT="TEST"/) {
			$printtest = 1;
		}
		elsif($ligne =~ m/<\/REUTERS>/){
			if($printtest) {
				print TEST $ligne;
			}
			if($printtrain) {
				print TRAIN $ligne
			}
			$printtest = 0;
			$printtrain = 0;
		}
		
		if($printtest) {
			print TEST $ligne;
		}
		if($printtrain) {
			print TRAIN $ligne
		}
	}
	
	#On ferme les fichiers entrée et de sortie
	close(ENTREE) or die("Erreur lors de la fermeture du fichier $_.");
	close(TEST) or die("Erreur lors de la fermeture du fichier $test.");
	close(TRAIN) or die("Erreur lors de la fermeture du fichier $train.");
}
