package CoffeeOrderWebApp.practice.question.entity;

import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.member.entity.Member;
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
public class Question extends TimeManager { // 등록 날짜
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionId;

    @Column(nullable = false) // 필수입력
    private String title;

    @Column(nullable = false) // 필수입력
    private String content;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int likeCount =0;

    @Column(nullable = false)
    private boolean openLike = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.QUESTION_REGISTRATION; // 질문의 상태 값, 초기 상태 값

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenToOthers openToOthers = OpenToOthers.QUESTION_PUBLIC; // 필수입력

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NewQuestion newQuestion = NewQuestion.NEW_QUESTION; // 최신 글 표시??? -> 조회할 때 변경 (5초) LocalDateTime 계산

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Like> likeList = new ArrayList<>(); // 갯수 표시??? -> resonseDto로 매핑할 때 갯수로 변경. expression

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 질문 조회 시, 해당 질문에 대한 답변이 존재한다면 답변도 함께 조회되어야 한다.
    @OneToOne(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Answer answer;

    @Getter
    public enum Status {
        QUESTION_REGISTRATION("질문 등록"),
        QUESTION_ANSWERED("답변 완료"),
        QUESTION_DELETE("질문 삭제");

        final String status;

        Status(String status) {
            this.status = status;
        }
    }

    @Getter
    public enum OpenToOthers {
        QUESTION_PUBLIC("공개글"),
        QUESTION_SECRET("비밀글");

        final String status;

        OpenToOthers(String status) {
            this.status = status;
        }
    }

    public enum NewQuestion {
        NEW_QUESTION("최신 게시물"),
        QUESTION("게시물");

        final String newQuestion;

        NewQuestion(String newQuestion) {
            this.newQuestion = newQuestion;
        }
    }

    public void setLike(Like like) {
        this.getLikeList().add(like);
        if (like.getQuestion()!=this) {
            like.setQuestion(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getQuestionList().contains(this)) {
            member.getQuestionList().add(this);
        }
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
        if (answer.getQuestion() != this) {
            answer.setQuestion(this);
        }
    }
}
