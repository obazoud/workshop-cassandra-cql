# Workshop Cassandra

## Installer Cassandra

Télécharger la version 1.2 de Cassandra à l'adresse http://www.apache.org/dyn/closer.cgi?path=/cassandra/1.2.4/apache-cassandra-1.2.4-bin.tar.gz

Ajoutez Cassandra dans le PATH

### Sous Linux/OSX

    $ export CASSANDRA_HOME=<...racine du répertoire Cassandra...>
    $ export PATH=$PATH:$CASSANDRA_HOME/bin

### Sous Windows

    set CASSANDRA_HOME=<...racine du répertoire Cassandra...>
    set PATH=%PATH%;%CASSANDRA_HOME\bin

## Réglages complémentaires

Pour activer le protocole CQL natif remplacez dans `$CASSANDRA_HOME/cassandra.yaml` :

    start_native_transport: true

Pour avoir plus d'un noeud en local et pouvoir inspecter le ring, il faut installer des outils complémentaires.

### Sous Linux (Debian)

Installer la commande `watch` si ce n'est pas déjà fait.

    % apt-get install watch

Installer Cassandra Cluser Manager

    % pip install ccm

### Sous OSX

Installer la commande `watch` si ce n'est pas déjà fait.

    $ sudo port install watch

Installer Cassandra Cluser Manager

    $ sudo port -v sync && sudo port -v install ccm

Pour plus d'informations : https://github.com/pcmanus/ccm

### Sous Windows

Padbol.


Sujet du Workshop
=================

Nous sommes une startup ambitieuse qui souhaite créer un spinoff de LastFM,
bien entendu supérieur.

Nous estimons que la demande nous apportera plusieurs millions d'utilisateurs
très rapidement. Nous avons donc choisi Cassandra, réputé pour ses performances
et sa scalabilité linéaire.

Le but de l'exercice est de faire passer les tests unitaires (dans l'ordre des
méthodes dans la classe).

Pour chaque exercice, vous allez devoir créer une column family. Puis y insérer
des données, et enfin les lire, en utilisant différentes techniques.


