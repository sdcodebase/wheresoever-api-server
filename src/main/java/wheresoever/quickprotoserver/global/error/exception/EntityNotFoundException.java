package wheresoever.quickprotoserver.global.error.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super(ErrorInfo.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        super(ErrorInfo.ENTITY_NOT_FOUND, message);
    }

    public EntityNotFoundException(ErrorInfo errorInfo, String message) {
        super(errorInfo, message);
    }
}
