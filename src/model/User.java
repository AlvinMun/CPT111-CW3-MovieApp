package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<String> watchlist;
    private ArrayList<String> history;
    private String userType;

    public User(String username, String password, ArrayList<String> watchlist, ArrayList<String> history) {
        this(username, password, watchlist, history, "BASIC");  // default type
    }

    public User(String username, String password, ArrayList<String> watchlist,
            ArrayList<String> history, String userType) {
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
        this.history = history;
        this.userType = userType;
    }


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

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getMaxRecommendations() {
        if ("PREMIUM".equalsIgnoreCase(userType)) {
            return 100;   // premium limit
        } else {
            return 20;    // basic limit
        }
    }

    public void addToWatchlist(String movieId) {
        if (!watchlist.contains(movieId)) {
            watchlist.add(movieId);
        }
    }

    public void removeFromWatchlist(String movieId) {
        watchlist.remove(movieId);
    }

    public void addToHistory(String movieId) {
        if (!history.contains(movieId)) {
            history.add(movieId);
        }
    }

    public String serializeWatchlist() {
        return String.join(";", watchlist);
    }

    public String serializeHistory() {
        return String.join(";", history);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}

