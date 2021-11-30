package com.starbux.starbuxapi.entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "add_to_cart")
public class AddToCart {
    @Id
    @GeneratedValue
    long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "drinks_id")
    Drinks drinks;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "toppings_id")
    Toppings toppings;
    //Long product_id;
    int qty;
    double price;
    Long userid;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime added_date;



    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public LocalDateTime getAdded_date() {
        return added_date;
    }
    public void setAdded_date(LocalDateTime added_date) {
        this.added_date = added_date;
    }
    Long getUser_id() {
        return userid;
    }
    public void setUser_id(Long user_id) {
        this.userid = user_id;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Drinks getDrinks() {
        return drinks;
    }
    public void setDrinks(Drinks drinks) {
        this.drinks = drinks;
    }
    public String getDrinksName() {
        return drinks.getName();
    }

    public Toppings getToppings() {
        return toppings;
    }
    public void setToppings(Toppings toppings) {
        this.toppings = toppings;
    }
    public String getToppingsName() {
        return toppings.getName();
    }

}