package com.example.liveearhmap2026.dashboard;

public class DashboardItem {

    private int icon;
    private String title;

    public DashboardItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}