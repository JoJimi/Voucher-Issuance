package org.example.voucherissuance.entity.voucher;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.example.voucherissuance.common.type.RequesterType;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.BaseEntity;

import java.time.LocalDate;

@Table(name="voucher_history")
@Entity
public class VoucherHistoryEntity extends BaseEntity {

    private String orderId;
    @Enumerated(EnumType.STRING)
    private RequesterType requesterType;
    private String requesterId;
    @Enumerated(EnumType.STRING)
    private VoucherStatusType status;
    private String description;
    public VoucherHistoryEntity() {
    }

    public VoucherHistoryEntity(String orderId, RequesterType requesterType, String requesterId, VoucherStatusType status, String description) {
        this.orderId = orderId;
        this.requesterType = requesterType;
        this.requesterId = requesterId;
        this.status = status;
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public RequesterType getRequesterType() {
        return requesterType;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public VoucherStatusType getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }


}
