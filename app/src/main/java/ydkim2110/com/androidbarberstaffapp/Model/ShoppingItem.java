package ydkim2110.com.androidbarberstaffapp.Model;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ShoppingItem implements Serializable {

    @Exclude private String id;
    private String name, image;
    private Long price;
    private Long quantity;
    public ShoppingItem(){

    }





    public ShoppingItem(String name, Long price ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;


    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
