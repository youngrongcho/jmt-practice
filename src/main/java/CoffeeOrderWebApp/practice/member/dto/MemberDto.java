package CoffeeOrderWebApp.practice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class MemberDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postDto{
        @NotNull
        @Pattern(regexp = "[a-zA-Z가-힣]+", message = "한글 또는 영문 이름을 작성해주세요.")
        @Length(max = 8, message = "이름은 8자 이내로 작성해주세요.")
        private String name;

        @NotNull
        @Email
        private String email;

        @NotNull
        @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "000-0000-0000 형식으로 입력해주세요.")
        private String phone;

        @NotNull
        @Length(min = 6, message = "비밀번호는 6자 이상으로 작성해주세요.")
        private String password;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class patchDto{
        @Pattern(regexp = "[a-zA-Z가-힣]+", message = "한글 또는 영문 이름을 작성해주세요.")
        @Length(max = 8, message = "8자 이내로 작성해주세요.")
        private String name;

        @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "000-0000-0000 형식으로 입력해주세요.")
        private String phone;

        @Length(min = 6, message = "비밀번호는 6자 이상으로 작성해주세요.")
        private String password;
    }

    @Getter
    @Builder
    public static class responseDto{
        private long memberId;
        private String name;
        private String email;
        private String phone;
        private int stampCount;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
