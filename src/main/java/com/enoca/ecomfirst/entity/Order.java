package com.enoca.ecomfirst.entity;

import jakarta.persistence.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Entity
@Table(name = "shop_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade ={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "shop_order_product",
            joinColumns=@JoinColumn(name = "order;_id"),
            inverseJoinColumns=@JoinColumn(name = "product_id")
    )

    private List<Product>products;

    @OneToOne(cascade =CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", products=" + products +
                ", cart=" + cart +
                '}';
    }
}
