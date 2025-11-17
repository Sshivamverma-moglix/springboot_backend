package com.employee.demo.specifications;

import com.employee.demo.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {
    public static Specification<Employee> hasManagerId(Integer managerId) {
        return (root, query, cb) ->
                managerId == null ? null : cb.equal(root.get("managerId"), managerId);
    }

    public static Specification<Employee> hasDepartmentId(Integer departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null : cb.equal(root.get("departmentId"), departmentId);
    }

    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isEmpty())
                        ? null
                        : cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%");
    }
}
