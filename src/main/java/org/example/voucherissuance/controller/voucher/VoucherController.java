package org.example.voucherissuance.controller.voucher;


import org.example.voucherissuance.controller.voucher.request.VoucherDisableV2Request;
import org.example.voucherissuance.controller.voucher.request.VoucherPublishRequest;
import org.example.voucherissuance.controller.voucher.request.VoucherPublishV2Request;
import org.example.voucherissuance.controller.voucher.request.VoucherUseV2Request;
import org.example.voucherissuance.controller.voucher.response.VoucherDisableV2Response;
import org.example.voucherissuance.controller.voucher.response.VoucherPublishResponse;
import org.example.voucherissuance.controller.voucher.response.VoucherPublishV2Response;
import org.example.voucherissuance.controller.voucher.response.VoucherUseV2Response;
import org.example.voucherissuance.domain.service.VoucherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
public class VoucherController {
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // 상품권 발행
    @PostMapping("/api/v1/voucher")
    public VoucherPublishResponse publish(@RequestBody final VoucherPublishRequest request) {
        final String publishedVoucherCode = voucherService.publish(LocalDate.now(), LocalDate.now().plusDays(1830L), request.amountType());
        return new VoucherPublishResponse(publishedVoucherCode);
    }

    // 상품권 사용
    @PutMapping("/api/v1/voucher/use")
    public void use(@RequestBody final String code) {
        voucherService.useVoucher(code);
    }

    // 상품권 폐기
    @PutMapping("/api/v1/voucher/disable")
    public void disable(@RequestBody final String code) {
        voucherService.disableVoucher(code);
    }

    // 상품권 발행
    @PostMapping("/api/v2/voucher")
    public VoucherPublishV2Response publishV2(@RequestBody final VoucherPublishV2Request request) {
        final String publishedVoucherCode = voucherService.publishV2(
                request.requesterType(),
                request.requesterId(),
                LocalDate.now(), LocalDate.now().plusDays(1830L), request.amountType());
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        return new VoucherPublishV2Response(orderId, publishedVoucherCode);
    }

    // 상품권 사용
    @PutMapping("/api/v2/voucher/use")
    public VoucherUseV2Response useV2(@RequestBody final VoucherUseV2Request request) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        voucherService.useVoucherV2(request.requesterType(), request.requesterId(), request.code());

        return new VoucherUseV2Response(orderId);
    }

    // 상품권 폐기
    @PutMapping("/api/v2/voucher/disable")
    public VoucherDisableV2Response disableV2(@RequestBody final VoucherUseV2Request request) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        voucherService.disableVoucherV2(request.requesterType(), request.requesterId(), request.code());

        return new VoucherDisableV2Response(orderId);
    }
}
