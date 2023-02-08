package CoffeeOrderWebApp.practice.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AnswerDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post{
        @NotNull
        @Min(1)
        private long questionId;

        @NotNull
        @Min(1)
        private long memberId; //답변 작성자 멤버 아이디

        @NotNull
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Patch{
        @Min(1)
        private long memberId;
        private String content;
    }

    @Getter
    @Builder
    public static class Response{
        private long answerId;
        private long questionId;
        private long memberId;
        private String content;
    }
}
