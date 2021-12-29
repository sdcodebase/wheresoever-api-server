package wheresoever.quickprotoserver.domain.comment.exception;

import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;
import wheresoever.quickprotoserver.global.error.exception.EntityNotFoundException;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException() {
        super(ErrorInfo.ENTITY_NOT_FOUND, ErrorMessageMapper.COMMENT_NOT_FOUND.getValue());
    }
}
