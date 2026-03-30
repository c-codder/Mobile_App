package com.example.ass_42;

import java.util.ArrayList;

public class ProductManager {
    private ArrayList<Product> productList = new ArrayList<>();

    public void addProduct(Product p) {
        productList.add(p);
    }

    public ArrayList<Product> getProductList() {
        return new ArrayList<>(productList);
    }

    public void setProductList(ArrayList<Product> list) {
        productList.clear();
        productList.addAll(list);
    }

    public String getAllProductsSummary() {
        StringBuilder sb = new StringBuilder();
        double grandTotal = 0;

        for (Product p : productList) {
            double total = p.getTotalValue();
            sb.append("ID: ").append(p.id)
                    .append(" | ").append(p.name)
                    .append("\nPrice: ").append(p.price)
                    .append(" x ").append(p.amount)
                    .append(" = ").append(total).append("\n\n");
            grandTotal += total;
        }
        sb.append("--------------------\n")
                .append("Total Inventory Value: ").append(grandTotal);

        return sb.toString();
    }
}