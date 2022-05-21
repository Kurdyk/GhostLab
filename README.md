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

La classe `ConnectionHandler` gère la connexion TCP principale avec le serveur. 
On peut lui demander d'enregistrer un "callback", c'est à dire un triplet
(`mot_clé`, `instance`, `pointeur_de_fonction`). L'instance doit hériter de la 
classe `CallbackInstance`, et surcharger la méthode donnée en dernier paramètre.
De cette façon, dès que le `connectionHandler` reçoit un message,
il peut le transferer au reste du programme, qui va se charger de le traiter.
C'est donc ce thread qui altère les données de jeu. Le thread de l'interface
se contente d'envoyer des requêtes au serveur et d'afficher à l'écran les données
enregistrées (position des joueurs, messages, liste des parties, etc...).
Des buffers sont également prévus pour gérer le cas où un message 
est envoyé au client avant que celui-ci ne s'y attende, ou qu'il
n'ai eu le temps d'enregistrer le "callback". Cette architecture permet une grande flexibilité lors de la programmation, et un traitement 
différent des requêtes selon l'état du programme.
Les données UDP et multicast sont renvoyées à travers la classe `ConnectionHandler`
pour que le programmeur ne fasse pas la différence.
#### Côté serveur

Les fichiers sources sont séparés en 3 répertoires :

- Apps : contient la classe qui accepent la connexion 
- Models : contient le nécessaire à la gestion d'une partie (le plateau, les cases, les joueurs, les fantômes...)
- Utils : contient des classes utilitaires (comme le parser qui reçoit et lit les requêtes, le writer) 
ainsi que des classes pour les requetes et le serveur de streaming audio.