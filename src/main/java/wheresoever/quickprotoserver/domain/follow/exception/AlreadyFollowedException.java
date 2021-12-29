package wheresoever.quickprotoserver.domain.follow.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

public class AlreadyFollowedException extends InvalidValueException {
    public AlreadyFollowedException() {
        super(ErrorInfo.PREV_FOLLOWED, ErrorMessageMapper.PREV_FOLLOWED.getValue());
    }
}
