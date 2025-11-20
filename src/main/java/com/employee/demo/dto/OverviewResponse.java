package com.employee.demo.dto;

public class OverviewResponse {
    private int totalSuppliers;
    private int activeSuppliers;
    private int disabledSuppliers;
    private int profileIncomplete;

    public OverviewResponse() {}

    public OverviewResponse(int total, int active, int disabled, int incomplete) {
        this.totalSuppliers = total;
        this.activeSuppliers = active;
        this.disabledSuppliers = disabled;
        this.profileIncomplete = incomplete;
    }

    // getters + setters
    public int getTotalSuppliers() { return totalSuppliers; }
    public void setTotalSuppliers(int totalSuppliers) { this.totalSuppliers = totalSuppliers; }
    public int getActiveSuppliers() { return activeSuppliers; }
    public void setActiveSuppliers(int activeSuppliers) { this.activeSuppliers = activeSuppliers; }
    public int getDisabledSuppliers() { return disabledSuppliers; }
    public void setDisabledSuppliers(int disabledSuppliers) { this.disabledSuppliers = disabledSuppliers; }
    public int getProfileIncomplete() { return profileIncomplete; }
    public void setProfileIncomplete(int profileIncomplete) { this.profileIncomplete = profileIncomplete; }
}
