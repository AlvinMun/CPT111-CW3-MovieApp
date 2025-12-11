package ui;

import data.MovieDatabase;
import data.UserDatabase;
import model.Movie;
import model.User;
import service.RecommendationEngine;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.List;
import java.util.Optional;

public class MovieFX extends Application {

    private MovieDatabase movieDb;
    private UserDatabase userDb;
    private RecommendationEngine recEngine;

    private Stage primaryStage;
    private Scene loginScene, mainScene, registerScene;

    private User currentUser;

    private Label welcomeLabel;
    private ListView<String> contentList;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // load csv data
        movieDb = new MovieDatabase("data/movies.csv");
        userDb = new UserDatabase("data/users.csv");
        recEngine = new RecommendationEngine();

        buildLoginScene();
        buildRegisterScene();
        buildMainScene();

        primaryStage.setTitle("Movie Recommendation & Tracker");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    //LOGIN SCREEN

    /** 
     * Builds the login scene
     * allows username and password login
    */
    private void buildLoginScene() {
        Label title = new Label("Movie Tracker");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String u = usernameField.getText().trim();
                String p = passwordField.getText().trim();
                User user = userDb.login(u, p);
                if (user != null) {
                    // Successful login
                    currentUser = user;
                    welcomeLabel.setText("Welcome, " + currentUser.getUsername());
                    messageLabel.setText("");
                    usernameField.clear();
                    passwordField.clear();
                    contentList.getItems().clear();
                    statusLabel.setText("");
                    primaryStage.setScene(mainScene);
                } else {
                    // invalid credentials
                    messageLabel.setText("Invalid username or password.");
                }
            }
        });

        // registration button action
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(registerScene);
            }
        });

        HBox buttons = new HBox(10, loginButton, registerButton);

        VBox root = new VBox(10, title, usernameField, passwordField, buttons, messageLabel);
        root.setPadding(new Insets(20));
        loginScene = new Scene(root, 400, 300);
    }

    // builds the registration screen
    private void buildRegisterScene() {
        Label title = new Label("Create New Account");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Choose a password");

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm password");

        Button createButton = new Button("Create Account");
        Button backButton = new Button("Back to Login");

        Label messageLabel = new Label();

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String confirm = confirmField.getText().trim();

                if (username.length() == 0) {
                    messageLabel.setText("Username cannot be empty.");
                    return;
                }

                if (userDb.usernameExists(username)) {
                    messageLabel.setText("Username already exists. Please choose another.");
                    return;
                }

                if (password.length() == 0) {
                    messageLabel.setText("Password cannot be empty.");
                    return;
                }

                if (!password.equals(confirm)) {
                    messageLabel.setText("Passwords do not match.");
                    return;
                }

                //create new user
                User newUser = userDb.createUser(username, password);
                if (newUser != null) {
                    messageLabel.setText("Account created! You can now log in.");
                    usernameField.clear();
                    passwordField.clear();
                    confirmField.clear();
                } else {
                    messageLabel.setText("Failed to create account. Please try again.");
                }
            }
        });

        // return to login screen
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                usernameField.clear();
                passwordField.clear();
                confirmField.clear();
                messageLabel.setText("");
                primaryStage.setScene(loginScene);
            }
        });

        HBox buttons = new HBox(10, createButton, backButton);
        VBox root = new VBox(10, title, usernameField, passwordField, confirmField, buttons, messageLabel);
        root.setPadding(new Insets(20));

        registerScene = new Scene(root, 450, 320);
    }

    /**
     * builds main application screen
     * contains movie browsing, watchlist management, history, recommendations and account settings.
     */
    private void buildMainScene() {
        welcomeLabel = new Label("Welcome");
        statusLabel = new Label();

        // Buttons
        Button browseBtn = new Button("Browse Movies");
        Button watchlistBtn = new Button("View Watchlist");
        Button historyBtn = new Button("View History");
        Button recBtn = new Button("Get Recommendations");
        Button changePassBtn = new Button("Change Password");
        Button logoutBtn = new Button("Logout");
        Button basicBtn = new Button("Basic");
        Button premiumBtn = new Button("Premium");

        //styling for tier buttons
        basicBtn.getStyleClass().add("tier-button");
        premiumBtn.getStyleClass().add("tier-button");

        
        browseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showAllMovies();
            }
        });

        watchlistBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showWatchlist();
            }
        });

        historyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showHistory();
            }
        });

        recBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showRecommendations();
            }
        });

        changePassBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                changePasswordDialog();
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                currentUser = null;
                contentList.getItems().clear();
                statusLabel.setText("");
                primaryStage.setScene(loginScene);
            }
        });

        basicBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentUser == null) {
                    statusLabel.setText("Please log in first.");
                    return;
                }
                currentUser.setUserType("BASIC");
                userDb.saveUsers();

                if (!basicBtn.getStyleClass().contains("selected-tier")) {
                    basicBtn.getStyleClass().add("selected-tier");
                }
                premiumBtn.getStyleClass().remove("selected-tier");

                statusLabel.setText("Switched to BASIC user (max "
                        + currentUser.getMaxRecommendations() + " recommendations).");
            }
        });

        premiumBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentUser == null) {
                    statusLabel.setText("Please log in first.");
                    return;
                }
                currentUser.setUserType("PREMIUM");
                userDb.saveUsers();

                if (!premiumBtn.getStyleClass().contains("selected-tier")) {
                    premiumBtn.getStyleClass().add("selected-tier");
                }
                basicBtn.getStyleClass().remove("selected-tier");

                statusLabel.setText("Switched to PREMIUM user (max "
                        + currentUser.getMaxRecommendations() + " recommendations).");
            }
        });

        // Layout (top rows)
        HBox topRow = new HBox(10, browseBtn, watchlistBtn, historyBtn, recBtn, changePassBtn, logoutBtn);
        HBox secondRow = new HBox(10, basicBtn, premiumBtn);
        
        Label idLabel = new Label("Movie ID:");
        TextField movieIdField = new TextField();
        movieIdField.setPromptText("e.g. M001");

        Button addWatchBtn = new Button("Add to Watchlist");
        Button removeWatchBtn = new Button("Remove from Watchlist");
        Button markWatchedBtn = new Button("Mark as Watched");

        // Add to Watchlist
        addWatchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentUser == null) {
                    statusLabel.setText("Please log in first.");
                    return;
                }

                String id = movieIdField.getText().trim();
                if (id.length() == 0) {
                    statusLabel.setText("Please enter a movie ID.");
                    return;
                }

                Movie movie = movieDb.getMovieById(id);
                if (movie == null) {
                    statusLabel.setText("No movie found with ID " + id);
                    return;
                }

                currentUser.addToWatchlist(id);
                userDb.saveUsers();
                statusLabel.setText("Added to watchlist: " + movie.getTitle());
            }
        });

        // Remove from Watchlist
        removeWatchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentUser == null) {
                    statusLabel.setText("Please log in first.");
                    return;
                }

                String id = movieIdField.getText().trim();
                if (id.length() == 0) {
                    statusLabel.setText("Please enter a movie ID.");
                    return;
                }

                if (!currentUser.getWatchList().contains(id)) {
                    statusLabel.setText("That movie is not in your watchlist.");
                    return;
                }

                currentUser.removeFromWatchlist(id);
                userDb.saveUsers();
                statusLabel.setText("Removed from watchlist: " + id);
            }
        });

        // Mark as Watched
        markWatchedBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentUser == null) {
                    statusLabel.setText("Please log in first.");
                    return;
                }

                String id = movieIdField.getText().trim();
                if (id.length() == 0) {
                    statusLabel.setText("Please enter a movie ID.");
                    return;
                }

                Movie movie = movieDb.getMovieById(id);
                if (movie == null) {
                    statusLabel.setText("No movie found with ID " + id);
                    return;
                }

                currentUser.addToHistory(id);
                currentUser.removeFromWatchlist(id);
                userDb.saveUsers();
                statusLabel.setText("Marked as watched: " + movie.getTitle());
            }
        });

        HBox editBox = new HBox(10, idLabel, movieIdField, addWatchBtn, removeWatchBtn, markWatchedBtn);

        contentList = new ListView<String>();

        VBox root = new VBox(10, welcomeLabel, topRow, secondRow, editBox, contentList, statusLabel);
        root.setPadding(new Insets(20));
        mainScene = new Scene(root, 900, 600);

        // Load CSS
        String css = getClass().getResource("style.css").toExternalForm();
        mainScene.getStylesheets().add(css);

    }

    // DISPLAY FUNCTIONS
    private void showAllMovies() {
        contentList.getItems().clear();
        for (Movie m : movieDb.getAllMovies()) {
            contentList.getItems().add(formatMovie(m));
        }
        statusLabel.setText("All movies.");
    }

    private void showWatchlist() {
        contentList.getItems().clear();
        if (currentUser.getWatchList().isEmpty()) {
            statusLabel.setText("Watchlist is empty.");
            return;
        }
        for (String id : currentUser.getWatchList()) {
            Movie m = movieDb.getMovieById(id);
            if (m != null) {
                contentList.getItems().add(formatMovie(m));
            }
        }
        statusLabel.setText("Your Watchlist");
    }

    private void showHistory() {
        contentList.getItems().clear();
        if (currentUser.getHistory().isEmpty()) {
            statusLabel.setText("History is empty.");
            return;
        }
        for (String id : currentUser.getHistory()) {
            Movie m = movieDb.getMovieById(id);
            if (m != null) {
                contentList.getItems().add(formatMovie(m));
            }
        }
        statusLabel.setText("Your viewing History");
    }

    // RECOMMENDATIONS

    /**
     * Prompts user for recommendation strategy and number of recommendations,
     * rating based or favourite genre based.
     */
    private void showRecommendations() {
        if (currentUser == null) {
            statusLabel.setText("Please log in first.");
            return;
        }
        TextInputDialog strategyDialog = new TextInputDialog("1");
        strategyDialog.setHeaderText("Choose recommendation strategy");
        strategyDialog.setTitle("Recommendations");
        strategyDialog.setContentText(
                "1 = By rating\n" +
                "2 = By favourite genre\n" +
                "Your choice: ");

        Optional<String> strategyResult = strategyDialog.showAndWait();
        if (!strategyResult.isPresent()) {
            return;
        }

        String strategyInput = strategyResult.get().trim();
        int strategy;
        try {
            strategy = Integer.parseInt(strategyInput);
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid strategy. Please enter 1 or 2.");
            return;
        }

        if (strategy != 1 && strategy != 2) {
            statusLabel.setText("Invalid strategy. Please enter 1 or 2.");
            return;
        }

        // number of recommendations (N)
        TextInputDialog nDialog = new TextInputDialog("5");
        nDialog.setHeaderText("How many recommendations?");
        nDialog.setTitle("Recommendations");
        nDialog.setContentText("N:");

        Optional<String> nResult = nDialog.showAndWait();
        if (!nResult.isPresent()) {
            return;
        }

        String nInput = nResult.get().trim();
        int n;
        try {
            n = Integer.parseInt(nInput);
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid number for N.");
            return;
        }

        if (n <= 0) {
            statusLabel.setText("N must be positive.");
            return;
        }

        // shows the tier limit if exceeded
        int max = currentUser.getMaxRecommendations();
        if (n > max) {
            n = max;
            statusLabel.setText("As a " + currentUser.getUserType()
                    + " user, you can request at most " + max + " recommendations. Showing " + max + ".");
        }

        // compute recommendations
        List<Movie> recs;
        if (strategy == 2) {
            recs = recEngine.getTopNByFavouriteGenre(currentUser, movieDb, n);
        } else {
            recs = recEngine.getTopNRecommendations(currentUser, movieDb, n);
        }

        contentList.getItems().clear();

        if (recs == null || recs.isEmpty()) {
            statusLabel.setText("No recommendations available. Try watching more movies first!");
            return;
        }

        for (Movie m : recs) {
            contentList.getItems().add(formatMovie(m));
        }

        if (strategy == 2) {
            statusLabel.setText("Top " + n + " recommendations (favourite genre)");
        } else {
            statusLabel.setText("Top " + n + " recommendations (by rating)");
        }
    }


    // CHANGE PASSWORD
    private void changePasswordDialog() {
        if (currentUser == null) {
            statusLabel.setText("Please log in first.");
            return;
        }

        // current password validation
        TextInputDialog currentDialog = new TextInputDialog();
        currentDialog.setTitle("Change Password");
        currentDialog.setHeaderText("Change Password");
        currentDialog.setContentText("Enter current password:");

        Optional<String> currentResult = currentDialog.showAndWait();
        if (!currentResult.isPresent()) {
            return; 
        }

        String current = currentResult.get().trim();
        if (!currentUser.getPassword().equals(current)) {
            statusLabel.setText("Current password is incorrect.");
            return;
        }

        // new password input
        TextInputDialog newDialog = new TextInputDialog();
        newDialog.setTitle("Change Password");
        newDialog.setHeaderText("Change Password");
        newDialog.setContentText("Enter new password:");

        Optional<String> newResult = newDialog.showAndWait();
        if (!newResult.isPresent()) {
            return;
        }

        String newPass = newResult.get().trim();
        if (newPass.length() == 0) {
            statusLabel.setText("New password cannot be empty.");
            return;
        }

        // confirm new password
        TextInputDialog confirmDialog = new TextInputDialog();
        confirmDialog.setTitle("Change Password");
        confirmDialog.setHeaderText("Change Password");
        confirmDialog.setContentText("Confirm new password:");

        Optional<String> confirmResult = confirmDialog.showAndWait();
        if (!confirmResult.isPresent()) {
            return;
        }

        String confirm = confirmResult.get().trim();
        if (!newPass.equals(confirm)) {
            statusLabel.setText("New passwords do not match.");
            return;
        }

        currentUser.setPassword(newPass);
        userDb.saveUsers();
        statusLabel.setText("Password changed successfully.");
    }

    // format movie into readable text
    private String formatMovie(Movie m) {
        return String.format("[%s] %s (%d) - %s - %.1f",
                m.getId(), m.getTitle(), m.getYear(), m.getGenre(), m.getRating());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
