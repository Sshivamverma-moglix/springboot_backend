package com.employee.demo.dto;

import java.util.List;

public class FunnelResponse {
    private List<FunnelStage> stages;
    private List<TimeseriesPoint> dailyCounts; // optional detailed series

    public FunnelResponse() {}
    public FunnelResponse(List<FunnelStage> stages, List<TimeseriesPoint> dailyCounts) {
        this.stages = stages;
        this.dailyCounts = dailyCounts;
    }

    public List<FunnelStage> getStages() { return stages; }
    public void setStages(List<FunnelStage> stages) { this.stages = stages; }
    public List<TimeseriesPoint> getDailyCounts() { return dailyCounts; }
    public void setDailyCounts(List<TimeseriesPoint> dailyCounts) { this.dailyCounts = dailyCounts; }
}
