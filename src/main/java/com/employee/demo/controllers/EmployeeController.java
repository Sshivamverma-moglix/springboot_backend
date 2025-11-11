package com.employee.demo.controllers;

import com.employee.demo.dto.PaginatedResponse;
import com.employee.demo.entity.Employee;
import com.employee.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public PaginatedResponse<Employee> getAllEmployees(
            @RequestParam(required = false) String manager,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {

        return employeeService.getAllEmployees(manager, department, name, page, limit);
    }

    @GetMapping("/all-employees")
    public List<Employee> getBulkEmployees() {
        return employeeService.getBulkEmployees();
    }

    @GetMapping("/managers")
    public List<Employee> getAllManagers() {
        return employeeService.getAllManagers();
    }

    @GetMapping("{id}")
    public Employee getEmployeeById(@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee employee){
        System.out.println(employee);
        return employeeService.addEmployee(employee);
    }

    @PostMapping("/add-bulk")
    public ResponseEntity<String> uploadEmployees(@RequestParam("file") MultipartFile file) {
        try {
            String message = employeeService.addEmployees(file);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body("{\"message\": \"" + message + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error uploading file: " + e.getMessage() + "\"}");
        }
    }


    @PutMapping("{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable int id){
        employeeService.deleteEmployee(id);
    }
}
