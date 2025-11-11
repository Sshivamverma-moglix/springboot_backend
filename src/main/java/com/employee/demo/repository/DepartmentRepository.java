package com.employee.demo.repository;


import com.employee.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  DepartmentRepository extends JpaRepository<Department, Integer> {
}
