package com.employee.demo.dto;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> data;
    private long totalRecords;
    private int page;
    private int limit;
    private int currentCount;

    public PaginatedResponse(List<T> data, long totalRecords, int page, int limit) {
        this.data = data;
        this.totalRecords = totalRecords;
        this.page = page;
        this.limit = limit;
        this.currentCount = data.size();
    }

    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
    public long getTotalRecords() {
        return totalRecords;
    }
    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    public int getCurrentCount() {
        return currentCount;
    }
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
}
