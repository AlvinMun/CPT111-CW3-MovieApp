# 🎬 Movie Recommendation & Tracker (CPT111 CourseWork 3)

## 📄 Overview
This project is a **Java-based Movie Recommendation and Tracking System** developed as part of **CPT111 – Programming with Java** coursework.

Users can:
- Login with existing accounts
- Browse all movies
- Add movies to a personal watchlist
- Mark movies as watched (History)
- Get top-rated movie recommendations
- Change their account password
- View data dynamically in a **JavaFX GUI**

The system reuses the core logic implemented earlier in a console version and extends it to a fully functional **JavaFX GUI**.

---

## 🧱 Architecture & Design
The application is structured using a **Model–View–Controller inspired** design:

CPT111-CW3-MovieApp/
│
├─ src/
│ ├─ data/ # Database loading and storage
│ │ ├─ MovieDatabase.java
│ │ └─ UserDatabase.java
│ │
│ ├─ model/ # Data models
│ │ ├─ Movie.java
│ │ └─ User.java
│ │
│ ├─ service/ # Business logic
│ │ └─ RecommendationEngine.java
│ │
│ └─ ui/ # JavaFX GUI interface
│ └─ MovieAppFX.java
│
├─ data/ # Data files for loading initial movie/user info
│ ├─ movies.csv
│ └─ users.csv
│
└─ movie-web/ (Optional prototype)
├─ index.html
├─ app.js
├─ style.css
└─ data/movies.json
