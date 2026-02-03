package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    String tag = "EVH_Demo: ";
    private long lastTime = 0;

    private void logLifecycle(String event) {
        long currentTime = System.currentTimeMillis();
        long elapsed = (lastTime == 0) ? 0 : (currentTime - lastTime);
        Log.d(tag, tag + event + " - Elapsed since last event: " + elapsed + "ms");
        lastTime = currentTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_main);
        
        lastTime = System.currentTimeMillis();
        logLifecycle("onCreate()");

        final EditText etFirstName = findViewById(R.id.et_first_name);
        final EditText etLastName = findViewById(R.id.et_last_name);
        final EditText etPhone = findViewById(R.id.et_phone);
        final EditText etComment = findViewById(R.id.et_comment);
        Button btnReset = findViewById(R.id.btn_reset);
        Button btnSend = findViewById(R.id.btn_send);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstName.setText("");
                etLastName.setText("");
                etPhone.setText("");
                etComment.setText("");
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Feedback Sent");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        

        /*if (getWindow() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            

            int width = (int) (displayMetrics.widthPixels * 0.90);
            int height = (int) (displayMetrics.heightPixels * 0.70);
            
            getWindow().setLayout(width, height);
        }*/
        
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
        logLifecycle("onDestroy()");
    }
}