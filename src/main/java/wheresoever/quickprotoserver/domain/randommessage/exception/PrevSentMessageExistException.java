package wheresoever.quickprotoserver.domain.randommessage.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

public class PrevSentMessageExistException extends InvalidValueException {
    public PrevSentMessageExistException() {
        super(ErrorInfo.PREV_SENT_MESSAGE, ErrorMessageMapper.PREV_SENT_MESSAGE.getValue());
    }
}
