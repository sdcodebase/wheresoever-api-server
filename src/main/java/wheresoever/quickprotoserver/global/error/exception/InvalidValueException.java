package wheresoever.quickprotoserver.global.error.exception;

public class InvalidValueException extends BusinessException {

    public InvalidValueException() {
        super(ErrorInfo.INTERNAL_SERVER_ERROR);
    }

    public InvalidValueException(ErrorInfo errorInfo, String message) {
        super(errorInfo, message);
    }
}