package wheresoever.quickprotoserver.domain.post.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.EntityNotFoundException;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

public class PostNotFoundException extends EntityNotFoundException {
    public PostNotFoundException() {
        super(ErrorInfo.POST_NOT_FOUND, ErrorMessageMapper.POST_NOT_FOUND.getValue());
    }
}
