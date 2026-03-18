package com.example.assigment_4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProductManager productManager;
    private EditText etId, etName, etPrice, etAmount;
    private TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the UI from activity_main.xml
        setContentView(R.layout.activity_main);

        productManager = new ProductManager();

        // Connect the Java variables to the XML views
        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etAmount = findViewById(R.id.etAmount);
        tvSummary = findViewById(R.id.tvSummary);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnClear = findViewById(R.id.btnClear);

        // Set button click listeners
        btnSubmit.setOnClickListener(v -> handleSubmitting());
        btnClear.setOnClickListener(v -> clearInputs());
    }

    private void handleSubmitting() {
        try {
            String id = etId.getText().toString();
            String name = etName.getText().toString();
            double price = Double.parseDouble(etPrice.getText().toString());
            int amount = Integer.parseInt(etAmount.getText().toString());

            if (name.isEmpty() || id.isEmpty()) throw new Exception();

            Product p = new Product(id, name, price, amount);
            productManager.addProduct(p);

            updateSummary();
            clearInputs();
            Toast.makeText(this, "Product Added!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Please enter all valid data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSummary() {
        tvSummary.setText(productManager.getAllProductsSummary());
    }

    private void clearInputs() {
        etId.setText("");
        etName.setText("");
        etPrice.setText("");
        etAmount.setText("");
    }
}

// Data Model
class Product {
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

// Business Logic Manager
class ProductManager {
    private ArrayList<Product> productList = new ArrayList<>();

    public void addProduct(Product p) {
        productList.add(p);
    }

    public String getAllProductsSummary() {
        StringBuilder sb = new StringBuilder();
        double grandTotal = 0;

        for (Product p : productList) {
            sb.append("ID: ").append(p.id)
                    .append(" | ").append(p.name)
                    .append("\nPrice: ").append(p.price)
                    .append(" x ").append(p.amount)
                    .append(" = ").append(p.getTotalValue()).append("\n\n");
            grandTotal += p.getTotalValue();
        }
        sb.append("--------------------\n");
        sb.append("Total Inventory Value: ").append(grandTotal);

        return sb.toString();
    }
}