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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VoucherServiceV3Test {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;


    @Autowired
    private ContractRepository contractRepository;

    @DisplayName("유효기간이 지난 계약으로 상품권 발행을 할 수 없습니다.")
    @Test
    public void test0(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String contractCode = "CT010";

        // when
        assertThatThrownBy(() -> voucherService.publishV3(requestContext, contractCode, amount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유효기간이 지난 계약입니다.");

    }

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

    @DisplayName("상품권은 발행 요청자만 사용 불가 처리를 할 수 있습니다.")
    @Test
    public void test2(){
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String contractCode = "CT001";
        final String code = voucherService.publishV3(requestContext, contractCode, amount);

        final RequestContext otherRequestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());

        // when
        assertThatThrownBy(() -> voucherService.disableVoucherV3(requestContext, code))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 불가 처리 권한이 없는 상품권입니다.");

        // then
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);

    }

}