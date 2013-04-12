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

use XML::Simple;


############################################### FONCTIONS ###############################################
#verifie que ce qui est donné en paramètre est un array
sub is_array {
  my ($var) = @_;
  return ( ref($var) eq 'ARRAY' );
}

#supprimer les doublons d'un array
sub uniq {
    return keys %{{ map { $_ => 1 } @_ }};
}

#supprimer les doublons d'un array
sub uniq {
    return keys %{{ map { $_ => 1 } @_ }};
}

#Fonction racinant tous les mots
sub racinisation {
	my $ligne = shift;
	#Le raciniseur "porter" semble supprimer certains retours à la lignes, on s'assurera avec ces variables de les maintenir
	my ($n, $r) = (0, 0);
	$n = 1 if($ligne =~ m/.*\n$/);
	$r = 1 if($ligne =~ m/.*\r$/);
	#Création un tableau contenant tous les mots de la ligne
	my @mots = split(/ /,$ligne);
	my @mots_racines;
	#Parcours des mots à raciniser
	foreach (@mots) {
		#Utilisation du raciniseur "porter", et ajout au tableau des mots racinisés
		push(@mots_racines,porter($_));
	}
	#Reconstruction une ligne avec les mots raciniés
	$ligne = join(" ",@mots_racines);
	#On s'assure que les retours à la ligne sont toujours présents
	$ligne = $ligne."\n" if($n && !($ligne =~ m/.*\n$/));
	$ligne = $ligne."\r" if($r && !($ligne =~ m/.*\r$/));
	
	return $ligne;
}

#Liste de mots que l'on considèrera comme inutiles (ainsi que des suffixes/extensions type "ll" pour "will"),
#d'autres mots pourront être ajoutés à la liste pour éliminer les mots que l'on aura empiriquement déterminés comme inutiles
my @mots_inutiles = qw/able about across after ain all almost also am among an and any are aren as at be because been but by can cannot could couldn dear did didn do does doesn don either else ever every for from get got had has hasn have he her hers him his how however i if in into is isn it its just least let like likely may me might mightn most must mustn my neither no nor not of off often on only or other our own rather said say says shan she should shouldn since so some than that the their them then there these they this tis to too twas us wants was wasn we were weren what when where which while who whom why will with won would wouldn yet you your ll ve re a b c d e f g h i j k l m n o p q r s t u v w x y z/;
#Fonction supprimant les mots inutiles
sub suppression_inutiles {
	my $ligne = shift;
	#Ma méthode semble supprimer certains retours à la lignes, on s'assurera avec ces variables de les maintenir
	my ($n, $r) = (0, 0);
	$n = 1 if($ligne =~ m/.*\n$/);
	$r = 1 if($ligne =~ m/.*\r$/);
	#Parcours des mots inutiles
	foreach (@mots_inutiles) {
		#Suppression du mot courant s'il apparait dans la ligne
		$ligne =~ s/^$_\W+/ /g;
		$ligne =~ s/\W+$_\W+/ /g;
	}
	#On s'assure que les retours à la ligne sont toujours présents
	$ligne = $ligne."\n" if($n && !($ligne =~ m/.*\n$/));
	$ligne = $ligne."\r" if($r && !($ligne =~ m/.*\r$/));
	
	return $ligne;
}

#La fonction qui sert à effectuer la purification (suppression des ponctuations sauf les points, suppression des mots inutiles, racinisation)
sub purification {
	my $ligne = shift;
	#NB : l'ordre d'appel des fonctions suivantes est important
	$ligne = lc($ligne);						#On passe tous les caractères en minuscules
	#$ligne = suppression_inutiles($ligne);		#On supprimme les mots "inutils"
	#$ligne = racinisation($ligne);				#On racinise les mots
	$ligne =~ s/\W+/\n/g;						#On remplace les espaces blancs par des retour à la ligne
	$ligne =~ s/\d+//g; 						#On supprime tous les chiffres
	$ligne =~ s/\n+/\n/g;						#On supprime les retours à la lignes consécutifs

	return $ligne;
}


############################################### VARS #################################################
my $nom;
#nom du fichier
my $nom_entry;
#parser XML
my $parser = XML::Simple->new( KeepRoot => 1 );


############################################### SCRIPT ###############################################
#On récupère la liste des fichiers SGM à purifier (le nom est : n importe quoi suivi un chiffre suivi de ".sgm",
#ce qui fonctionne puisqu on changera l extension des fichiers modifiées de ".sgm" à ".pure.sgm")
my @liste_train = <*train.sgm.pure>;
my @liste_test = <*test.sgm.pure>;
my @liste_word;
my @uniq_wordlist;

#On vérifie si on a bien les droits en lecture sur les fichiers SGM et en écriture sur le dossier courant.
foreach (@liste_train) {
	die("Erreur : le programme n a pas autorisation en lecture sur $_.") if(!(-r $_));
}
die("Erreur : le programme n a pas autorisation en ecriture sur le dossier courant.") if(!( -w "./" ));

foreach (@liste_test) {
	die("Erreur : le programme n a pas autorisation en lecture sur $_.") if(!(-r $_));
}
die("Erreur : le programme n a pas autorisation en ecriture sur le dossier courant.") if(!( -w "./" ));

