package org.example.voucherissuance.config;

import jakarta.annotation.PostConstruct;
import org.example.voucherissuance.entity.voucher.ContractEntity;
import org.example.voucherissuance.entity.voucher.ContractRepository;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LocalDataInit {
    private final ContractRepository contractRepository;

    public LocalDataInit(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @PostConstruct
    public void init(){
        // 계약 데이터 초기화
        contractRepository.save(new ContractEntity("CT001", LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), 366*5));

    }
}
