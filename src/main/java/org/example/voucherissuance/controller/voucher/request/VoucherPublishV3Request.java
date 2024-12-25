package org.example.voucherissuance.controller.voucher.request;

import org.example.voucherissuance.common.type.RequesterType;
import org.example.voucherissuance.common.type.VoucherAmountType;

public record VoucherPublishV3Request(
        RequesterType requesterType,
        String requesterId,
        String contractCode,
        VoucherAmountType amountType
) {
}
