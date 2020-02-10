package ydkim2110.com.androidbarberstaffapp.Model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class UpdateHostel implements Serializable {

    @Exclude
    private String id;
    private String name, username, password, barberId;
    private Long rating;
    private Long ratingTimes ;
    private  int slot;
    private  Long rent;

    public UpdateHostel(String id, String name, String username, String password, String barberId, Long rating, Long ratingTimes, int slot, Long rent) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.barberId = barberId;
        this.rating = rating;
        this.ratingTimes = ratingTimes;
        this.slot = slot;
        this.rent = rent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Long getRent() {
        return rent;
    }

    public void setRent(Long rent) {
        this.rent = rent;
    }
}
