package com.example.ass5;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Input fields (Submit area)
    private EditText etFirstName, etLastName, etPhone;
    private Button btnSubmit;

    // Search fields (AutoCompleteTextView)
    private AutoCompleteTextView actvSearchFirst, actvSearchLast, actvSearchPhone;

    // Result display
    private TextView tvResult;

    // Three separate lists - each used by one AutoCompleteTextView
    private final ArrayList<String> firstBasedList = new ArrayList<>();
    private final ArrayList<String> lastBasedList = new ArrayList<>();
    private final ArrayList<String> phoneBasedList = new ArrayList<>();

    // Adapters (recreated after every submit)
    private ArrayAdapter<String> adapterFirst;
    private ArrayAdapter<String> adapterLast;
    private ArrayAdapter<String> adapterPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupAdapters();
        setupListeners();
    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etPhone = findViewById(R.id.et_phone);
        btnSubmit = findViewById(R.id.btn_submit);

        actvSearchFirst = findViewById(R.id.actv_search_first);
        actvSearchLast = findViewById(R.id.actv_search_last);
        actvSearchPhone = findViewById(R.id.actv_search_phone);

        tvResult = findViewById(R.id.tv_result);
    }

    private void setupAdapters() {
        // Create adapters exactly like in lecture Example 5-2
        adapterFirst = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, firstBasedList);
        adapterLast = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, lastBasedList);
        adapterPhone = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, phoneBasedList);

        actvSearchFirst.setAdapter(adapterFirst);
        actvSearchLast.setAdapter(adapterLast);
        actvSearchPhone.setAdapter(adapterPhone);

        // Minimum 1 character to start showing suggestions (as in lecture)
        actvSearchFirst.setThreshold(1);
        actvSearchLast.setThreshold(1);
        actvSearchPhone.setThreshold(1);
    }

    private void setupListeners() {
        // Submit button
        btnSubmit.setOnClickListener(v -> submitData());

        // Click on any search result
        actvSearchFirst.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            displayResult(selected, "first");
        });

        actvSearchLast.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            displayResult(selected, "last");
        });

        actvSearchPhone.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            displayResult(selected, "phone");
        });
    }

    private void submitData() {
        String first = etFirstName.getText().toString().trim();
        String last = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (first.isEmpty() || last.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, R.string.msg_fill_all, Toast.LENGTH_SHORT).show();
            return;
        }

        // Create 3 combinations (NO spaces - exactly as the hint says)
        String comboFirst = first + "|" + last + "|" + phone;   // for first-name search
        String comboLast = last + "|" + first + "|" + phone;    // for last-name search
        String comboPhone = phone + "|" + first + "|" + last;   // for phone search

        // Add to the three lists
        firstBasedList.add(comboFirst);
        lastBasedList.add(comboLast);
        phoneBasedList.add(comboPhone);

        // IMPORTANT: Recreate adapters so new records appear immediately
        // (This is the fix for the "only first record works" bug)
        setupAdapters();

        // Clear all fields
        etFirstName.setText("");
        etLastName.setText("");
        etPhone.setText("");
        actvSearchFirst.setText("");
        actvSearchLast.setText("");
        actvSearchPhone.setText("");
        tvResult.setText("");

        Toast.makeText(this, R.string.msg_success, Toast.LENGTH_SHORT).show();
    }

    private void displayResult(String selected, String type) {
        String[] parts = selected.split("\\|");

        String first = "", last = "", phone = "";

        // Parse according to which search field was used
        if (type.equals("first")) {
            first = parts[0];
            last = parts[1];
            phone = parts[2];
        } else if (type.equals("last")) {
            last = parts[0];
            first = parts[1];
            phone = parts[2];
        } else if (type.equals("phone")) {
            phone = parts[0];
            first = parts[1];
            last = parts[2];
        }

        // Exact format required by the assignment
        String result = getString(R.string.label_first) + first +
                getString(R.string.label_last) + last +
                getString(R.string.label_phone) + phone;

        tvResult.setText(result);

        // Clear search fields after selection
        actvSearchFirst.setText("");
        actvSearchLast.setText("");
        actvSearchPhone.setText("");
    }
}