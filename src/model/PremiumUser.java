package model;

import java.util.ArrayList;

public class PremiumUser extends User {

    // Constructor (parent constructor sets userType to "PREMIUM")
    public PremiumUser(String username, String password,
                       ArrayList<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, "PREMIUM");
    }
}
