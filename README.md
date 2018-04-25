# [Comme Chez Soi](https://axeleroy.github.io/commechezsoi/)
_Une version web de [#UnToitPourCaramel](https://github.com/axeleroy/untoitpourcaramel) 
–mon précédent logiciel d'aggrégation d'annonces immobilières– utilisant Angular 5 et AWS Lambda._

## Contexte
En 2016 j'ai programmé en Python [#UnToitPourCaramel](https://github.com/axeleroy/untoitpourcaramel)
afin de m'aider dans ma recherche d'appartement et après avoir trouvé grâce à lui 
l'appartement de mes rêves en moins d'une semaine, plusieurs de mes amis furent intéressés pour l'utiliser.

Cependant, l'installation et l'utilisation de celui-ci étaient peu triviales pour ceux qui
avaient peu expérience technique.

Fin 2017, j'ai donc entrepris de réécrire complétement cette application afin de la rendre accessible à tous
à partir d'un simple navigateur web.

## Fonctionnement
Cette application est composée de deux composants :
* Une **web-app Angular** en charge de :
  * Gérer et stocker (dans un `LocalStorage`) les critères de recherches
  * Appeler les Lambdas AWS de récupération des annonces
  * Stocker (dans une `IndexedDB`) et afficher les annonces
* Plusieurs **Lambdas AWS** en charge de :
  * Récupérer les critères qui leurs sont envoyés
  * Effectuer les requêtes vers les APIs des différents sites d'annonce
  * Parser les réponses et les retourner dans des objets `Annonce` 
  
## Déploiement
### AWS Lambda
1. Se déplacer dans `/awslambda`
2. Modifier le champ `ORIGIN` de `src/main/java/sh.leroy.awel.commechezsoi.awslambda\Constants.java`
afin qu'il corresponde à l'URL (protocole et port compris) sur laquelle la web-app sera disponible
3. Compiler les lambdas
   ```bash
   mvn package
   ```
3. Installer `serverless` 
   ```bash
   npm install -g serverless
   ```
4. Déployer
   ```bash
   export AWS_ACCESS_KEY_ID=<your-AMI-key-here>
   export AWS_SECRET_ACCESS_KEY=<your-AMI-secret-key-here>
   serverless deploy --stage [dev|production] --region [aws-region]
   ```
   et noter l'URL à laquelle les lambdas sont déployées.

### Angular
1. Se déplacer dans `/angular`
2. Modifier le champ `aws_lambda_endpoint` dans `src/environments/environments.ts` afin qu'il
corresponde au à l'URL notée plus haut (ex : `https://xxxxxxx.execute-api.eu-west-2.amazonaws.com/production/`)
3. Installer `angular-cli`
   ```bash
   npm install -g @angular/cli
   ````
4. Construire l'application
   ```bash
   ng build --prod
   ```
5. Copier le contenu de `dist` sur votre serveur ou un repo [GitHub Pages](https://pages.github.com/).
 
## To-Do List
* Support de Bien'ici, LogicImmo, PAP et SeLoger
* Critères avancés (type de logement, commodités, etc.)
* Tri des annonces 
  * Par site
  * Par ville
  * Par prix
  * Par surface
  * etc.
* Interface à la Trello
* Récupération des coordonnées GPS et affichage sur une carte

## Crédits
* [ng-bootstrap](https://github.com/ng-bootstrap/ng-bootstrap) : Boostrap pour Angular et quelques
goodies (typeahead + datepicker)
* [angular-persistence](https://github.com/darkarena1/angular-persistence) : service de stockage 
via `LocalStorage`
* [Dexie.js](https://github.com/dfahlander/Dexie.js/) : wrapper pour `IndexedDB`
* [codes-postaux](https://github.com/etalab/codes-postaux) : librairie permettant de faire
correspondre codes postaux et codes INSEE
* [VICOPO](https://vicopo.selfbuild.fr/) : API HTTP utilisée pour l'autocomplétion des villes
* [Font Awesome](https://fontawesome.com/v4.7.0/)
* [Serverless](https://serverless.com/) : framework de déploiement et gestion des Lambdas AWS
