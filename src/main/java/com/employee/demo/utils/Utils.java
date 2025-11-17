package com.employee.demo.utils;

import com.employee.demo.entity.Employee;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;

public class Utils {

    public static Employee convertRecordToEmployee(String name, String email, String designation,
                                                   String phone, String managerIdStr, String departmentIdStr) {

        Employee e = new Employee();
        e.setName(name);
        e.setEmail(email);
        e.setDesignation(designation);
        e.setPhone(phone);

        // managerId
        if (managerIdStr != null && !managerIdStr.isEmpty()) {
            e.setManagerId(Integer.parseInt(managerIdStr));
        } else {
            e.setManagerId(null);
        }

        // departmentId
        if (departmentIdStr != null && !departmentIdStr.isEmpty()) {
            e.setDepartmentId(Integer.parseInt(departmentIdStr));
        } else {
            e.setDepartmentId(null);
        }

        e.setCreatedDate(LocalDate.now());
        return e;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }


}
