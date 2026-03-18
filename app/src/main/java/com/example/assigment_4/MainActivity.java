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

    // Rule 8: Variables are private
    private NumberGameManager gameManager;
    private EditText favoriteNumberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the base layout from XML
        setContentView(R.layout.activity_main);

        // Rule 7: Initialize the manager class which takes care of operations
        gameManager = new NumberGameManager();

        // Create the user interface during runtime
        initializeUserInterface();
    }


    private void initializeUserInterface() {
        LinearLayout mainContainer = findViewById(R.id.main_container);

        // Rule 1: Descriptive variable name for the label
        TextView instructionLabel = new TextView(this);
        instructionLabel.setText("Type your favourite number");
        instructionLabel.setTextSize(18);
        mainContainer.addView(instructionLabel);

        // Rule 1: Descriptive variable name for the input field
        favoriteNumberInput = new EditText(this);
        favoriteNumberInput.setHint("Enter number here");
        // Ensure only numbers can be entered
        favoriteNumberInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        // Set keyboard action to "Go"
        favoriteNumberInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        
        // Listen for the "Go" key (Enter)
        favoriteNumberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Check if Go key or Enter key was pressed
                if (actionId == EditorInfo.IME_ACTION_GO || 
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    
                    // Interaction logic starts here
                    handleUserInput();
                    return true;
                }
                return false;
            }
        });

        mainContainer.addView(favoriteNumberInput);
    }


    private void handleUserInput() {
        String inputString = favoriteNumberInput.getText().toString();
        
        if (inputString.isEmpty()) {
            showToastMessage("Please enter a number first!");
            return;
        }

        try {
            int userValue = Integer.parseInt(inputString);
            
            // Rule 7: Use a concept class to hold the data
            FavoriteNumber userFavorite = new FavoriteNumber(userValue);
            
            // Rule 6: Business logic is handled by the manager class, not in the interaction method
            int generatedRandom = gameManager.generateNewRandomNumber(1, 10);
            boolean isCorrectGuess = gameManager.checkIfMatch(userFavorite.getStoredValue());

            // Rule 4: Get a string result from a helper method instead of printing directly
            String resultText = createResultMessage(isCorrectGuess, userFavorite.getStoredValue(), generatedRandom);
            
            // Rule 3: Separate printing/display logic from calculation logic
            showToastMessage(resultText);

        } catch (NumberFormatException e) {
            showToastMessage("Invalid input! Please enter a valid number.");
        }
    }


    private String createResultMessage(boolean isMatch, int userNum, int randomNum) {
        if (isMatch) {
            return "Success! Your favorite number " + userNum + " matches the random number!";
        } else {
            return "No match. Your number: " + userNum + ", Random number: " + randomNum;
        }
    }


    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}


class FavoriteNumber {
    // Rule 8: Private variable
    private int storedValue;

    public FavoriteNumber(int value) {
        setStoredValue(value);
    }

    // Rule 8: Method to interact with variable
    public int getStoredValue() {
        return storedValue;
    }

    // Rule 8: Method to interact with variable
    public void setStoredValue(int value) {
        // Rule 9: Check values before initialization
        if (value < 0) {
            this.storedValue = 0;
        } else {
            this.storedValue = value;
        }
    }
}


class NumberGameManager {
    private int currentRandomNumber;


    public int generateNewRandomNumber(int min, int max) {
        currentRandomNumber = (int) (Math.random() * (max - min + 1) + min);
        return currentRandomNumber;
    }


    public boolean checkIfMatch(int userNumber) {
        return userNumber == currentRandomNumber;
    }
}
