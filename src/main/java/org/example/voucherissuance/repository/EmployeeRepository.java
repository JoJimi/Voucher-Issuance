package org.example.voucherissuance.repository;

import org.example.voucherissuance.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository <EmployeeEntity, Long> {

}
