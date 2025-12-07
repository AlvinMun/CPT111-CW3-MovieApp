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

    public List<Movie> getTopNRecommendations(User user, MovieDatabase movieDb, int n) {
        List<Movie> recommendations = new ArrayList<>();

        if (user == null || n <= 0) {
            return recommendations;
        }

        Set<String> watched = new HashSet<>(user.getHistory());

        for (int i = 0; i < n; i++) {
            Movie best = null;

            for (Movie m : movieDb.getAllMovies()) {
                if (watched.contains(m.getId())) {
                    continue;
                }

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

    public List<Movie> getTopNByFavouriteGenre(User user, MovieDatabase movieDb, int n) {
        List<Movie> recommendations = new ArrayList<Movie>();

        if (user == null || n <= 0) {
            return recommendations;
        }

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

        if (genreCounts.isEmpty()) {
            return getTopNRecommendations(user, movieDb, n);
        }

        String favouriteGenre = null;
        int maxCount = -1;

        for (String g : genreCounts.keySet()) {
            int c = genreCounts.get(g).intValue();
            if (c > maxCount) {
                maxCount = c;
                favouriteGenre = g;
            }
        }
        
        HashSet<String> watched = new HashSet<String>(user.getHistory());

        for (int i = 0; i < n; i++) {
            Movie best = null;

            for (Movie m : movieDb.getAllMovies()) {
                if (!m.getGenre().equals(favouriteGenre)) {
                    continue;
                }

                if (watched.contains(m.getId())) {
                    continue;
                }

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
