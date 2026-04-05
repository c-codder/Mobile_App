package com.mobile.myappp1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.mobile.myappp1.fragment.SubmitEventFragment;
import com.mobile.myappp1.fragment.SearchEventFragment;
import com.mobile.myappp1.viewmodel.EventViewModel;

public class MainActivity extends AppCompatActivity {

    private EventViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(EventViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new SearchEventFragment())
                    .commit();
        }
    }

    public void showAddScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SubmitEventFragment())
                .addToBackStack(null)
                .commit();
    }
}