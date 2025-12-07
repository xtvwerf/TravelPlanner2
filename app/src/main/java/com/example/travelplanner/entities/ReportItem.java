package com.example.travelplanner.entities;

public abstract class ReportItem {

    private final String title;
    private final String primaryInfo;
    private final String secondaryInfo;

    protected ReportItem(String title, String primaryInfo, String secondaryInfo) {
        this.title = title;
        this.primaryInfo = primaryInfo;
        this.secondaryInfo = secondaryInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getPrimaryInfo() {
        return primaryInfo;
    }

    public String getSecondaryInfo() {
        return secondaryInfo;
    }

    public abstract String getItemType();
}
