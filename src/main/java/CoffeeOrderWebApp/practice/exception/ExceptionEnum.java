package CoffeeOrderWebApp.practice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입한 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원가입 정보가 없습니다."),
    COFFEE_EXISTS(HttpStatus.BAD_REQUEST, "이미 입력된 커피입니다."),
    COFFEE_NOT_FOUND(HttpStatus.NOT_FOUND, "커피 정보가 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보가 없습니다."),
    ORDER_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "주문이 확정되어 취소가 어렵습니다."),
    ORDER_CANNOT_CHANGE(HttpStatus.BAD_REQUEST, "주문이 확정되어 변경이 어렵습니다."),
    QUESTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "문의 내역이 없습니다."),
    QUESTION_DELETED(HttpStatus.NO_CONTENT, "삭제된 문의입니다."),
    QUESTION_ANSWERED(HttpStatus.BAD_REQUEST, "이미 답변이 작성된 문의입니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "작성된 답변이 없습니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 [좋아요]를 눌렀습니다."),
    THERE_IS_NO_LIKE(HttpStatus.BAD_REQUEST, "취소할 [좋아요]가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
