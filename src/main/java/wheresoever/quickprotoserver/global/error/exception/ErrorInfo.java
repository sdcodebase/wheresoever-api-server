package wheresoever.quickprotoserver.global.error.exception;

import lombok.Getter;
import wheresoever.quickprotoserver.global.constant.ErrorMessageMapper;

@Getter
public enum ErrorInfo {

    // Common Error
    BAD_REQUEST(400, "COMMON_001", ErrorMessageMapper.ERROR_BAD_REQUEST.getValue()),
    RESOURCE_NOT_FOUND(404, "COMMON_002", ErrorMessageMapper.ERROR_NOT_FOUND.getValue()),
    INTERNAL_SERVER_ERROR(500, "COMMON_003", ErrorMessageMapper.ERROR_INTERNAL_SERVER_ERROR.getValue()),

    ENTITY_NOT_FOUND(500, "COMMON_010", ErrorMessageMapper.ENTITY_NOT_FOUND.getValue()),
    SESSION_NOT_FOUND(400, "COMMON_011"),

    INVALID_INPUT_VALUE(400, "COMMON_012", ErrorMessageMapper.INVALID_INPUT_VALUE.getValue()),

    // Member Exception
    MEMBER_NOT_FOUND(500, "MEMBER_001"),
    MEMBER_LOGIN_FAIL(200, "MEMBER_002"),
    MEMBER_EMAIL_PREVIOUS_EXISTS(200, "MEMBER_003"),

    // RandomMessage Exception
    PREV_SENT_MESSAGE(200, "RM_001"),

    ;

    private final int statusCode;
    private final String code;
    private String message;

    ErrorInfo(final int statusCode, final String code, final String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
    }

    ErrorInfo(final int statusCode, final String code) {
        this.statusCode = statusCode;
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}