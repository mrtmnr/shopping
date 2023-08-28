package com.enoca.ecomfirst.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private int id;
    @Column(name ="quantity")
    private int quantity;
    @Column(name ="total_price")
    private int totalPrice;
    @Column(name ="discount_price")
    private int discountPrice;
    @Column(name ="total_price_with_discount")
    private int totalPriceWithDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "cart_entry",
            joinColumns=@JoinColumn(name = "entry_id"),
            inverseJoinColumns=@JoinColumn(name = "cart_id")
    )
    private List<Cart> carts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public Entry() {
    }

    public Entry(int quantity, int totalPrice, int discountPrice, int getTotalPriceWithDiscount) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.totalPriceWithDiscount = totalPriceWithDiscount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getTotalPriceWithDiscount() {
        return totalPriceWithDiscount;
    }

    public void setTotalPriceWithDiscount(int totalPriceWithDiscount) {
        this.totalPriceWithDiscount = totalPriceWithDiscount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", discountPrice=" + discountPrice +
                ", totalPriceWithDiscount=" + totalPriceWithDiscount +
                ", product=" + product +
                ", carts=" + carts +
                ", order=" + order +
                '}';
    }
}
