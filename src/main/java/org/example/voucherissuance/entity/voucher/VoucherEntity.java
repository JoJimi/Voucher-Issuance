package org.example.voucherissuance.entity.voucher;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name="voucher")
@Entity
public class VoucherEntity extends BaseEntity {

    private String code;                    // 상품권 코드
    private VoucherStatusType status;       // 상품권 상태
    private LocalDate validFrom;            // 상품권 유효기간 시작일
    private LocalDate validTo;              // 상품권 유효기간 종료일
    private Long amount;                    // 상품권 금액

    public VoucherEntity() {
    }
    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, Long amount) {
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public VoucherStatusType getStatus() {
        return status;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public Long getAmount() {
        return amount;
    }
}
