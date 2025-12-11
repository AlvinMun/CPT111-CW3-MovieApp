package model;

import java.util.ArrayList;

public class BasicUser extends User {

    // Constructor (parent constructor sets userType to "BASIC")
    public BasicUser(String username, String password,
                     ArrayList<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, "BASIC");
    }
}
