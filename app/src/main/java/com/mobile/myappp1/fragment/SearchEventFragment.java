package com.mobile.myappp1.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mobile.myappp1.MainActivity;
import com.mobile.myappp1.R;
import com.mobile.myappp1.adapter.EventAdapter;
import com.mobile.myappp1.model.Event;
import com.mobile.myappp1.viewmodel.EventViewModel;
import java.util.List;
import java.util.stream.Collectors;

public class SearchEventFragment extends Fragment {

    private EventAdapter adapter;
    private TextView tvEmpty;
    private EditText etSearch;
    private EventViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_event, container, false);

        tvEmpty = view.findViewById(R.id.tv_empty);
        etSearch = view.findViewById(R.id.et_search);
        RecyclerView recycler = view.findViewById(R.id.recycler_events);

        adapter = new EventAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        view.findViewById(R.id.btn_go_to_add).setOnClickListener(v ->
                ((MainActivity) requireActivity()).showAddScreen());

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> filterEvents(etSearch.getText().toString()));

        return view;
    }

    private void filterEvents(String query) {
        List<Event> allEvents = viewModel.getEvents().getValue();
        if (allEvents == null) return;

        List<Event> filtered = allEvents.stream()
                .filter(e -> e.toString().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        adapter.updateEvents(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}