package data;

import model.Movie;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class MovieDatabase {
    // maps movie ID to Movie object
    private HashMap<String, Movie> moviesById = new HashMap<>();

    public MovieDatabase(String filePath) {
        loadMoviesFromFile(filePath);
    }

    // Load movies from a CSV file
    private void loadMoviesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // split csv line by commas
                String[] parts = line.split(",");

                if (parts.length < 5) {
                    continue;
                }

                // extract movie details
                String id = parts[0].trim();
                String title = parts[1].trim();
                String genre = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                double rating = Double.parseDouble(parts[4].trim());

                // create new Movie object
                Movie movie = new Movie(id, title, genre, year, rating);

                moviesById.put(id, movie);
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to load movies from file: " + filePath);
            e.printStackTrace();
        }
    }

    // returns a collection of all movies
    public Collection<Movie> getAllMovies() {
        return moviesById.values();
    }

    // returns a movie by its ID
    public Movie getMovieById(String id) {
        return moviesById.get(id);
    }
}
