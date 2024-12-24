package org.example.voucherissuance.controller.voucher.request;

import org.example.voucherissuance.common.type.RequesterType;

public record VoucherDisableV2Request(
        RequesterType requesterType,
        String requesterId,
        String code
) {
}
