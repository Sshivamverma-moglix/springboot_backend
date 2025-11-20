package com.employee.demo.dto;

import java.util.Map;

public class OnboardingTimeResponse {
    private double averageDays;
    private Map<String, Integer> buckets;
    private int notActivatedCount;

    public OnboardingTimeResponse() {}
    public OnboardingTimeResponse(double avgDays, Map<String, Integer> buckets, int notActivatedCount) {
        this.averageDays = avgDays;
        this.buckets = buckets;
        this.notActivatedCount = notActivatedCount;
    }

    public double getAverageDays() { return averageDays; }
    public void setAverageDays(double averageDays) { this.averageDays = averageDays; }
    public Map<String, Integer> getBuckets() { return buckets; }
    public void setBuckets(Map<String, Integer> buckets) { this.buckets = buckets; }
    public int getNotActivatedCount() { return notActivatedCount; }
    public void setNotActivatedCount(int notActivatedCount) { this.notActivatedCount = notActivatedCount; }
}
