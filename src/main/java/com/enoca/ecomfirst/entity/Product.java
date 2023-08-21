package com.enoca.ecomfirst.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "price")
    private int price;
    @Column(name = "stock")
    private int stock;
    @Column(name = "title")
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "cart_product",
            joinColumns=@JoinColumn(name = "product_id"),
            inverseJoinColumns=@JoinColumn(name = "cart_id")
    )
    private List<Cart>carts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "shop_order_product",
            joinColumns=@JoinColumn(name = "product_id"),
            inverseJoinColumns=@JoinColumn(name = "order_id")
    )
    private List<Order>orders;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(int price, int stock, String title) {
        this.price = price;
        this.stock = stock;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addCart(Cart cart){
        if(carts==null){
            carts=new ArrayList<Cart>();
        }
        carts.add(cart);
    }


    public void addorder(Order order){
        if(orders==null){
            orders=new ArrayList<Order>();
        }
        orders.add(order);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", price=" + price +
                ", stock=" + stock +
                ", title='" + title + '\'' +
                '}';
    }
}
