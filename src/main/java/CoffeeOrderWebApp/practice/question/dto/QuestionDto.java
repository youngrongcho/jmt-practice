package CoffeeOrderWebApp.practice.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class QuestionDto {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Post{
        @NotNull
        @Min(1)
        private long memberId;

        @NotNull
        @Length(max = 30)
        private String title;

        @NotNull
        private String content;

        @NotNull
        private String openToOthers;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Patch{
        @Length(max = 30)
        private String title;
        private String content;
        private String openToOthers;
        private String status;
        private boolean openLike;
    }

    @Builder
    @Getter
    public static class Response{
        private long questionId;
        private long memberId;
        private String title;
        private String content;
        private String openToOthers;
        private String status;
        private String newQuestion;
        private int viewCount;
        private int likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
