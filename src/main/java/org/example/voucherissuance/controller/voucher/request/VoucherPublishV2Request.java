package org.example.voucherissuance.controller.voucher.request;

import org.example.voucherissuance.common.type.RequesterType;
import org.example.voucherissuance.common.type.VoucherAmountType;

public record VoucherPublishV2Request(
        RequesterType requesterType,
        String requesterId,
        VoucherAmountType amountType
) {
}
