package model;

import java.util.ArrayList;

public class BasicUser extends User {

    public BasicUser(String username, String password,
                     ArrayList<String> watchlist, ArrayList<String> history) {
        super(username, password, watchlist, history, "BASIC");
    }
}
