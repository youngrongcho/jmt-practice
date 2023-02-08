package CoffeeOrderWebApp.practice.question.entity;

import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Likes")
@NoArgsConstructor
@Getter
@Setter
public class Like extends TimeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setQuestion(Question question) {
        this.question = question;
        if(!question.getLikeList().contains(this)) {
            question.getLikeList().add(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if(!member.getLikeList().contains(this)){
            member.getLikeList().add(this);
        }
    }
}
