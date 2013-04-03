#!/usr/bin/perl

$line = "19 yes no yes no yes yes yes yes yes no yes no no no no no yes no no no no no no yes no yes no yes no no yes yes yes no yes yes no yes yes no yes no no yes yes no yes yes yes no yes no yes no no yes yes yes no yes yes no no no no no no no yes no no yes no no yes no yes yes yes no no yes yes no no yes no no yes no yes yes no no yes no yes no yes yes no no yes yes yes no no no no no yes no yes yes no no no yes no yes no yes yes yes yes no no yes no yes yes yes yes no no no wheat";

@A = split (/ /,$line);

print length(@A)."\n";

use Data::Dumper;
print Dumper @A;
