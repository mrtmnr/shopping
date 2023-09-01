package com.enoca.ecomfirst.entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "cart_limit")
    private int cartLimit;
    @Column(name = "discount")
    private int discount;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "category_promotion",
            joinColumns=@JoinColumn(name = "promotion_id"),
            inverseJoinColumns=@JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Promotion() {
    }

    public Promotion(int cartLimit, int discount) {
        this.cartLimit = cartLimit;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartLimit() {
        return cartLimit;
    }

    public void setCartLimit(int cartLimit) {
        this.cartLimit = cartLimit;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
