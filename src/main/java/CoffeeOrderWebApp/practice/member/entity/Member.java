package CoffeeOrderWebApp.practice.member.entity;

import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.question.entity.Like;
import CoffeeOrderWebApp.practice.question.entity.Question;
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

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.MEMBER_ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList();

    @OneToOne(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Stamp stamp;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "member") //완전 종속 관계가 아니라서 캐스캐이드 생략
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Answer> answerList = new ArrayList<>();

    @Getter
    public enum Status{
        MEMBER_ACTIVE("활동 회원"),
        MEMBER_NON_ACTIVE("휴면 회원"),
        MEMBER_GONE("탈퇴 회원");

        final String status;

        Status(String status) {
            this.status = status;
        }
    }

    public void setStamp(Stamp stamp) { // stamp랑 member 양방향 매핑
        this.stamp = stamp;
        if(stamp.getMember()!=this) {
            stamp.setMember(this);
        }
    }

    public void setOrder(Order order) { // order랑 member 양방향 매핑
        this.getOrderList().add(order);
        if(order.getMember()!=this){
            order.setMember(this);
        }
    }

    public void setLike(Like like) {
        this.getLikeList().add(like);
        if(like.getMember()!=this){
            like.setMember(this);
        }
    }

    public void setQuestion(Question question) {
        this.getQuestionList().add(question);
        if(question.getMember()!=this){
            question.setMember(this);
        }
    }

    public void setAnswer(Answer answer) {
        this.getAnswerList().add(answer);
        if(answer.getMember()!=this){
            answer.setMember(this);
        }
    }
}
