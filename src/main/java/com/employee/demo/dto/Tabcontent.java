package com.employee.demo.dto;

public class Tabcontent {
    private Integer totalSuppliers;
    private Integer activeSuppliers;
    private Integer disablesSuppliers;
    private Integer profileIncomplete;

    public Tabcontent(Integer totalSuppliers, Integer activeSuppliers, Integer disablesSuppliers, Integer profileIncomplete) {
        this.totalSuppliers = totalSuppliers;
        this.activeSuppliers = activeSuppliers;
        this.disablesSuppliers = disablesSuppliers;
        this.profileIncomplete = profileIncomplete;
    }


    public Integer getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Integer totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    public Integer getActiveSuppliers() {
        return activeSuppliers;
    }

    public void setActiveSuppliers(Integer activeSuppliers) {
        this.activeSuppliers = activeSuppliers;
    }

    public Integer getDisablesSuppliers() {
        return disablesSuppliers;
    }

    public void setDisablesSuppliers(Integer disablesSuppliers) {
        this.disablesSuppliers = disablesSuppliers;
    }

    public Integer getProfileIncomplete() {
        return profileIncomplete;
    }

    public void setProfileIncomplete(Integer profileIncomplete) {
        this.profileIncomplete = profileIncomplete;
    }
}
