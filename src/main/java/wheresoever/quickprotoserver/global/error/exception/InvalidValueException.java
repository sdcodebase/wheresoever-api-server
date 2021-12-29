package wheresoever.quickprotoserver.global.error.exception;

public class InvalidValueException extends BusinessException {

    public InvalidValueException() {
        super(ErrorInfo.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String message) {
        super(ErrorInfo.INVALID_INPUT_VALUE, message);
    }

    public InvalidValueException(ErrorInfo errorInfo, String message) {
        super(errorInfo, message);
    }
}