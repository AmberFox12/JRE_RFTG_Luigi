package com.example.applicationrftg;

public class Rental {
    private int rentalId;
    private int inventoryId;
    private int customerId;
    private int statusId;
    private String rentalDate;
    private String returnDate;
    private Film film; // Film associé via inventory

    public Rental() {
    }

    // Getters
    public int getRentalId() {
        return rentalId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public Film getFilm() {
        return film;
    }

    // Setters
    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}