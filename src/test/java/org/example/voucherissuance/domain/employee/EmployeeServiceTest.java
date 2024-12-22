package org.example.voucherissuance.domain.employee;

import org.example.voucherissuance.controller.response.EmployeeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;

    @DisplayName("회원 생성 후 조회가 가능하다.")
    @Test
    public void 테스트1(){
        // given
        String name = "홍길동";
        String position = "사원";
        String department = "개발팀";

        // when
        Long no = employeeService.create(name, position, department);
        EmployeeResponse response = employeeService.get(no);

        // then
        assertThat(response).isNotNull();
        assertThat(response.no()).isEqualTo(no);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.position()).isEqualTo(position);
        assertThat(response.department()).isEqualTo(department);

        System.out.println(response);
    }
}