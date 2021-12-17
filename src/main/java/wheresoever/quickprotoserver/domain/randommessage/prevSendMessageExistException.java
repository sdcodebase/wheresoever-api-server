package wheresoever.quickprotoserver.domain.randommessage;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

public class prevSendMessageExistException extends InvalidValueException {
    public prevSendMessageExistException() {
        super(ErrorInfo.PREV_SENT_MESSAGE, ErrorMessageMapper.PREV_SENT_MESSAGE.getValue());
    }
}
