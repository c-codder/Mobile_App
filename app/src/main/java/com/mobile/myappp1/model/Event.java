package com.mobile.myappp1.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class Event implements Serializable {
    private String type;
    private int year, month, day;
    private int hour, minute;

    public Event(String type, int year, int month, int day, int hour, int minute) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getType() {
        return type;
    }

    public String getFormattedDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        // "15 Mar" format
        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
        return String.format(Locale.US, "%02d %s", day, monthName);
    }

    public String getFormattedTime() {
        return String.format(Locale.US, "%02d:%02d", hour, minute);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%s – %s – %s",
                type, getFormattedDate(), getFormattedTime());
    }
}
