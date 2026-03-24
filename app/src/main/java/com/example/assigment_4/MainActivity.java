package com.example.assigment_4;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText favoriteNumberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build the entire UI at runtime (as required)
        initializeUserInterface();
    }

    private void initializeUserInterface() {
        LinearLayout mainContainer = findViewById(R.id.main_container);

        // Label (TextView)
        TextView instructionLabel = new TextView(this);
        instructionLabel.setText("Type your favourite number");
        instructionLabel.setTextSize(18f);
        instructionLabel.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        labelParams.setMargins(0, 0, 0, 24);
        instructionLabel.setLayoutParams(labelParams);
        mainContainer.addView(instructionLabel);

        // Input field (EditText)
        favoriteNumberInput = new EditText(this);
        favoriteNumberInput.setHint("Enter number here");
        favoriteNumberInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        favoriteNumberInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        favoriteNumberInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Listen for "Go" / Enter key
        favoriteNumberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    handleUserInput();
                    return true;
                }
                return false;
            }
        });

        mainContainer.addView(favoriteNumberInput);
    }

    private void handleUserInput() {
        String input = favoriteNumberInput.getText().toString().trim();

        if (input.isEmpty()) {
            showToast("Please enter a number first!");
            return;
        }

        try {
            int userNumber = Integer.parseInt(input);

            // Generate random number between 1 and 10 (each time a new one)
            int randomNumber = (int) (Math.random() * 10) + 1;

            String message;
            if (userNumber == randomNumber) {
                message = "Success! Your favourite number " + userNumber +
                        " matches the random number!";
            } else {
                message = "No match. Your number: " + userNumber +
                        ", Random number: " + randomNumber;
            }

            showToast(message);

            // Clear input so the user can play again immediately (better UX)
            favoriteNumberInput.setText("");

        } catch (NumberFormatException e) {
            showToast("Invalid input! Please enter a valid number.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}