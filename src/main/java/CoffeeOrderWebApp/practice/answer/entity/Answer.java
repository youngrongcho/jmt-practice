package CoffeeOrderWebApp.practice.answer.entity;

import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.question.entity.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Answer extends TimeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerId;

    @Column(nullable = false) // 필수입력
    private String content;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    // 답변 작성자 확인 및 기록위해 member와 매핑.

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenToOthers openToOthers = OpenToOthers.ANSWER_PUBLIC; // 필수입력

    @Getter
    public enum OpenToOthers {
        ANSWER_PUBLIC("공개글"),
        ANSWER_SECRET("비밀글");

        final String status;

        OpenToOthers(String status) {
            this.status = status;
        }
    }

    public void setQuestion(Question question) {
        this.question = question;
        if(question.getAnswer()!=this){
            question.setAnswer(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if(!member.getAnswerList().contains(this)){
            member.getAnswerList().add(this);
        }
    }
}
