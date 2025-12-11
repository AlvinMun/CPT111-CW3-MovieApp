package model;

public class Movie {

    // Movie attributes
    private String id;
    private String title;
    private String genre;
    private int year;
    private double rating;

    // Constructor
    public Movie(String id, String title, String genre, int year, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    // returns a readable string representation of the movie
    @Override
    public String toString() {
    return "[" + id + "] " + title + " (" + year + ") - " + genre + " - " + rating;
    }
}


