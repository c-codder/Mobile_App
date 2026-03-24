package com.example.assigment_4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProductManager productManager;
    private EditText etId, etName, etPrice, etAmount;
    private TextView tvSummary;

    private static final String KEY_PRODUCT_LIST = "product_list";   // ← NEW

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productManager = new ProductManager();

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etAmount = findViewById(R.id.etAmount);
        tvSummary = findViewById(R.id.tvSummary);

        findViewById(R.id.btnSubmit).setOnClickListener(v -> handleSubmitting());
        findViewById(R.id.btnClear).setOnClickListener(v -> clearInputs());

        // NEW: Restore data after rotation
        if (savedInstanceState != null) {
            ArrayList<Product> savedList = (ArrayList<Product>)
                    savedInstanceState.getSerializable(KEY_PRODUCT_LIST);
            if (savedList != null) {
                productManager.setProductList(savedList);
                updateSummary();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_PRODUCT_LIST, productManager.getProductList());
    }

    private void handleSubmitting() {
        try {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            double price = Double.parseDouble(etPrice.getText().toString().trim());
            int amount = Integer.parseInt(etAmount.getText().toString().trim());

            if (id.isEmpty() || name.isEmpty()) {
                throw new Exception("Empty fields");
            }

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
class Product implements Serializable {
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


    public ArrayList<Product> getProductList() {
        return new ArrayList<>(productList);   // returns a copy
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