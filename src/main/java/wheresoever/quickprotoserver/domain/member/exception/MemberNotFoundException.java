package wheresoever.quickprotoserver.domain.member.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.EntityNotFoundException;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException() {
        super(ErrorInfo.MEMBER_NOT_FOUND, ErrorMessageMapper.MEMBER_NOT_FOUND.getValue());
    }

    public MemberNotFoundException(String message) {
        super(ErrorInfo.MEMBER_NOT_FOUND, message);
    }
}
