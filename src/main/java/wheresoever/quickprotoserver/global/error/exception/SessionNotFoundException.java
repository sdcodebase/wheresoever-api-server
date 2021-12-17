package wheresoever.quickprotoserver.global.error.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;

public class SessionNotFoundException extends InvalidValueException {

    public SessionNotFoundException() {
        super(ErrorInfo.SESSION_NOT_FOUND, ErrorMessageMapper.SESSION_NOT_FOUND.getValue());
    }
}
