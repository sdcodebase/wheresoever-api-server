package wheresoever.quickprotoserver.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int statusCode;
    private String code;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private ErrorResponse(ErrorInfo errorInfo) {
        this.message = errorInfo.getMessage();
        this.statusCode = errorInfo.getStatusCode();
        this.code = errorInfo.getCode();
    }

    private ErrorResponse(ErrorInfo errorInfo, String message) {
        this.message = message;
        this.statusCode = errorInfo.getStatusCode();
        this.code = errorInfo.getCode();
    }

    public static ErrorResponse of(ErrorInfo error) {
        return new ErrorResponse(error);
    }

    public static ErrorResponse of(ErrorInfo error, String message) {
        return new ErrorResponse(error, message);
    }

}
