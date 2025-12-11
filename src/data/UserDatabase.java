package data;

import model.BasicUser;
import model.PremiumUser;
import model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDatabase {
    // map username to User object
    private HashMap<String, User> users = new HashMap<>();
    private String filePath;

    // constructor (loads users from csv file)
    public UserDatabase(String filePath) {
        this.filePath = filePath;
        loadUsersFromFile(filePath);
    }

    // load users from csv file
    private void loadUsersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();

            // process each line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // split line by commas
                String[] parts = line.split(",", -1);

                if (parts.length < 4) continue;

                // extract user data
                String username = parts[0].trim();
                String password = parts[1].trim();
                String watchlistStr = parts[2].trim();
                String historyStr = parts[3].trim();

                // default user type is BASIC
                String type = "BASIC";
                if (parts.length >= 5) {
                    type = parts[4].trim().toUpperCase();
                }

                // convert strings to lists
                ArrayList<String> watchlist = parseList(watchlistStr);
                ArrayList<String> history = parseHistory(historyStr);

                // create user object depending on type
                User user;

                if ("PREMIUM".equals(type)) {
                    user = new PremiumUser(username, password, watchlist, history);
                } else {
                    user = new BasicUser(username, password, watchlist, history);
                }

                users.put(username, user);
            }
        } catch (IOException e) {
            System.out.println("Error loading users.csv " + e.getMessage());
        }
    }

    // convert semicolon-separated string to list
    private ArrayList<String> parseList(String s) {
        ArrayList<String> list = new ArrayList<>();

        if (s == null || s.isEmpty()) return list;

        String[] arr = s.split(";");
        for (String id : arr) {
            id = id.trim();
            if (!id.isEmpty()) {
                list.add(id);
            }
        }
        return list;
    }

    // convert history format string to list
    private ArrayList<String> parseHistory(String s) {
        ArrayList<String> list = new ArrayList<>();

        if (s == null || s.isEmpty()) return list;

        String[] arr = s.split(";");
        for (String entry : arr) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;

            // split by @ to get movie ID
            String[] parts = entry.split("@");
            String movieId = parts[0].trim();

            if (!movieId.isEmpty()) {
                list.add(movieId);
            }
        }
        return list;
    }

    // authenticate user by checking username and password
    public User login(String username, String password) {
        User u = users.get(username);

        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    // saves all users to csv file
    public void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
        bw.write("username,password,watchlist,history,userType");
        bw.newLine();

        for (User u : users.values()) {
            String watchlistStr = u.serializeWatchlist();
            String historyStr = u.serializeHistory();
            String type = u.getUserType();

            String line = u.getUsername() + "," + u.getPassword() + "," +
                        watchlistStr + "," + historyStr + "," + type;

            bw.write(line);
            bw.newLine();
        }

        } catch (IOException e) {
            System.out.println("Error saving users.csv: " + e.getMessage());
        }
    }
    
    // create a new Basic user and add to database
    public User createUser(String username, String password) {
        if (users.containsKey(username)) return null;

        User u = new BasicUser(username, password,
                            new ArrayList<String>(), new ArrayList<String>());

        users.put(username, u);
        saveUsers();
        return u;
    }

    // return all users
    public HashMap<String, User> getUsers() {
        return users;
    }

    // check if username exists
    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }
}
