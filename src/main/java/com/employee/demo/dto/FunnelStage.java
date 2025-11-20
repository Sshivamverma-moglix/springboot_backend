package com.employee.demo.dto;

public class FunnelStage {
    private String name;
    private int count;

    public FunnelStage() {}
    public FunnelStage(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
