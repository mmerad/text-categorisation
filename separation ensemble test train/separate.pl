#!/usr/bin/perl

# Ce script sert à séparer les fichier du corpus de test reuters en deux fichier : un fichier qui contient uniquement les articles qui vont servir à l'apprentissage, et un autre fichier qui contient 


############################################### MODULES ###############################################
use Data::Dumper;

#On récupère la liste des fichiers SGM à séparer en deux
my @liste_sgm = <*[0-9].sgm>;


############################################### SCRIPT ###############################################

#On vérifie si on a bien les droits en lecture sur les fichiers SGM et en écriture sur le dossier courant.
foreach (@liste_sgm) {
	die("Erreur : le programme n a pas autorisation en lecture sur $_.") if(!(-r $_));
}
die("Erreur : le programme n a pas autorisation en ecriture sur le dossier courant.") if(!( -w "./" ));


#On parcourt tous les fichiers
foreach (@liste_sgm) {
	
	#On ouvre les fichiers en lecture et en écriture, la variable $_ contient le nom du fichier en entrée,
	#et on attribue à $nom le nom du fichier en sortie, c est-à-dire avec l extension modifiée de ".sgm" à ".pure.sgm"
	my $nom = $_;
	open(ENTREE,"<$_") or die("Erreur lors de l ouverture du fichier $_.");
	my $test = $nom;
	my $train = $nom;

	print $nom."\n";

	$test =~ s/\.sgm/.test.sgm/;
	open(TEST,">$test") or die("Erreur lors de la creation du fichier $nom.");
	$train =~ s/\.sgm/.train.sgm/;
	open(TRAIN,">$train") or die("Erreur lors de la creation du fichier $nom.");
	
	#Définition de variables pour la boucle qui suit
	my $printtest = 0;	#Valeur a 1 si le texte est dans test, 0 sinon
	my $printtrain = 0;	#Valeur a 1 si le texte est dans train, 0 sinon
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
