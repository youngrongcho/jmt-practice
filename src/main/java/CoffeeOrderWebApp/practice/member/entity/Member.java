package CoffeeOrderWebApp.practice.member.entity;

import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.stamp.Stamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member extends TimeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.MEMBER_ACTIVE;

    @OneToOne(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Stamp stamp;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    public enum Status{
        MEMBER_ACTIVE("활동 회원"),
        MEMBER_NON_ACTIVE("휴면 회원"),
        MEMBER_GONE("탈퇴 회원");

        final String status;

        Status(String status) {
            this.status = status;
        }
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
        if(stamp.getMember()!=this) {
            stamp.setMember(this);
        }
    }
}
