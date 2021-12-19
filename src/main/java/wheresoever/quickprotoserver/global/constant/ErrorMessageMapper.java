package wheresoever.quickprotoserver.global.constant;

public enum ErrorMessageMapper {

    ERROR_BAD_REQUEST("Bad Request"),
    ERROR_NOT_FOUND("Not Found"),
    ERROR_INTERNAL_SERVER_ERROR("예상치 못한 오류가 발생했습니다. 다시 시도 해주세요."),


    ENTITY_NOT_FOUND("데이터를 찾을 수 없습니다."),
    SESSION_NOT_FOUND("세션을 찾을 수 없습니다."),

    INVALID_INPUT_VALUE("Invalid input"),

    MEMBER_NOT_FOUND("해당 계정의 정보가 없습니다."),
    MEMBER_LOGIN_FAIL("로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요."),
    MEMBER_EMAIL_PREVIOUS_EXISTS("해당 이메일은 이미 존재하는 계정입니다."),

    PREV_SENT_MESSAGE("해당 멤버에게 메시지를 보낸 이력이 있습니다."),

    PREV_FOLLOWED("이미 팔로우한 멤버입니다"),

    PREV_POST_LIKED("이미 좋아요한 게시글입니다"),

    ;

    private final String message;

    ErrorMessageMapper(final String message) {
        this.message = message;
    }

    public String getValue() {
        return this.message;
    }

}
