package wheresoever.quickprotoserver.domain.member.exception;


import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

public class MemberLoginFailException extends InvalidValueException {

    public MemberLoginFailException() {
        super(ErrorInfo.MEMBER_LOGIN_FAIL, ErrorMessageMapper.MEMBER_LOGIN_FAIL.getValue());
    }

    public MemberLoginFailException(String message) {
        super(ErrorInfo.MEMBER_LOGIN_FAIL, message);
    }
}
