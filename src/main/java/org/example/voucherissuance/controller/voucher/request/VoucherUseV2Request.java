package org.example.voucherissuance.controller.voucher.request;

import org.example.voucherissuance.common.type.RequesterType;

public record VoucherUseV2Request(
        RequesterType requesterType,
        String requesterId,
        String code
) {
}
