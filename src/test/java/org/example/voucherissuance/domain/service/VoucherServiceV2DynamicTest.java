package org.example.voucherissuance.domain.service;

import org.example.voucherissuance.common.dto.RequestContext;
import org.example.voucherissuance.common.type.RequesterType;
import org.example.voucherissuance.common.type.VoucherAmountType;
import org.example.voucherissuance.common.type.VoucherStatusType;
import org.example.voucherissuance.entity.voucher.VoucherEntity;
import org.example.voucherissuance.entity.voucher.VoucherRepository;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
public class VoucherServiceV2DynamicTest {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    // 단위 테스트가 아닌 일련 과정의 테스트
    // Dynamic Test 시나리오를 작성
    @TestFactory
    Stream<DynamicTest> test(){
        final List<String> codes = new ArrayList<>();

        return Stream.of(
                dynamicTest("[0] 상품권을 발행합니다.", ()->{
                    //given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    final LocalDate validFrom = LocalDate.now();
                    final LocalDate validTo = LocalDate.now().plusDays(30);
                    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

                    //when
                    final String code = voucherService.publishV1(requestContext, validFrom, validTo, amount);
                    codes.add(code);

                    //then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);

                }),
                dynamicTest("[0] 상품권을 사용 불가 처리합니다.", ()->{
                    //given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    final String code = codes.get(0);

                    //when
                    voucherService.disableVoucherV2(requestContext, code);

                    //then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[0] 사용 불가 상태의 상품권은 사용할 수 없습니다.", ()->{
                    //given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    final String code = codes.get(0);

                    //when
                    assertThatThrownBy(() -> voucherService.useVoucherV1(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권입니다.");

                    //then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                }),

                dynamicTest("[1] 상품권을 사용합니다.", ()->{
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    final LocalDate validFrom = LocalDate.now();
                    final LocalDate validTo = LocalDate.now().plusDays(30);
                    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

                    final String code = voucherService.publishV1(requestContext, validFrom, validTo, amount);
                    codes.add(code);

                    //when
                    voucherService.useVoucherV1(requestContext, code);

                    //then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[1] 사용한 상품권은 사용 불가 처리 할 수 없습니다.", ()->{
                    //given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    final String code = codes.get(1);

                    //when
                    assertThatThrownBy(()->voucherService.disableVoucherV2(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용 불가 처리할 수 없는 상태의 상품권입니다.");

                    //then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[1] 사용한 상품권은 또 사용할 수 없습니다.", ()->{
                    //given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    final String code = codes.get(0);

                    //when
                    assertThatThrownBy(()->voucherService.useVoucherV1(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권입니다.");

                    //then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                })
        );
    }

}
