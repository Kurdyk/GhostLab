##Projet de programmation réseau

### Equipe 40

- JOSSO Victor
- KURDYK Louis
- MORAND Paul-Emile

### Exécution

Sur les salles de TP de la fac faire :

- `make dependencies` pour récupérer JavaFX nécessaire au client.
- `source univ.sh` pour ajouter au PATH le chemin d'accès de Java 17 (à faire pour chaque terminal).

`make client` et `make serveur` permettent de build et de lancer respectivement le client et le serveur.<br><br>
Pour ne faire que build : 
- `make build_client` pour le client.
- `make build_serveur` pour le serveur.<br><br>

Pour ne faire que lancer :
- `make run_client` pour le client.
- `make run_serveur` pour le serveur.<br><br>

Faire `make clean` pour supprimer les dépendances et les out.

### Architecture du code

#### Côté client

#### Côté serveur

Les fichiers sources sont séparés en 3 répertoires :

- Apps : contient la classe qui accepent la connexion 
- Models : contient le nécessaire à la gestion d'une partie (le plateau, les cases, les joueurs, les fantômes...)
- Utils : contient des classes utilitaires (comme le parser qui reçoit et lit les requêtes, le writer) 
ainsi que des classes pour les requetes et le serveur de streaming audio.