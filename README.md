# ✈️ DestiMatch API

**DestiMatch** est un moteur de recommandation de voyage intelligent. Ce backend permet de connecter des voyageurs à leurs destinations idéales grâce à un algorithme de matching basé sur le budget, le style de voyage, la saisonnalité et les préférences thématiques.

> *Ne cherchez plus où partir, DestiMatch le trouve pour vous.*

---

## 🚀 Fonctionnalités Clés

### 🧠 Core Engine (Le Cerveau)
* **Algorithme de Matching** : Calcul de score de pertinence (0-100) en temps réel croisant 5 critères (Tags, Budget, Style, Saison, Avis).
* **Auto-Catégorisation** : Calcul automatique du niveau de budget (`ECO`, `MODERATE`, `HIGH`, `LUXURY`) basé sur le coût journalier.

### 🌍 Gestion des Destinations
* **Catalogue complet** : CRUD complet pour les administrateurs.
* **Recherche Avancée** : Filtrage par continent, fourchette de prix et style de voyage.
* **Système d'Avis (Reviews)** : Les utilisateurs peuvent noter et commenter. La note globale et le compteur d'avis sont recalculés mathématiquement à chaque ajout ou suppression.

### 👤 Gestion Utilisateurs
* **Profilage Progressif** : L'utilisateur affine ses préférences (continents favoris, style solo/famille...) au fil de l'eau.
* **Favoris** : Gestion de liste de souhaits (Wishlist).
* **Sécurité** : Authentification JWT (JSON Web Token) et hachage des mots de passe (Bcrypt).
* **Rôles** : Distinction stricte entre `User` et `Admin`.

---

## 🛠️ Stack Technique

Ce projet est construit sur une architecture moderne et performante :

* **Langage** : Java 17+
* **Framework** : [Quarkus](https://quarkus.io/) (Supersonic Subatomic Java)
* **Base de Données** : MongoDB (via Panache ORM)
* **Sécurité** : SmallRye JWT & Elytron
* **Outils** : Maven, Postman (pour les tests API), Docker (optionnel pour la DB)

---

## 📚 Documentation API (Swagger UI)

Ce projet intègre **OpenAPI (Swagger)** pour une documentation vivante et interactive.
Plutôt que de lire des spécifications statiques, vous pouvez tester les endpoints directement via le navigateur.

### Accéder à la documentation
Une fois l'application lancée, rendez-vous sur :

👉 **[http://localhost:3000/q/swagger-ui/](http://localhost:3000/q/swagger-ui/)**

### Aperçu des quelques endpoints

| Module | Méthode | Route | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/users/auth/login` | Récupération du Token JWT |
| **Matching** | `POST` | `/api/destinations/match` | Algorithme de recommandation |
| **Reviews** | `POST` | `/api/destinations/{id}/reviews` | Ajout d'un avis client |
| **Admin** | `DELETE` | `/api/reviews/{id}` | Modération des commentaires |

> **Note :** Pour tester les routes sécurisées dans Swagger, cliquez sur le bouton "Authorize" et collez votre token (format : `Bearer eyJhbGciOi...`).

---

## ⚙️ Installation et Lancement

### Prérequis
* JDK 17 ou supérieur
* Maven (ou utiliser le wrapper `./mvnw` fourni)
* Une instance MongoDB (locale ou Atlas)

### 1. Cloner le projet
```bash
git clone [https://github.com/votre-pseudo/destimatch-api.git](https://github.com/votre-pseudo/destimatch-api.git)
cd destimatch-api