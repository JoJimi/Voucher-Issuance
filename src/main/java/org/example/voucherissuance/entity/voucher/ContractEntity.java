package org.example.voucherissuance.entity.voucher;

import jakarta.persistence.*;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name="contract")
@Entity
public class ContractEntity extends BaseEntity {

    private String code;                            // 계약의 고유 코드
    private LocalDate validFrom;                    // 계약의 유효기간 시작일
    private LocalDate validTo;                      // 계약의 유효기간 종료일
    private Integer voucherValidPeriodDayCount;     // 상품권 유효기간 일자


    public ContractEntity() {
    }

    public ContractEntity(String code, LocalDate validFrom, LocalDate validTo, Integer voucherValidPeriodDayCount) {
        this.code = code;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.voucherValidPeriodDayCount = voucherValidPeriodDayCount;
    }

    public Boolean isExpired(){
        return LocalDate.now().isAfter(validTo);
    }

    public String getCode() {
        return code;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public Integer getVoucherValidPeriodDayCount() {
        return voucherValidPeriodDayCount;
    }
}
