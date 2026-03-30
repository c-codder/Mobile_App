package com.example.ass_42;

import java.io.Serializable;

public class Product implements Serializable {
    String id, name;
    double price;
    int amount;

    public Product(String id, String name, double price, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public double getTotalValue() {
        return price * amount;
    }
}