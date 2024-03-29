package CoffeeOrderWebApp.practice.coffee.dto;

import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class CoffeeDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postDto{
        @NotNull
        @Pattern(regexp = "[가-힣]+", message = "커피 한글 이름을 입력해주세요.")
        private String korName;

        @NotNull
        @Pattern(regexp = "[A-Za-z]+" ,message = "커피 영어 이름을 적어주세요.")
        private String engName;

        @NotNull
        @Range(min = 2000, max = 20000)
        private Integer price;

        @NotNull
        @Pattern(regexp = "[A-Z]{3}", message = "대문자 영문 3자리로 입력해주세요.")
        private String coffeeCode;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class patchDto{
        @Pattern(regexp = "[가-힣]+", message = "커피 한글 이름을 입력해주세요.")
        private String korName;

        @Pattern(regexp = "[A-Za-z]+" ,message = "커피 영어 이름을 적어주세요.")
        private String engName;

        @Range(min = 2000, max = 20000)
        private Integer price;
    }

    @Getter
    @Builder
    public static class responseDto{
        private long coffeeId;
        private String korName;
        private String engName;
        private Integer price;
        private Coffee.Status status;
        private String coffeeCode;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
