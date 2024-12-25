package org.example.voucherissuance.common.exception;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp,
        UUID traceId
) {
}
