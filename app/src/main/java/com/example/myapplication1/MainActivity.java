package com.example.myapplication1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_HOME_ADDRESS = "home_address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LAST_RANDOM = "last_random";
    private static final String KEY_BEFORE_ROTATION = "before_rotation";
    private static final String KEY_ORIENTATION_TIME = "orientation_time";
    private static final String KEY_SHOW_ORIENTATION_TOAST = "show_orientation_toast";

    private EditText etFirstName, etLastName, etBirthday, etPhone, etHomeAddress, etEmail;
    private ImageView ivLogo;
    private LinearLayout landscapeExtraFields;
    private TextView tvRandomNumber, tvBeforeRotation;

    private final Random random = new Random();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int lastRandomNumber = 0;
    private String orientationTimestamp;
    private boolean showOrientationToast = false;

    private final Runnable randomNumberRunnable = new Runnable() {
        @Override
        public void run() {
            if (tvRandomNumber != null) {
                lastRandomNumber = random.nextInt(10000);
                tvRandomNumber.setText(String.valueOf(lastRandomNumber));
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
        applyOrientationVisibility(getResources().getConfiguration());
        updateLogoForOrientation(getResources().getConfiguration());

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        startRandomNumberUpdates();
    }

    private void initViews() {
        ivLogo = findViewById(R.id.iv_logo);
        landscapeExtraFields = findViewById(R.id.landscape_extra_fields);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etBirthday = findViewById(R.id.et_birthday);
        etPhone = findViewById(R.id.et_phone);
        etHomeAddress = findViewById(R.id.et_home_address);
        etEmail = findViewById(R.id.et_email);
        tvRandomNumber = findViewById(R.id.tv_random_number);
        tvBeforeRotation = findViewById(R.id.tv_before_rotation);

        findViewById(R.id.btn_reset).setOnClickListener(v -> resetRegistrationForm());
        findViewById(R.id.btn_send).setOnClickListener(v -> {
            Toast.makeText(this, "Send clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void applyOrientationVisibility(Configuration config) {
        boolean isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
        landscapeExtraFields.setVisibility(isLandscape ? View.VISIBLE : View.GONE);
    }

    private void updateLogoForOrientation(Configuration config) {
        boolean isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
        ivLogo.setImageResource(isLandscape ? R.drawable.contact_icon2 : R.drawable.contact_icon);
    }

    private void resetRegistrationForm() {
        etFirstName.setText("");
        etLastName.setText("");
        etBirthday.setText("");
        etPhone.setText("");
        etHomeAddress.setText("");
        etEmail.setText("");
    }

    private void startRandomNumberUpdates() {
        handler.removeCallbacks(randomNumberRunnable);
        handler.post(randomNumberRunnable);
    }

    private void restoreState(Bundle state) {
        etFirstName.setText(state.getString(KEY_FIRST_NAME, ""));
        etLastName.setText(state.getString(KEY_LAST_NAME, ""));
        etBirthday.setText(state.getString(KEY_BIRTHDAY, ""));
        etPhone.setText(state.getString(KEY_PHONE, ""));
        etHomeAddress.setText(state.getString(KEY_HOME_ADDRESS, ""));
        etEmail.setText(state.getString(KEY_EMAIL, ""));

        lastRandomNumber = state.getInt(KEY_LAST_RANDOM, 0);
        int randomBeforeRotation = state.getInt(KEY_BEFORE_ROTATION, 0);
        orientationTimestamp = state.getString(KEY_ORIENTATION_TIME, "");
        showOrientationToast = state.getBoolean(KEY_SHOW_ORIENTATION_TOAST, false);

        tvRandomNumber.setText(String.valueOf(lastRandomNumber));
        tvBeforeRotation.setText(String.valueOf(randomBeforeRotation));

        if (showOrientationToast && orientationTimestamp != null && !orientationTimestamp.isEmpty()) {
            handler.post(() -> Toast.makeText(this,
                getString(R.string.orientation_changed_at) + " " + orientationTimestamp,
                Toast.LENGTH_LONG).show());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FIRST_NAME, etFirstName.getText().toString());
        outState.putString(KEY_LAST_NAME, etLastName.getText().toString());
        outState.putString(KEY_BIRTHDAY, etBirthday.getText().toString());
        outState.putString(KEY_PHONE, etPhone.getText().toString());
        outState.putString(KEY_HOME_ADDRESS, etHomeAddress.getText().toString());
        outState.putString(KEY_EMAIL, etEmail.getText().toString());
        outState.putInt(KEY_LAST_RANDOM, lastRandomNumber);
        outState.putInt(KEY_BEFORE_ROTATION, lastRandomNumber);
        outState.putString(KEY_ORIENTATION_TIME,
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        outState.putBoolean(KEY_SHOW_ORIENTATION_TOAST, true);
    }
}
