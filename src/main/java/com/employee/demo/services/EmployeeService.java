package com.employee.demo.services;

import com.employee.demo.dto.EmployeeResponse;
import com.employee.demo.dto.PaginatedResponse;
import com.employee.demo.entity.Employee;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.specifications.EmployeeSpecifications;
import com.employee.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import com.employee.demo.utils.Utils.*;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public PaginatedResponse<EmployeeResponse> getAllEmployees(Integer manager, Integer department, String name, Integer page, Integer limit) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (limit != null && limit > 0) ? limit : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        // ✅ Combine all filters dynamically
        Specification<Employee> spec = Specification
                .where(EmployeeSpecifications.hasManagerId(manager))
                .and(EmployeeSpecifications.hasDepartmentId(department))
                .and(EmployeeSpecifications.hasName(name));

        // ✅ One repository call handles all combinations
        Page<Employee> resultPage = employeeRepository.findAll(spec, pageable);

        List<EmployeeResponse> employeeResponses = resultPage.getContent().stream()
                .map(e -> new EmployeeResponse(
                        e.getId(),
                        e.getName(),
                        e.getEmail(),
                        e.getDesignation(),
                        e.getPhone(),
                        e.getManager() != null ? e.getManager().getName() : null,
                        e.getDepartment() != null ? e.getDepartment().getName() : null,
                        e.getCreatedDate()
                ))
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                employeeResponses,
                resultPage.getTotalElements(),
                pageNumber,
                pageSize
        );
    }

    public List<EmployeeResponse> getAllManagers() {

        return employeeRepository.findAllManagers().stream()
                .map(e -> new EmployeeResponse(
                        e.getId(),
                        e.getName(),
                        e.getEmail(),
                        e.getDesignation(),
                        e.getPhone(),
                        e.getManager() != null ? e.getManager().getName() : null,
                        e.getDepartment() != null ? e.getDepartment().getName() : null,
                        e.getCreatedDate()
                ))
                .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployeeById(int id) {
        return employeeRepository.findById(id)
                .map(e -> new EmployeeResponse(
                        e.getId(),
                        e.getName(),
                        e.getEmail(),
                        e.getDesignation(),
                        e.getPhone(),
                        e.getManager() != null ? e.getManager().getName() : null,
                        e.getDepartment() != null ? e.getDepartment().getName() : null,
                        e.getCreatedDate()
                ))
                .orElse(null);
    }


    public List<EmployeeResponse> getBulkEmployees() {
        return employeeRepository.findAll().stream().map(
                e -> new EmployeeResponse(
                        e.getId(),
                        e.getName(),
                        e.getEmail(),
                        e.getDesignation(),
                        e.getPhone(),
                        e.getManager() != null ? e.getManager().getName() + " (" + e.getManager().getId() + ")" : null,
                        e.getDepartment() != null ? e.getDepartment().getName() : null,
                        e.getCreatedDate()
                )).collect(Collectors.toList());
    }

    public EmployeeResponse addEmployee(Employee employee) {
        Employee e=  employeeRepository.save(employee);

        EmployeeResponse response = new EmployeeResponse(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getDesignation(),
                e.getPhone(),
                e.getManager() != null ? e.getManager().getName() : null,
                e.getDepartment() != null ? e.getDepartment().getName() : null,
                e.getCreatedDate()
        );
        return  response;
    }

    public String addEmployees(MultipartFile file) throws IOException {
        try{
            String fileName = file.getOriginalFilename();
            if(fileName == null){
                return "file not found";
            }

            List<Employee> employees = new ArrayList<>();

            if(fileName.endsWith(".csv")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                for (CSVRecord record : records) {
                    Employee e = new Employee();
                    e.setName(record.get("name"));
                    e.setEmail(record.get("email"));
                    e.setDesignation(record.get("designation"));
                    e.setPhone(record.get("phone"));
                    // ✅ Convert managerId safely
                    String managerIdStr = record.get("managerId");
                    if (managerIdStr != null && !managerIdStr.isEmpty()) {
                        e.setManagerId(Integer.parseInt(managerIdStr));
                    } else {
                        e.setManagerId(null);
                    }
                    // ✅ Convert departmentId safely
                    String departmentIdStr = record.get("departmentId");
                    if (departmentIdStr != null && !departmentIdStr.isEmpty()) {
                        e.setDepartmentId(Integer.parseInt(departmentIdStr));
                    } else {
                        e.setDepartmentId(null);
                    }

                    // ✅ Set current date
                    e.setCreatedDate(LocalDate.now());

                    employees.add(e);
                }
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                Workbook workBook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workBook.getSheetAt(0);

                boolean isHeader = true;

                for(Row row : sheet) {
                    if(isHeader) {
                        isHeader = false;
                        continue;
                    }

                    employees.add(
                            Utils.convertRecordToEmployee(
                                    Utils.getCellValue(row.getCell(0)),
                                    Utils.getCellValue(row.getCell(1)),
                                    Utils.getCellValue(row.getCell(2)),
                                    Utils.getCellValue(row.getCell(3)),
                                    Utils.getCellValue(row.getCell(4)),
                                    Utils.getCellValue(row.getCell(5))
                            )
                    );
                }
                workBook.close();
            } else {
                return "Unsupported file type";
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
            existing.setManagerId(employee.getManagerId());
            existing.setDepartmentId(employee.getDepartmentId());
            return employeeRepository.save(existing);
        }
        return null;
    }

    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}
