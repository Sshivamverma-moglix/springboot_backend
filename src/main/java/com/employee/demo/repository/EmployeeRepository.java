package com.employee.demo.repository;

import com.employee.demo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // find by manager name
    @Query("SELECT e FROM Employee e WHERE e.managerName = :managerName")
    Page<Employee> findByManagerName(@Param("managerName") String managerName, Pageable pageable);

    // Find by department name
    @Query("SELECT e FROM Employee e WHERE e.departmentName = :departmentName")
    Page<Employee> findByDepartmentName(@Param("departmentName") String departmentName, Pageable pageable);

    // Find by both manager ID and department ID
    @Query("SELECT e FROM Employee e WHERE e.managerName = :managerName AND e.departmentName = :departmentName")
    Page<Employee> findByManagerNameAndDepartmentName(@Param("managerName") String managerName,
                                                  @Param("departmentName") String departmentName,
                                                  Pageable pageable);

    @Query("SELECT DISTINCT e FROM Employee e WHERE e.name IN (SELECT DISTINCT emp.managerName FROM Employee emp WHERE emp.managerName IS NOT NULL AND emp.managerName <> '') OR e.managerName IS NULL OR e.managerName = '' ")
    List<Employee> findAllManagers();

    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT(:name, '%'))")
    Page<Employee> findByName(@Param("name") String name, Pageable pageable);
}
