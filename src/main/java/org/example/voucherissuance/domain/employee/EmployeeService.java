package org.example.voucherissuance.domain.employee;

import org.example.voucherissuance.controller.employee.response.EmployeeResponse;
import org.example.voucherissuance.entity.employee.EmployeeEntity;
import org.example.voucherissuance.entity.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

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

        return new EmployeeResponse(employeeEntity.getId(), employeeEntity.getName(), employeeEntity.getPosition(), employeeEntity.getDepartment(),
                employeeEntity.getCreateAt(), employeeEntity.getUpdateAt());
    }

}
