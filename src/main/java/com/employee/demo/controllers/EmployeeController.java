package com.employee.demo.controllers;

import com.employee.demo.dto.*;
import com.employee.demo.entity.Employee;
import com.employee.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public PaginatedResponse<EmployeeResponse> getAllEmployees(
            @RequestParam(required = false) Integer manager,
            @RequestParam(required = false) Integer department,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {

        return employeeService.getAllEmployees(manager, department, name, page, limit);
    }

    @GetMapping("/all-employees")
    public List<EmployeeResponse> getBulkEmployees() {
        return employeeService.getBulkEmployees();
    }

    @GetMapping("/managers")
    public List<EmployeeResponse> getAllManagers() {
        return employeeService.getAllManagers();
    }

    @GetMapping("{id}")
    public EmployeeResponse getEmployeeById(@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/add")
    public EmployeeResponse addEmployee(@RequestBody Employee employee){
        return employeeService.addEmployee(employee);
    }

    @PostMapping("/add-bulk")
    public ResponseEntity<?> uploadEmployees(@RequestParam("file") MultipartFile file) {
        try {
            String message = employeeService.addEmployees(file);
            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error uploading file: " + e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable int id){
        employeeService.deleteEmployee(id);
    }

    /**
     * Overview totals
     * segment => all | online | enterprise | both
     */
    @GetMapping("/overview")
    public OverviewResponse getOverview(@RequestParam(defaultValue = "all") String segment) {
        return employeeService.getOverview(segment);
    }

    /**
     * Onboarding funnel counts by stage for a date range (defaults to last 6 months)
     * rangeFrom / rangeTo optional
     * segment => all | online | enterprise | both
     */
    @GetMapping("/onboarding/funnel")
    public FunnelResponse getOnboardingFunnel(
            @RequestParam(defaultValue = "all") String segment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeTo
    ) {
        return employeeService.getFunnel(segment, rangeFrom, rangeTo);
    }

    /**
     * Onboarding time summary (average time in days) and distribution buckets (1 day, 3 days, 1 week, >1 week)
     */
    @GetMapping("/onboarding/time")
    public OnboardingTimeResponse getOnboardingTimes(
            @RequestParam(defaultValue = "all") String segment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeTo
    ) {
        return employeeService.getOnboardingTimeStats(segment, rangeFrom, rangeTo);
    }

    /**
     * Engagement: returns timeseries for login counts or onboarding events
     * metric = login | onboarding
     * granularity = daily | weekly | monthly
     */
    @GetMapping("/engagement")
    public EngagementResponse getEngagement(
            @RequestParam(defaultValue = "login") String metric,
            @RequestParam(defaultValue = "daily") String granularity,
            @RequestParam(defaultValue = "all") String segment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeTo
    ) {
        return employeeService.getEngagementTimeseries(metric, granularity, segment, rangeFrom, rangeTo);
    }
}
