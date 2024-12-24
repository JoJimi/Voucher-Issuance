package org.example.voucherissuance.domain.service;

import org.example.voucherissuance.common.dto.RequestContext;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.voucher.VoucherEntity;
import org.example.voucherissuance.entity.voucher.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // 상품권 발행
    @Transactional
    public String publishV2(final LocalDate validFrom, final LocalDate validTo, final VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount);

        return voucherRepository.save(voucherEntity).getCode();
    }

    // 상품권 사용불가
    @Transactional
    public void disableVoucherV2(String code) {
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));

        voucherEntity.disable();

    }

    // 상품권 사용
    @Transactional
    public void useVoucherV2(String code) {
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));

        voucherEntity.use();

    }

    // 상품권 발행
    @Transactional
    public String publishV2(final RequestContext requestContext,
                            final LocalDate validFrom, final LocalDate validTo, final VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount);

        return voucherRepository.save(voucherEntity).getCode();
    }

    // 상품권 사용불가
    @Transactional
    public void disableVoucherV2(final RequestContext requestContext, final String code) {
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));

        voucherEntity.disable();

    }

    // 상품권 사용
    @Transactional
    public void useVoucherV2(final RequestContext requestContext, String code) {
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));

        voucherEntity.use();

    }
}
