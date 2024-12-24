package org.example.voucherissuance.common.dto;

import org.example.voucherissuance.common.type.RequesterType;

public record RequestContext(
        RequesterType requesterType,
        String requesterId
) {
}
