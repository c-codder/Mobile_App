package com.mobile.myappp1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.mobile.myappp1.model.Event;
import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> _events = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Event>> getEvents() {
        return _events;
    }

    public void addEvent(Event event) {
        List<Event> currentEvents = _events.getValue();
        if (currentEvents != null) {
            List<Event> updatedEvents = new ArrayList<>(currentEvents);
            updatedEvents.add(event);
            _events.setValue(updatedEvents);
        }
    }
}