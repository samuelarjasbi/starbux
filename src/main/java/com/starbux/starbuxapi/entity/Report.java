package com.starbux.starbuxapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


@Entity
@Table(name = "REPORT_TBL")
public class Report {
    @Id
    @GeneratedValue
    long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    int orders = 1;


    public long getId() {
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public int getOrders(){
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}
