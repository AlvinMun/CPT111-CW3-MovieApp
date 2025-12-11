package service;

import model.Movie;
import data.MovieDatabase;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RecommendationEngine {

    // returns the top N highest-rated movies that the user has not watched yet

    /**
     * Algorithm:
     * 1. Loop N times to select N recommendations.
     * 2. In each iteration, loop through all movies in the database.
     * 3. Skip movies that the user has already watched or that have already been chosen
     */
    public List<Movie> getTopNRecommendations(User user, MovieDatabase movieDb, int n) {
        List<Movie> recommendations = new ArrayList<>();

        if (user == null || n <= 0) {
            return recommendations;
        }

        // track all movies the user has watched
        Set<String> watched = new HashSet<>(user.getHistory());

        for (int i = 0; i < n; i++) {
            Movie best = null;

            // search through all movies 
            for (Movie m : movieDb.getAllMovies()) {
                // skip watched movies
                if (watched.contains(m.getId())) {
                    continue;
                }

                // skip movies already recommended
                boolean alreadyChosen = false;
                for (Movie chosen : recommendations) {
                    if (chosen.getId().equals(m.getId())) {
                        alreadyChosen = true;
                        break;
                    }
                }
                if (alreadyChosen) {
                    continue;
                }

                // select the highest-rated movie
                if (best == null || m.getRating() > best.getRating()) {
                    best = m;
                }
            }

            if (best == null) {
                break;
            }

            recommendations.add(best);
        }

        return recommendations;
    }

    // returns the top N highest-rated movies in the user's favourite genre that the user has not watched yet

    /**
     * Algorithm:
     * 1. Count genres from user's history.
     * 2. Identify the favourite genre.
     * 3. Recommend top-rated movies in that genre.
     * 4. If no history, fall back to rating-based recommendations.
     */
    public List<Movie> getTopNByFavouriteGenre(User user, MovieDatabase movieDb, int n) {
        List<Movie> recommendations = new ArrayList<Movie>();

        if (user == null || n <= 0) {
            return recommendations;
        }

        // count how many movies the user has watched in each genre
        HashMap<String, Integer> genreCounts = new HashMap<String, Integer>();

        for (String id : user.getHistory()) {
            Movie m = movieDb.getMovieById(id);
            if (m != null) {
                String g = m.getGenre();
                Integer old = genreCounts.get(g);
                if (old == null) {
                    genreCounts.put(g, 1);
                } else {
                    genreCounts.put(g, old + 1);
                }
            }
        }

        // if user has no history, fall back to rating-based recommendations
        if (genreCounts.isEmpty()) {
            return getTopNRecommendations(user, movieDb, n);
        }

        // find the genre with the highest count (favorite genre)
        String favouriteGenre = null;
        int maxCount = -1;

        for (String g : genreCounts.keySet()) {
            int c = genreCounts.get(g).intValue();
            if (c > maxCount) {
                maxCount = c;
                favouriteGenre = g;
            }
        }
        
        // track all movies the user has watched
        HashSet<String> watched = new HashSet<String>(user.getHistory());

        for (int i = 0; i < n; i++) {
            Movie best = null;

            // Look through all movies
            for (Movie m : movieDb.getAllMovies()) {
                // skip movies not in favourite genre
                if (!m.getGenre().equals(favouriteGenre)) {
                    continue;
                }

                // skip watched movies
                if (watched.contains(m.getId())) {
                    continue;
                }

                // skip movies already recommended
                boolean alreadyChosen = false;
                for (Movie chosen : recommendations) {
                    if (chosen.getId().equals(m.getId())) {
                        alreadyChosen = true;
                        break;
                    }
                }
                if (alreadyChosen) {
                    continue;
                }

                // pick the highest-rated movie
                if (best == null || m.getRating() > best.getRating()) {
                    best = m;
                }
            }

            if (best == null) {
                break;
            }

            recommendations.add(best);
        }

        return recommendations;
    }
}
