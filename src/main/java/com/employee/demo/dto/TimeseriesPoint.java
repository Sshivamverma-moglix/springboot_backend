package com.employee.demo.dto;

public class TimeseriesPoint {
    private String label;
    private int value;

    public TimeseriesPoint() {}
    public TimeseriesPoint(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}
