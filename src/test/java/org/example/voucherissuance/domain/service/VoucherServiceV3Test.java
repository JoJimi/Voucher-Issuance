package org.example.voucherissuance.domain.service;

import org.example.voucherissuance.common.dto.RequestContext;
import org.example.voucherissuance.common.type.RequesterType;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.voucher.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoucherServiceV3Test {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;


    @Autowired
    private ContractRepository contractRepository;

    @DisplayName("발행한 상품권은 계약정보의 voucherValidPeriodDayCount 만큼 유효기간을 가져야 된다.")
    @Test
    public void test1(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String contractCode = "CT001";

        // when
        final String code = voucherService.publishV3(requestContext, contractCode, amount);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        final ContractEntity contractEntity = contractRepository.findByCode(contractCode).get();

        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(LocalDate.now());
        assertThat(voucherEntity.getValidTo()).isEqualTo(LocalDate.now().plusDays(contractEntity.getVoucherValidPeriodDayCount()));
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
    public void test2(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        final RequestContext disableRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

        // when
        voucherService.disableVoucherV2(disableRequestContext, code);
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
    public void test3(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        final RequestContext useRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

        // when
        voucherService.useVoucherV1(useRequestContext, code);
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