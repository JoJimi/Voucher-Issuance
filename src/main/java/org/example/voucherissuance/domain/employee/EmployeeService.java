package org.example.voucherissuance.domain.employee;

import org.example.voucherissuance.controller.request.EmployeeCreateRequest;
import org.example.voucherissuance.controller.response.EmployeeResponse;
import org.example.voucherissuance.entity.EmployeeEntity;
import org.example.voucherissuance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // 사원 생성
    public Long create(final String name, final String position, final String department) {
        final EmployeeEntity employeeEntity = employeeRepository.save(new EmployeeEntity(name, position, department));

        return employeeEntity.getId();
    }

    // 사원 조회
    public EmployeeResponse get(final Long no) {
        EmployeeEntity employeeEntity = employeeRepository.findById(no)
                .orElseThrow(() -> new IllegalArgumentException("존제하지 않는 회원입니다."));

        return new EmployeeResponse(employeeEntity.getId(), employeeEntity.getName(), employeeEntity.getPosition(), employeeEntity.getDepartment());
    }

}
