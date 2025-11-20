package com.employee.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String designation;
    private String phone;
    @Column(name = "manager_id")
    private Integer managerId;
    @Column(name = "department_id")
    private Integer departmentId;
    @Column(name="created_date")
    private LocalDate createdDate;

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDate.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Employee manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Employee> subordinates;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDate date) {
        this.createdDate = date;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getManager() {
        return this.manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getSubordinates() { return subordinates; }
    public void setSubordinates(List<Employee> subordinates) { this.subordinates = subordinates; }
}
