package com.employee.demo.utils;

import com.employee.demo.dto.TimeseriesPoint;
import com.employee.demo.entity.Employee;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    private final static Random rnd = new Random(1234);

    public static Employee convertRecordToEmployee(String name, String email, String designation,
                                                   String phone, String managerIdStr, String departmentIdStr,
                                                    String createdDateStr) {

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

        if(createdDateStr != null && !createdDateStr.isEmpty()){
            e.setCreatedDate(LocalDate.parse(createdDateStr));
        } else {
            e.setCreatedDate(null);
        }

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

    public static double multiplierFor(String segment) {
        if (segment == null) return 1.0;
        switch (segment.toLowerCase()) {
            case "all": return 1.0;
            case "online": return 0.7;
            case "enterprise": return 1.3;
            case "both": return 1.0;
            default: return 1.0;
        }
    }

    public static List<TimeseriesPoint> generateTimeseries(LocalDate start, LocalDate end, String label, double base) {
        long days = start.until(end).getDays();
        List<TimeseriesPoint> list = new ArrayList<>();
        LocalDate cur = start;
        while (!cur.isAfter(end)) {
            int v = (int)Math.round(Math.max(0, base * (0.4 + rnd.nextDouble() * 0.8)));
            list.add(new TimeseriesPoint(cur.toString(), v));
            cur = cur.plusDays(1);
        }
        return list;
    }

    public static List<TimeseriesPoint> generateDailyTimeseries(LocalDate start, LocalDate end, String metric, double mult) {
        return generateTimeseries(start, end, metric, 80 * mult);
    }

    public static List<TimeseriesPoint> generateWeeklyTimeseries(LocalDate start, LocalDate end, String metric, double mult) {
        List<TimeseriesPoint> weekly = new ArrayList<>();
        LocalDate cur = start.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        while (!cur.isAfter(end)) {
            LocalDate weekEnd = cur.plusDays(6);
            int val = 0;
            // sum of days in week
            for (LocalDate d = cur; !d.isAfter(weekEnd) && !d.isAfter(end); d = d.plusDays(1)) {
                val += (int)Math.round(20 * mult * (0.5 + rnd.nextDouble()));
            }
            weekly.add(new TimeseriesPoint(cur.toString(), val));
            cur = cur.plusWeeks(1);
        }
        return weekly;
    }

    public static List<TimeseriesPoint> generateMonthlyTimeseries(LocalDate start, LocalDate end, String metric, double mult) {
        List<TimeseriesPoint> months = new ArrayList<>();
        LocalDate cur = start.with(TemporalAdjusters.firstDayOfMonth());
        while (!cur.isAfter(end)) {
            int val = 0;
            LocalDate monthEnd = cur.with(TemporalAdjusters.lastDayOfMonth());
            for (LocalDate d = cur; !d.isAfter(monthEnd) && !d.isAfter(end); d = d.plusDays(1)) {
                val += (int)Math.round(30 * mult * (0.5 + rnd.nextDouble()));
            }
            months.add(new TimeseriesPoint(cur.getYear() + "-" + String.format("%02d", cur.getMonthValue()), val));
            cur = cur.plusMonths(1);
        }
        return months;
    }
}
