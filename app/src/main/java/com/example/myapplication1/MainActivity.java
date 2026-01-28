package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String tag = "EVH_Demo: ";
    private long lastTime = 0;

    // Form fields
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etBirthday;
    private EditText etPhone;
    private EditText etHomeAddress;
    private EditText etEmailAddress;
    
    // Labels
    private TextView tvRandomNumber;
    private TextView tvLastRandomNumber;
    
    // Logo ImageView
    private ImageView ivLogo;
    
    // State variables
    private int currentRandomNumber = 0;
    private int lastRandomNumberBeforeOrientationChange = 0;
    private String orientationChangeDateTime = "";
    private Handler randomNumberHandler;
    private Runnable randomNumberRunnable;
    private boolean isLandscape = false;

    private void logLifecycle(String event) {
        long currentTime = System.currentTimeMillis();
        long elapsed = (lastTime == 0) ? 0 : (currentTime - lastTime);
        Log.d(tag, tag + event + " - Elapsed since last event: " + elapsed + "ms");
        lastTime = currentTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Determine orientation
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        
        setContentView(R.layout.activity_main);
        
        lastTime = System.currentTimeMillis();
        logLifecycle("onCreate()");

        // Initialize views
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etBirthday = findViewById(R.id.et_birthday);
        etPhone = findViewById(R.id.et_phone);
        tvRandomNumber = findViewById(R.id.tv_random_number);
        tvLastRandomNumber = findViewById(R.id.tv_last_random_number);
        ivLogo = findViewById(R.id.iv_logo);
        
        // Landscape-only fields (may be null in portrait mode)
        etHomeAddress = findViewById(R.id.et_home_address);
        etEmailAddress = findViewById(R.id.et_email_address);
        
        // Set initial logo based on orientation
        updateLogoImage();

        // Restore saved state if available
        if (savedInstanceState != null) {
            restoreFormData(savedInstanceState);
            currentRandomNumber = savedInstanceState.getInt("currentRandomNumber", 0);
            lastRandomNumberBeforeOrientationChange = savedInstanceState.getInt("lastRandomNumberBeforeOrientationChange", 0);
            orientationChangeDateTime = savedInstanceState.getString("orientationChangeDateTime", "");
            
            // Update UI with restored values
            if (currentRandomNumber > 0) {
                updateRandomNumberDisplay();
            }
            if (lastRandomNumberBeforeOrientationChange > 0) {
                tvLastRandomNumber.setText(getString(R.string.last_random_number_label, lastRandomNumberBeforeOrientationChange));
                tvLastRandomNumber.setVisibility(View.VISIBLE);
            }
        }

        // Start random number generation
        startRandomNumberGeneration();
        
        // Show Toast if orientation change occurred
        if (!TextUtils.isEmpty(orientationChangeDateTime)) {
            showOrientationChangeToast();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        // Save form data
        saveFormData(outState);
        
        // Save random number state
        outState.putInt("currentRandomNumber", currentRandomNumber);
        outState.putInt("lastRandomNumberBeforeOrientationChange", lastRandomNumberBeforeOrientationChange);
        outState.putString("orientationChangeDateTime", orientationChangeDateTime);
        
        logLifecycle("onSaveInstanceState()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        boolean wasLandscape = isLandscape;
        isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        
        // If orientation actually changed
        if (wasLandscape != isLandscape) {
            // Save current form data before layout change
            Bundle savedData = new Bundle();
            saveFormData(savedData);
            
            // Save current random number before orientation change
            lastRandomNumberBeforeOrientationChange = currentRandomNumber;
            
            // Save current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            orientationChangeDateTime = sdf.format(new Date());
            
            // Reload layout to get the correct layout file for new orientation
            setContentView(R.layout.activity_main);
            
            // Re-initialize all views after layout reload
            etFirstName = findViewById(R.id.et_first_name);
            etLastName = findViewById(R.id.et_last_name);
            etBirthday = findViewById(R.id.et_birthday);
            etPhone = findViewById(R.id.et_phone);
            tvRandomNumber = findViewById(R.id.tv_random_number);
            tvLastRandomNumber = findViewById(R.id.tv_last_random_number);
            ivLogo = findViewById(R.id.iv_logo);
            etHomeAddress = findViewById(R.id.et_home_address);
            etEmailAddress = findViewById(R.id.et_email_address);
            
            // Restore form data
            restoreFormData(savedData);
            
            // Update logo image for new orientation
            updateLogoImage();
            
            // Update random number display
            updateRandomNumberDisplay();
            
            // Update last random number display
            if (tvLastRandomNumber != null) {
                tvLastRandomNumber.setText(getString(R.string.last_random_number_label, lastRandomNumberBeforeOrientationChange));
                tvLastRandomNumber.setVisibility(View.VISIBLE);
            }
            
            // Restart random number generation
            if (randomNumberHandler != null && randomNumberRunnable != null) {
                randomNumberHandler.removeCallbacks(randomNumberRunnable);
            }
            startRandomNumberGeneration();
            
            // Show Toast in the new orientation
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showOrientationChangeToast();
                }
            }, 500); // Small delay to ensure UI is ready
            
            Log.d(tag, "Orientation changed from " + (wasLandscape ? "landscape" : "portrait") + 
                      " to " + (isLandscape ? "landscape" : "portrait") + " at " + orientationChangeDateTime);
        }
        
        logLifecycle("onConfigurationChanged()");
    }
    
    private void updateLogoImage() {
        if (ivLogo != null) {
            if (isLandscape) {
                // Landscape mode - use contact_icon2
                ivLogo.setImageResource(R.drawable.contact_icon2);
            } else {
                // Portrait mode - use contact_icon
                ivLogo.setImageResource(R.drawable.contact_icon);
            }
        }
    }

    private void saveFormData(Bundle outState) {
        outState.putString("firstName", etFirstName.getText().toString());
        outState.putString("lastName", etLastName.getText().toString());
        outState.putString("birthday", etBirthday.getText().toString());
        outState.putString("phone", etPhone.getText().toString());
        
        if (etHomeAddress != null) {
            outState.putString("homeAddress", etHomeAddress.getText().toString());
        }
        if (etEmailAddress != null) {
            outState.putString("emailAddress", etEmailAddress.getText().toString());
        }
    }

    private void restoreFormData(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("firstName")) {
            etFirstName.setText(savedInstanceState.getString("firstName"));
        }
        if (savedInstanceState.containsKey("lastName")) {
            etLastName.setText(savedInstanceState.getString("lastName"));
        }
        if (savedInstanceState.containsKey("birthday")) {
            etBirthday.setText(savedInstanceState.getString("birthday"));
        }
        if (savedInstanceState.containsKey("phone")) {
            etPhone.setText(savedInstanceState.getString("phone"));
        }
        
        if (etHomeAddress != null && savedInstanceState.containsKey("homeAddress")) {
            etHomeAddress.setText(savedInstanceState.getString("homeAddress"));
        }
        if (etEmailAddress != null && savedInstanceState.containsKey("emailAddress")) {
            etEmailAddress.setText(savedInstanceState.getString("emailAddress"));
        }
    }

    private void startRandomNumberGeneration() {
        randomNumberHandler = new Handler(Looper.getMainLooper());
        randomNumberRunnable = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                currentRandomNumber = random.nextInt(1000); // Random number between 0-999
                updateRandomNumberDisplay();
                
                // Schedule next update in 1 second
                randomNumberHandler.postDelayed(this, 1000);
            }
        };
        
        // Start immediately
        randomNumberRunnable.run();
    }

    private void updateRandomNumberDisplay() {
        if (tvRandomNumber != null) {
            tvRandomNumber.setText(getString(R.string.random_number_label, currentRandomNumber));
        }
    }

    private void showOrientationChangeToast() {
        if (!TextUtils.isEmpty(orientationChangeDateTime)) {
            String message = getString(R.string.orientation_changed, orientationChangeDateTime);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        logLifecycle("onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logLifecycle("onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logLifecycle("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logLifecycle("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logLifecycle("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Stop random number generation
        if (randomNumberHandler != null && randomNumberRunnable != null) {
            randomNumberHandler.removeCallbacks(randomNumberRunnable);
        }
        
        logLifecycle("onDestroy()");
    }
}
