package service;

import data.MovieDatabase;
import model.Movie;
import model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationEngine {

    public List<Movie> getTopNRecommendations(User user, MovieDatabase movieDb, int n) {
        List<Movie> recommendations = new ArrayList<>();

        if (n <= 0) {
            return recommendations;
        }

        Set<String> watchedIds = new HashSet<>(user.getHistory());

        List<Movie> candidates = new ArrayList<>();
        for (Movie m : movieDb.getAllMovies()) {
            if (!watchedIds.contains(m.getId())) {
                candidates.add(m);
            }
        }

        candidates.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));

        for (int i = 0; i < candidates.size() && i < n; i++) {
            recommendations.add(candidates.get(i));
        }

        return recommendations;
    }
}
