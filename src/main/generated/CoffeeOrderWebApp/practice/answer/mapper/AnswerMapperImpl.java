package CoffeeOrderWebApp.practice.answer.mapper;

import CoffeeOrderWebApp.practice.answer.dto.AnswerDto;
import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.question.entity.Question;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-08T21:11:54+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public Answer answerPostDtoToAnswer(AnswerDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setQuestion( postToQuestion( post ) );
        answer.setMember( postToMember( post ) );
        answer.setContent( post.getContent() );

        return answer;
    }

    @Override
    public Answer answerPatchDtoToAnswer(AnswerDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setMember( patchToMember( patch ) );
        answer.setContent( patch.getContent() );

        return answer;
    }

    @Override
    public AnswerDto.Response answerToResponseDto(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerDto.Response.ResponseBuilder response = AnswerDto.Response.builder();

        response.questionId( answerQuestionQuestionId( answer ) );
        Long memberId = answerMemberMemberId( answer );
        if ( memberId != null ) {
            response.memberId( memberId );
        }
        response.answerId( answer.getAnswerId() );
        response.content( answer.getContent() );

        return response.build();
    }

    protected Question postToQuestion(AnswerDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Question question = new Question();

        question.setQuestionId( post.getQuestionId() );

        return question;
    }

    protected Member postToMember(AnswerDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Member member = new Member();

        member.setMemberId( post.getMemberId() );

        return member;
    }

    protected Member patchToMember(AnswerDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Member member = new Member();

        member.setMemberId( patch.getMemberId() );

        return member;
    }

    private long answerQuestionQuestionId(Answer answer) {
        if ( answer == null ) {
            return 0L;
        }
        Question question = answer.getQuestion();
        if ( question == null ) {
            return 0L;
        }
        long questionId = question.getQuestionId();
        return questionId;
    }

    private Long answerMemberMemberId(Answer answer) {
        if ( answer == null ) {
            return null;
        }
        Member member = answer.getMember();
        if ( member == null ) {
            return null;
        }
        Long memberId = member.getMemberId();
        if ( memberId == null ) {
            return null;
        }
        return memberId;
    }
}
