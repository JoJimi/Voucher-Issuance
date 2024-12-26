package org.example.voucherissuance.entity.voucher;

import jakarta.persistence.*;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name="voucher")
@Entity
public class VoucherEntity extends BaseEntity {

    private String code;
    @Enumerated(EnumType.STRING)// 상품권 코드
    private VoucherStatusType status;       // 상품권 상태
    private LocalDate validFrom;            // 상품권 유효기간 시작일
    private LocalDate validTo;              // 상품권 유효기간 종료일
    @Enumerated(EnumType.STRING)
    private VoucherAmountType amount;       // 상품권 금액

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    private List<VoucherHistoryEntity> histories = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    public VoucherEntity() {
    }
    public VoucherEntity(String code, VoucherStatusType status, VoucherAmountType amount, VoucherHistoryEntity voucherHistoryEntity, ContractEntity contractEntity) {
        this.code = code;
        this.status = status;
        this.validFrom = LocalDate.now();
        this.validTo = LocalDate.now().plusDays(contractEntity.getVoucherValidPeriodDayCount());
        this.amount = amount;

        this.histories.add(voucherHistoryEntity);
        this.contract = contractEntity;
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

    public VoucherAmountType getAmount() {
        return amount;
    }

    public List<VoucherHistoryEntity> getHistories() {
        return histories;
    }

    public VoucherHistoryEntity publishHistory(){
        return histories.stream()
                .filter(voucherHistoryEntity -> voucherHistoryEntity.getStatus().equals(VoucherStatusType.PUBLISH))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("발행 이력이 존재하지 않습니다."));
    }

    public void disable(final VoucherHistoryEntity voucherHistoryEntity) {
        if(!this.status.equals(VoucherStatusType.PUBLISH)){
            throw new IllegalStateException("사용 불가 처리할 수 없는 상태의 상품권입니다.");
        }
        this.status = VoucherStatusType.DISABLE;
        this.histories.add(voucherHistoryEntity);
    }

    public void use(final VoucherHistoryEntity voucherHistoryEntity) {
        if(!this.status.equals(VoucherStatusType.PUBLISH)){
            throw new IllegalStateException("사용할 수 없는 상태의 상품권입니다.");
        }

        this.status = VoucherStatusType.USE;
        this.histories.add(voucherHistoryEntity);
    }
}
