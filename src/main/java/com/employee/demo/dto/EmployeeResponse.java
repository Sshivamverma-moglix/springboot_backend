package com.employee.demo.dto;

import java.time.LocalDate;

public class EmployeeResponse {
    private int id;
    private String name;
    private String email;
    private String designation;
    private String phone;
    private String managerName;
    private String departmentName;
    private LocalDate createdDate;

    public EmployeeResponse(int id, String name, String email, String designation,
                            String phone, String managerName, String departmentName,
                            LocalDate createdDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.phone = phone;
        this.managerName = managerName;
        this.departmentName = departmentName;
        this.createdDate = createdDate;
    }

    // ✅ Getters only (no setters needed for response)
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDesignation() { return designation; }
    public String getPhone() { return phone; }
    public String getManager() { return managerName; }
    public String getDepartment() { return departmentName; }
    public LocalDate getCreatedDate() { return createdDate; }

}
