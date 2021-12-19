package wheresoever.quickprotoserver.domain.postlike.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

public class AlreadyLikedPostException extends InvalidValueException {
    public AlreadyLikedPostException() {
        super(ErrorInfo.PREV_POST_LIKED, ErrorMessageMapper.PREV_SENT_MESSAGE.getValue());
    }
}
