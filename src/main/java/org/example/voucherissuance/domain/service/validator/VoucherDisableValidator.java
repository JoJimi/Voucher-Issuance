package org.example.voucherissuance.domain.service.validator;

import org.example.voucherissuance.common.dto.RequestContext;
import org.example.voucherissuance.entity.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherDisableValidator {
    public void validate(VoucherEntity voucherEntity, RequestContext requestContext) {
        if(voucherEntity.publishHistory().getRequesterType() != requestContext.requesterType()
                || !voucherEntity.publishHistory().getRequesterId().equals(requestContext.requesterId())){
            throw new IllegalArgumentException("사용 불가 처리 권한이 없는 상품권입니다.");
        }
    }

}
