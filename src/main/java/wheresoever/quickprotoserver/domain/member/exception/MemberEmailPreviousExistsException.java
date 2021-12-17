package wheresoever.quickprotoserver.domain.member.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

public class MemberEmailPreviousExistsException extends InvalidValueException {
    public MemberEmailPreviousExistsException() {
        super(ErrorInfo.MEMBER_EMAIL_PREVIOUS_EXISTS, ErrorMessageMapper.MEMBER_EMAIL_PREVIOUS_EXISTS.getValue());
    }
}
