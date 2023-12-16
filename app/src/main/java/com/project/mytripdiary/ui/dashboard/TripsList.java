package com.project.mytripdiary.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

public class TripsList {
    private String trip;
    private String startDate;
    private String endDate;

    // 생성자, Getter 및 Setter 메서드

    // ...

    public TripsList(String trip, String startDate, String endDate) {
        this.trip = trip;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
