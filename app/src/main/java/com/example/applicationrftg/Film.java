package com.example.applicationrftg;

public class Film {
    private int filmId;
    private String title;
    private String description;
    private int releaseYear;
    private int originalLanguageId;
    private int rentalDuration;
    private double rentalRate;
    private int length;
    private double replacementCost;
    private String rating;
    private String specialFeatures;
    private String lastUpdate;

    public Film() {
    }

    // Getters
    public int getFilmId() {
        return filmId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public int getLength() {
        return length;
    }

    public String getRating() {
        return rating;
    }

    public int getOriginalLanguageId() {
        return originalLanguageId;
    }

    public int getRentalDuration() {
        return rentalDuration;
    }

    public double getReplacementCost() {
        return replacementCost;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    // Setters
    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setOriginalLanguageId(int originalLanguageId) {
        this.originalLanguageId = originalLanguageId;
    }

    public void setRentalDuration(int rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public void setReplacementCost(double replacementCost) {
        this.replacementCost = replacementCost;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}