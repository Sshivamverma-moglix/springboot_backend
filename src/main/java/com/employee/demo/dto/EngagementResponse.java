package com.employee.demo.dto;

import java.util.List;

public class EngagementResponse {
    private String metric;
    private String granularity;
    private String segment;
    private int average;
    private List<TimeseriesPoint> series;

    public EngagementResponse() {}
    public EngagementResponse(String metric, String granularity, String segment, int average, List<TimeseriesPoint> series) {
        this.metric = metric;
        this.granularity = granularity;
        this.segment = segment;
        this.average = average;
        this.series = series;
    }

    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
    public String getGranularity() { return granularity; }
    public void setGranularity(String granularity) { this.granularity = granularity; }
    public String getSegment() { return segment; }
    public void setSegment(String segment) { this.segment = segment; }
    public int getAverage() { return average; }
    public void setAverage(int average) { this.average = average; }
    public List<TimeseriesPoint> getSeries() { return series; }
    public void setSeries(List<TimeseriesPoint> series) { this.series = series; }
}
