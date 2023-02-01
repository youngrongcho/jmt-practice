package CoffeeOrderWebApp.practice.member.dto;

import CoffeeOrderWebApp.practice.member.entity.Member;
import lombok.*;

import javax.persistence.MappedSuperclass;
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
        private String name;

        @NotNull
        @Email
        private String email;

        @NotNull
        @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "000-0000-0000 형식으로 입력해주세요.")
        private String phone;
    }

    @Getter
    @Builder
    public static class patchDto{
        private String name;
        private String phone;
    }

    @Builder
    @Getter
    public static class responseDto{
        private long memberId;
        private String name;
        private String email;
        private String phone;
        private int stampCount;
        private Member.Status status;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
