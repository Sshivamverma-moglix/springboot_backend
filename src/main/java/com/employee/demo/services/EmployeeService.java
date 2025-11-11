package com.employee.demo.services;

import com.employee.demo.dto.PaginatedResponse;
import com.employee.demo.entity.Employee;
import com.employee.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public PaginatedResponse<Employee> getAllEmployees(String manager, String department, String name, Integer page, Integer limit) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (limit != null && limit > 0) ? limit : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Employee> resultPage;

        if (manager != null && department != null) {
            resultPage = employeeRepository.findByManagerNameAndDepartmentName(manager, department, pageable);
        } else if (manager != null) {
            resultPage = employeeRepository.findByManagerName(manager, pageable);
        } else if (department != null) {
            resultPage = employeeRepository.findByDepartmentName(department, pageable);
        } else if (name != null) {
            resultPage = employeeRepository.findByName(name, pageable);
        } else {
            resultPage = employeeRepository.findAll(pageable);
        }

        return new PaginatedResponse<>(
                resultPage.getContent(),
                resultPage.getTotalElements(),
                pageNumber,
                pageSize
        );
    }

    public List<Employee> getAllManagers() {
        return employeeRepository.findAllManagers();
    }

    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getBulkEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public String addEmployees(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            List<Employee> employees = new ArrayList<>();

            for (CSVRecord record : records) {
                Employee e = new Employee();
                e.setName(record.get("name"));
                e.setEmail(record.get("email"));
                e.setDesignation(record.get("designation"));
                e.setPhone(record.get("phone"));
                e.setManagerName(record.get("managerName"));
                e.setDepartmentName(record.get("departmentName"));
                employees.add(e);
            }

            employeeRepository.saveAll(employees);
            return "Uploaded " + employees.size() + " employees successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
    }

    public Employee updateEmployee(int id, Employee employee) {
        Employee existing = employeeRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(employee.getName());
            existing.setEmail(employee.getEmail());
            existing.setDesignation(employee.getDesignation());
            existing.setPhone(employee.getPhone());
            existing.setManagerName(employee.getManagerName());
            existing.setDepartmentName(employee.getDepartmentName());
            return employeeRepository.save(existing);
        }
        return null;
    }

    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}
