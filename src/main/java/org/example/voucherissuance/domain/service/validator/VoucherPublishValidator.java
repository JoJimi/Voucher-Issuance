package org.example.voucherissuance.domain.service.validator;

import org.example.voucherissuance.entity.voucher.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherPublishValidator {
    public void validate(ContractEntity contractEntity) {
        상품권_발행을_위한_계약_유효기간이_확인되었는지_확인(contractEntity);
    }

    // 메서드 이름을 한글로 쓰는 것을 추천 (Validator 안에서만)
    private static void 상품권_발행을_위한_계약_유효기간이_확인되었는지_확인(ContractEntity contractEntity) {
        if (contractEntity.isExpired()) {
            throw new IllegalStateException("유효기간이 지난 계약입니다.");
        }
    }
}
