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

    @OneToMany(mappedBy ="product" ,fetch = FetchType.LAZY)
    private List<Entry> entries;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "product_category",
            joinColumns=@JoinColumn(name = "product_id"),
            inverseJoinColumns=@JoinColumn(name = "category_id")
    )
    private List<Category> categories;

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addCategory(Category category){
        if(categories==null){
            categories=new ArrayList<Category>();
        }
        categories.add(category);
    }

    public void addEntry(Entry entry){
        if(entries==null){
            entries=new ArrayList<Entry>();
        }
        entries.add(entry);
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
