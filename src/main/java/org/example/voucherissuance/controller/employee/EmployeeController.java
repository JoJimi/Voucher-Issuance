package org.example.voucherissuance.controller.employee;

import org.example.voucherissuance.controller.employee.request.EmployeeCreateRequest;
import org.example.voucherissuance.controller.employee.response.EmployeeResponse;
import org.example.voucherissuance.domain.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private final Map<Long, EmployeeResponse> employeeResponseMap = new HashMap<>();

    // 사원 생성
    @PostMapping("/api/v1/employee")
    public Long create(@RequestBody final EmployeeCreateRequest request) {
        return employeeService.create(request.name(), request.position(), request.department());
    }

    // 사원 조회
    @GetMapping("/api/v1/employee/{no}")
    public EmployeeResponse get(@PathVariable(value = "no") final Long no) {
        return employeeService.get(no);
    }
}
