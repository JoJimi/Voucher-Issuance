package org.example.voucherissuance.domain.service.validator;

import org.example.voucherissuance.common.dto.RequestContext;
import org.example.voucherissuance.entity.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherDisableValidator {
    public void validate(VoucherEntity voucherEntity, RequestContext requestContext) {
        상품권_사용_불가_처리_권한이_있는지_확인(voucherEntity, requestContext);
    }
    // 메서드 이름을 한글로 쓰는 것을 추천 (Validator 안에서만)
    private static void 상품권_사용_불가_처리_권한이_있는지_확인(VoucherEntity voucherEntity, RequestContext requestContext) {
        if(voucherEntity.publishHistory().getRequesterType() != requestContext.requesterType()
                || !voucherEntity.publishHistory().getRequesterId().equals(requestContext.requesterId())){
            throw new IllegalArgumentException("사용 불가 처리 권한이 없는 상품권입니다.");
        }
    }

}
