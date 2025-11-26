package service;

import model.Movie;
import data.MovieDatabase;
import model.User;

import java.util.ArrayList;
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
}
