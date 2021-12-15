package wheresoever.quickprotoserver.global.error.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorInfo errorInfo;

    public BusinessException() {
        super(ErrorInfo.INTERNAL_SERVER_ERROR.getMessage());
        this.errorInfo = ErrorInfo.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
    }

    public BusinessException(ErrorInfo errorInfo, String message) {
        super(message);
        this.errorInfo = errorInfo;
        errorInfo.setMessage(message);
    }
}
