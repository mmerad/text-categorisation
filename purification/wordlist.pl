#!/usr/bin/perl -w

#####################################################################################
#      Script pour le projet de catégorisation de documents textuels (Reuters)      #
#                               INSA de Rennes - 4INFO                              #
#                            Florian Teyssier - Mars 2013                           #
#####################################################################################

############################################### MODULES ###############################################
#strict et warnings pour rendre Perl moins permissif
use strict;
use warnings;
#porter pour la racinisation, cf le fichier porter.pm

############################################### FONCTIONS ###############################################

#supprimer les doublons d'un array
sub uniq {
    return keys %{{ map { $_ => 1 } @_ }};
}

#La fonction qui sert à effectuer la purification (suppression des ponctuations sauf les points, suppression des mots inutiles, racinisation)
sub purification {
	my $ligne = shift;
	#NB : l'ordre d'appel des fonctions suivantes est important
	$ligne = lc($ligne);	#On passe tous les caractères en minuscules
	$ligne =~ s/\W/\n/g;		#On remplace les espaces multiples par des espaces simples
	
	$ligne =~ s/\n+/\n/g;	#On supprime les retours à la lignes consécutifs
	return $ligne;
}

############################################### SCRIPT ###############################################
#On récupère la liste des fichiers SGM à purifier (le nom est : n importe quoi suivi un chiffre suivi de ".sgm",
#ce qui fonctionne puisqu on changera l extension des fichiers modifiées de ".sgm" à ".pure.sgm")
my @liste_sgm = <*[0-9].sgm>;
my @liste_word;

#On vérifie si on a bien les droits en lecture sur les fichiers SGM et en écriture sur le dossier courant.
foreach (@liste_sgm) {
	die("Erreur : le programme n a pas autorisation en lecture sur $_.") if(!(-r $_));
}
die("Erreur : le programme n a pas autorisation en ecriture sur le dossier courant.") if(!( -w "./" ));

#On parcourt tous les fichiers pour en créer une version purifiée
foreach (@liste_sgm) {
	#On ouvre les fichiers en lecture et en écriture, la variable $_ contient le nom du fichier en entrée,
	#et on attribue à $nom le nom du fichier en sortie, c est-à-dire avec l extension modifiée de ".sgm" à ".pure.sgm"
	open(ENTREE,"<$_") or die("Erreur lors de l ouverture du fichier $_.");
	my $nom = $_;
	$nom =~ s/\.sgm/.words.sgm/;
	open(SORTIE,">$nom") or die("Erreur lors de la creation du fichier $nom.");
	
	#Définition de variables pour la boucle qui suit
	my $purifier = 0;	#Valeur a 1 si le texte devra être purifié, 0 sinon
	my $debut;			#Valeur tampon pour le début une ligne splitée
	my @reste;			#Tableau tampon pour le reste une ligne splitée
	#On parcourt toutes les lignes du fichier entrée
	while(my $ligne = <ENTREE>) {
		#On traite les cas particuliers des lignes où les balises suivantes apparaissent (seul le texte entre ces balises doit être purifié)
		#La méthode est la suivante : quand on trouve une balise ouvrante, on écrit dans le fichier ce qui était devant,
		#et on demande la purification pour le reste de la ligne ($purifier = 1); quand on trouve une balise fermante,
		#on purifie ce qui était devant avant de l écrire dans le fichier, et on stoppe la demande de purification pour le
		#reste de la ligne ($purifier = 0)
		while($ligne =~ m/<TITLE>/ or $ligne =~ m/<\/TITLE>/ or $ligne =~ m/<DATELINE>/ or $ligne =~ m/<\/DATELINE>/ or $ligne =~ m/<BODY>/ or $ligne =~ m/<\/BODY>/) {
			if($ligne =~ m/<TITLE>/)
			{
				($debut, @reste) = split(/<TITLE>/, $ligne);
				$ligne = join("<TITLE>", @reste);
				$purifier = 0;
			}
			elsif($ligne =~ m/<\/TITLE>/) {
				($debut, @reste) = split(/<\/TITLE>/, $ligne);
				$debut = purification($debut);
				$ligne = join("</TITLE>", @reste);
				$purifier = 0;
			}
			elsif($ligne =~ m/<DATELINE>/)
			{
				($debut, @reste) = split(/<DATELINE>/, $ligne);
				$ligne = join("<DATELINE>", @reste);
				$purifier = 0;
			}
			elsif($ligne =~ m/<\/DATELINE>/) {
				($debut, @reste) = split(/<\/DATELINE>/, $ligne);
				$debut = purification($debut);
				$ligne = join("</DATELINE>", @reste);
				$purifier = 0;
			}
			if($ligne =~ m/<BODY>/)
			{
				($debut, @reste) = split(/<BODY>/, $ligne);
				$ligne = join("<BODY>", @reste);
				$purifier = 1;
			}
			elsif($ligne =~ m/<\/BODY>/) {
				($debut, @reste) = split(/<\/BODY>/, $ligne);
				$debut = purification($debut);
				$ligne = join("</BODY>", @reste);
				$purifier = 0;
			}
		}
		#On écrit la ligne dans le fichier en l ayant au préalable purifié si nécessaire
		$ligne = purification($ligne) if($purifier) ;
		
		#print(SORTIE $ligne) if ($purifier);
		@liste_word = (@liste_word,split(/\n/, $ligne)) if ($purifier);
}
	

#@liste_word = uniq(@liste_word);

#my @tab = ("banane","banane","poisson","poisson");

#foreach (uniq(@tab)) {
#	print SORTIE $_."\n";
#}


foreach (uniq(@liste_word)) {
	print SORTIE $_."\n";
}

	#On ferme les fichiers entrée et de sortie
	close(ENTREE) or die("Erreur lors de la fermeture du fichier $_.");
	close(SORTIE) or die("Erreur lors de la fermeture du fichier $nom.");
}
