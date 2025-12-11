package model;

import java.util.ArrayList;

public class User {
    
    // User attributes
    private String username;
    private String password;
    private ArrayList<String> watchlist;
    private ArrayList<String> history;
    private String userType;

    // Constructors
    public User(String username, String password, ArrayList<String> watchlist, ArrayList<String> history) {
        this(username, password, watchlist, history, "BASIC");  // default type
    }

    // Full constructor
    public User(String username, String password, ArrayList<String> watchlist,
            ArrayList<String> history, String userType) {
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
        this.history = history;
        this.userType = userType;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getWatchList() {
        return watchlist;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public String getUserType() {
        return userType;
    }

    // updates the user type
    public void setUserType(String userType) {
        this.userType = userType;
    }

    // returns max recommendations based on user type
    public int getMaxRecommendations() {
        if ("PREMIUM".equalsIgnoreCase(userType)) {
            return 100;   // premium limit
        } else {
            return 20;    // basic limit
        }
    }

    // adds a movie to the watchlist
    public void addToWatchlist(String movieId) {
        if (!watchlist.contains(movieId)) {
            watchlist.add(movieId);
        }
    }

    // removes a movie from the watchlist
    public void removeFromWatchlist(String movieId) {
        watchlist.remove(movieId);
    }

    // adds a movie to the watched history
    public void addToHistory(String movieId) {
        if (!history.contains(movieId)) {
            history.add(movieId);
        }
    }

    // converts watchlist and history to semicolon-separated strings
    public String serializeWatchlist() {
        return String.join(";", watchlist);
    }

    public String serializeHistory() {
        return String.join(";", history);
    }

    // updates the user's password
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}

