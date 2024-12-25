package org.example.voucherissuance.domain.service;

import org.example.voucherissuance.common.dto.RequestContext;
import org.example.voucherissuance.common.type.RequesterType;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.voucher.VoucherEntity;
import org.example.voucherissuance.entity.voucher.VoucherHistoryEntity;
import org.example.voucherissuance.entity.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoucherServiceV2Test {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 code로 조회할 수 있어야 한다.")
    @Test
    public void 상품권_발행(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String code = voucherService.publishV1(requestContext, validFrom, validTo, amount);

        // when
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(0);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(requestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(requestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 발행");

    }

    @DisplayName("발행된 상품권은 사용 불가 처리할 수 있다.")
    @Test
    public void 상품권_사용불가(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String code = voucherService.publishV1(requestContext, validFrom, validTo, amount);

        final RequestContext disableRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

        // when
        voucherService.disableVoucherV2(requestContext, code);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
        assertThat(voucherEntity.getUpdateAt()).isNotEqualTo(voucherEntity.getCreateAt());

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(voucherEntity.getHistories().size()-1);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(disableRequestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(disableRequestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 사용 불가");

    }

    @DisplayName("발행된 상품권은 사용할 수 있다.")
    @Test
    public void 상품권_사용(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String code = voucherService.publishV1(requestContext, validFrom, validTo, amount);

        final RequestContext useRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

        // when
        voucherService.useVoucherV1(requestContext, code);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
        assertThat(voucherEntity.getUpdateAt()).isNotEqualTo(voucherEntity.getCreateAt());

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(voucherEntity.getHistories().size()-1);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(useRequestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(useRequestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 사용");

    }


}