#On parcourt tous les fichiers pour en créer une version purifiée
foreach (@liste_train) {
	#On ouvre les fichiers en lecture et en écriture, la variable $_ contient le nom du fichier en entrée,
	#et on attribue à $nom le nom du fichier en sortie, c est-à-dire avec l extension modifiée de ".sgm" à ".pure.sgm"
	open(ENTREE,"<$_") or die("Erreur lors de l ouverture du fichier $_.");
	$nom = $_;
	$nom_entry = $_;
	$nom =~ s/\.sgm\.pure/.train.db/;
	open(SORTIE,">$nom") or die("Erreur lors de la creation du fichier $nom.");

	#open(WORD,">listemots.txt") or die();
	
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
		while($ligne =~ m/<BODY>/ or $ligne =~ m/<\/BODY>/) {
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
		
		#on ajoute au t ableau de la liste des mots si c'est bon.
		@liste_word = (@liste_word,split(/\n/, $ligne)) if ($purifier);
	}
	#SORTIE si on veut uniquement la liste des mots
	#foreach (uniq(@liste_word)) {
	#	print SORTIE $_."\n";
	#}
	
	#Cette routine sert à supprimer les mots videsd de la listes de mots
	@uniq_wordlist = uniq(@liste_word);
	my $i =0;

	++$i until $uniq_wordlist[$i] eq '' or $i > $#uniq_wordlist;
	print $i;
	splice(@uniq_wordlist,$i,1);
	#fin routine

	#On crée le fichier pour JadTI

	#Première ligne, nom de la database
	print SORTIE $nom."\n";

	#impression de la deuxième ligne tous les mots suivis de symbolic
	print SORTIE "object name";
	foreach (@uniq_wordlist) {
		print SORTIE " ".$_." symbolic";
	}
	print SORTIE " topic symbolic\n";
	open(ENTREE,"<$nom_entry") or die("Erreur lors de l ouverture du fichier $nom_entry.");
	my $doc = $parser->XMLin("$nom_entry");

	foreach my $reuters ( @{ $doc->{xml}->{REUTERS} } ) {
	  my @liste_topics;
	  my $reutersbody = purification($reuters->{TEXT}->{BODY});

use Data::Dumper;
#print Dumper  @{ $reuters->{TOPICS}->{D} };

		if(is_array($reuters->{TOPICS}->{D})) {
			@liste_topics =  @{ $reuters->{TOPICS}->{D} };
		}
		else
		{
			@liste_topics = $reuters->{TOPICS}->{D};
		}
	  #if($reuters->{NEWID} == 14088){
	#	print"\n\nid";
	#	print Dumper @liste_topics;
	#	print scalar(@liste_topics);
	#	print "id\n\n ";
	#	}

		if(defined($liste_topics[0])){
			foreach my $topic ( @liste_topics ) {
				print SORTIE $reuters->{NEWID};
				foreach my $mot (@uniq_wordlist) {
					my $presence = "no";
					if (index($reutersbody, $mot) != -1) {
						$presence = "yes";
					}
					print SORTIE " ".$presence;
				}
				print SORTIE " ".$topic."\n";
			}
		}

	}
	#saut de ligne final
	print SORTIE "\n";
	

	#On ferme les fichiers entrée et de sortie
	close(ENTREE) or die("Erreur lors de la fermeture du fichier $_.");
	close(SORTIE) or die("Erreur lors de la fermeture du fichier $nom.");
}

foreach (@liste_test) {
	#On ouvre les fichiers en lecture et en écriture, la variable $_ contient le nom du fichier en entrée,
	#et on attribue à $nom le nom du fichier en sortie, c est-à-dire avec l extension modifiée de ".sgm" à ".pure.sgm"
	open(ENTREE,"<$_") or die("Erreur lors de l ouverture du fichier $_.");
	$nom = $_;
	$nom_entry = $_;
	$nom =~ s/\.sgm\.pure/.test.db/;
	open(SORTIE,">$nom") or die("Erreur lors de la creation du fichier $nom.");

	#open(WORD,">listemots.txt") or die();

	#Première ligne, nom de la database
	print SORTIE $nom."\n";

	#impression de la deuxième ligne tous les mots suivis de symbolic
	print SORTIE "object name";
	foreach (@uniq_wordlist) {
		print SORTIE " ".$_." symbolic";
	}
	print SORTIE " topic symbolic\n";
	open(ENTREE,"<$nom_entry") or die("Erreur lors de l ouverture du fichier $nom_entry.");
	my $doc = $parser->XMLin("$nom_entry");

	foreach my $reuters ( @{ $doc->{xml}->{REUTERS} } ) {
	  my @liste_topics;
	  my $reutersbody = purification($reuters->{TEXT}->{BODY});

		if(is_array($reuters->{TOPICS}->{D})) {
			@liste_topics =  @{ $reuters->{TOPICS}->{D} };
		}
		else
		{
			@liste_topics = $reuters->{TOPICS}->{D};
		}

		if(defined($liste_topics[0])){
			foreach my $topic ( @liste_topics ) {
				print SORTIE $reuters->{NEWID};
				foreach my $mot (@uniq_wordlist) {
					my $presence = "no";
					if (index($reutersbody, $mot) != -1) {
						$presence = "yes";
					}
					print SORTIE " ".$presence;
				}
				print SORTIE " ".$topic."\n";
			}
		}

	}
	#saut de ligne final
	print SORTIE "\n";
	

	#On ferme les fichiers entrée et de sortie
	close(ENTREE) or die("Erreur lors de la fermeture du fichier $_.");
	close(SORTIE) or die("Erreur lors de la fermeture du fichier $nom.");
}
