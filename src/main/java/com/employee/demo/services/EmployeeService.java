package com.employee.demo.services;

import com.employee.demo.dto.EmployeeResponse;
import com.employee.demo.dto.PaginatedResponse;
import com.employee.demo.dto.*;
import com.employee.demo.entity.Employee;
import com.employee.demo.exceptions.ResourceNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Random rnd = new Random(1234);

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private EmployeeResponse convertToResponse(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getDesignation(),
                e.getPhone(),
                e.getManager() != null ? e.getManager().getName() : null,
                e.getDepartment() != null ? e.getDepartment().getName() : null,
                e.getCreatedDate()
        );
    }

    public PaginatedResponse<EmployeeResponse> getAllEmployees(Integer manager, Integer department, String name, Integer page, Integer limit) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (limit != null && limit > 0) ? limit : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Employee> spec = Specification
                .where(EmployeeSpecifications.hasManagerId(manager))
                .and(EmployeeSpecifications.hasDepartmentId(department))
                .and(EmployeeSpecifications.hasName(name));

        Page<Employee> resultPage = employeeRepository.findAll(spec, pageable);

        List<EmployeeResponse> employeeResponses = resultPage.getContent().stream()
                .map(this::convertToResponse)
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
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployeeById(int id) {
        return employeeRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public List<EmployeeResponse> getBulkEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse addEmployee(Employee employee) {
        Employee e = employeeRepository.save(employee);
        return convertToResponse(e);
    }

    @Transactional
    public String addEmployees(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                return "file not found";
            }

            List<Employee> employees = new ArrayList<>();

            if (fileName.endsWith(".csv")) {
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
                    String managerIdStr = record.get("managerId");
                    if (managerIdStr != null && !managerIdStr.isEmpty()) {
                        e.setManagerId(Integer.parseInt(managerIdStr));
                    } else {
                        e.setManagerId(null);
                    }
                    String departmentIdStr = record.get("departmentId");
                    if (departmentIdStr != null && !departmentIdStr.isEmpty()) {
                        e.setDepartmentId(Integer.parseInt(departmentIdStr));
                    } else {
                        e.setDepartmentId(null);
                    }
                    String createdDateStr = null;
                    try {
                        createdDateStr = record.get("createdDate");
                    } catch (Exception ignored) {}

                    if (createdDateStr != null && !createdDateStr.isEmpty()) {
                        e.setCreatedDate(LocalDate.parse(createdDateStr));
                    } else {
                        e.setCreatedDate(null);
                    }

                    employees.add(e);
                }
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                Workbook workBook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workBook.getSheetAt(0);

                boolean isHeader = true;

                for (Row row : sheet) {
                    if (isHeader) {
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
                                    Utils.getCellValue(row.getCell(5)),
                                    Utils.getCellValue(row.getCell(6))
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

    public EmployeeResponse updateEmployee(int id, Employee employee) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setDesignation(employee.getDesignation());
        existing.setPhone(employee.getPhone());
        existing.setManagerId(employee.getManagerId());
        existing.setDepartmentId(employee.getDepartmentId());

        Employee updatedEmployee = employeeRepository.save(existing);
        return convertToResponse(updatedEmployee);
    }

    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }

    public OverviewResponse getOverview(String segment) {
        double m = Utils.multiplierFor(segment);
        int total = (int) Math.round(500 * m);
        int active = (int) Math.round(total * 0.7);
        int disabled = (int) Math.round(total * 0.15);
        int incomplete = total - active - disabled;
        return new OverviewResponse(total, active, disabled, Math.max(incomplete, 0));
    }

    public FunnelResponse getFunnel(String segment, LocalDate from, LocalDate to) {
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from == null ? end.minusMonths(6) : from;

        List<FunnelStage> stages = new ArrayList<>();
        double base = 600 * Utils.multiplierFor(segment);

        int signup = (int) Math.round(base);
        int s1 = (int) Math.round(signup * 0.85);
        int s2 = (int) Math.round(s1 * 0.73);
        int s3 = (int) Math.round(s2 * 0.65);
        int s4 = (int) Math.round(s3 * 0.55);
        int s5 = (int) Math.round(s4 * 0.45);
        int activation = (int) Math.round(s5 * 0.9);

        stages.add(new FunnelStage("Signup", signup));
        stages.add(new FunnelStage("Stage 1", s1));
        stages.add(new FunnelStage("Stage 2", s2));
        stages.add(new FunnelStage("Stage 3", s3));
        stages.add(new FunnelStage("Stage 4", s4));
        stages.add(new FunnelStage("Stage 5", s5));
        stages.add(new FunnelStage("Activation", activation));

        List<TimeseriesPoint> daily = Utils.generateTimeseries(start, end, "Signup", base);

        return new FunnelResponse(stages, daily);
    }

    public OnboardingTimeResponse getOnboardingTimeStats(String segment, LocalDate from, LocalDate to) {
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from == null ? end.minusMonths(6) : from;

        double avgDays = 3.0 + rnd.nextDouble() * 4.0;

        int total = 600;
        int oneDay = (int) Math.round(total * 0.25);
        int threeDays = (int) Math.round(total * 0.35);
        int oneWeek = (int) Math.round(total * 0.25);
        int aboveWeek = total - oneDay - threeDays - oneWeek;

        Map<String, Integer> buckets = new LinkedHashMap<>();
        buckets.put("1_day", oneDay);
        buckets.put("3_days", threeDays);
        buckets.put("1_week", oneWeek);
        buckets.put("above_1_week", aboveWeek);

        int notActivated = (int)Math.round(total * 0.12);

        return new OnboardingTimeResponse(avgDays, buckets, notActivated);
    }

    public EngagementResponse getEngagementTimeseries(String metric, String granularity, String segment, LocalDate from, LocalDate to) {
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from == null ? end.minusMonths(6) : from;

        List<TimeseriesPoint> series;
        switch (granularity.toLowerCase()) {
            case "weekly":
                series = Utils.generateWeeklyTimeseries(start, end, metric, Utils.multiplierFor(segment));
                break;
            case "monthly":
                series = Utils.generateMonthlyTimeseries(start, end, metric, Utils.multiplierFor(segment));
                break;
            default:
                series = Utils.generateDailyTimeseries(start, end, metric, Utils.multiplierFor(segment));
        }

        double avg = series.stream().mapToDouble(TimeseriesPoint::getValue).average().orElse(0);

        return new EngagementResponse(metric, granularity, segment, (int)Math.round(avg), series);
    }
}
