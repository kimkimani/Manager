package ydkim2110.com.androidbarberstaffapp.Model;

public class HostelAdd1 {



    private String name, username, password ,contact;
    private Long slot;
    private int rent;
    private Long rating;
    private Long ratingTimes ;

    public HostelAdd1(){

    }

    public HostelAdd1(String name, String username, String password, Long slot, int rent ,String contact) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.slot = slot;
        this.rent = rent;
        this.contact = contact;

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

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}


