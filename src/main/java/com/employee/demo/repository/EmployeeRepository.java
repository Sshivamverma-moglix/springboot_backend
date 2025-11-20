package com.employee.demo.repository;

import com.employee.demo.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
    @Query("SELECT DISTINCT e FROM Employee e " +
            "WHERE e.managerId IS NULL " +
            "OR e.id IN (SELECT DISTINCT emp.managerId FROM Employee emp WHERE emp.managerId IS NOT NULL)")
    List<Employee> findAllManagers();

}
