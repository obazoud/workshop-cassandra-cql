Workshop Cassandra
======================

Installer Cassandra
========

La version 1.2 de Cassandra :  http://www.apache.org/dyn/closer.cgi?path=/cassandra/1.2.4/apache-cassandra-1.2.4-bin.tar.gz

Ajoutez CASSANDRA_HOME (racine du répertoire Cassandra) dans l'environnement et
    $CASSANDRA_HOME/bin (%CASSANDRA_HOME%/bin pour Windows)
dans le path.

Pour activer le protocole CQL natif remplacez dans $CASSANDRA_HOME/cassandra.yaml :

    start_native_transport: true

Pour avoir plus d'un noeud en local et pouvoir inspecter le ring :

MacOS : sudo port install watch
Linux (debian) : apt-get install watch

Installer le Cassandra Cluster Manager :

MacOS : sudo port -v sync && sudo port -v install ccm
Linux : pip install ccm

Pour plus d'informations : https://github.com/pcmanus/ccm


Sujet du Workshop
========

Nous sommes une petite startup qui souhaite créer un spinoff de LastFM qui sera bien entendu bien supérieur,
et qui risque donc d'avoir rapidement des millions d'utilisateurs. Nous avons donc choisi Cassandra dont on a entendu
qu'il était très performant et linéaire.

Le but de l'exercice est de faire passer les tests unitaires (dans l'ordre des méthodes dans la classe).

Pour chaque exercice, vous allez devoir créer une column family. Puis y insérer des données, et enfin les lire,
en utilisant différentes techniques.







