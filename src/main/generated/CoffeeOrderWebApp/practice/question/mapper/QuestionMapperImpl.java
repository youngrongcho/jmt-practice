package CoffeeOrderWebApp.practice.question.mapper;

import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.question.dto.QuestionDto;
import CoffeeOrderWebApp.practice.question.entity.Question;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-08T21:11:54+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public Question questionPostDtoToQuestion(QuestionDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Question question = new Question();

        question.setMember( postToMember( post ) );
        question.setTitle( post.getTitle() );
        question.setContent( post.getContent() );
        if ( post.getOpenToOthers() != null ) {
            question.setOpenToOthers( Enum.valueOf( Question.OpenToOthers.class, post.getOpenToOthers() ) );
        }

        return question;
    }

    @Override
    public Question questionPatchDtoToQuestion(QuestionDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Question question = new Question();

        question.setTitle( patch.getTitle() );
        question.setContent( patch.getContent() );
        question.setOpenLike( patch.isOpenLike() );
        if ( patch.getStatus() != null ) {
            question.setStatus( Enum.valueOf( Question.Status.class, patch.getStatus() ) );
        }
        if ( patch.getOpenToOthers() != null ) {
            question.setOpenToOthers( Enum.valueOf( Question.OpenToOthers.class, patch.getOpenToOthers() ) );
        }

        return question;
    }

    @Override
    public QuestionDto.Response questionToResponseDto(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionDto.Response.ResponseBuilder response = QuestionDto.Response.builder();

        response.questionId( question.getQuestionId() );
        response.title( question.getTitle() );
        response.content( question.getContent() );
        if ( question.getOpenToOthers() != null ) {
            response.openToOthers( question.getOpenToOthers().name() );
        }
        if ( question.getStatus() != null ) {
            response.status( question.getStatus().name() );
        }
        response.viewCount( question.getViewCount() );
        response.likeCount( question.getLikeCount() );
        response.createdAt( question.getCreatedAt() );
        response.modifiedAt( question.getModifiedAt() );

        return response.build();
    }

    @Override
    public List<QuestionDto.Response> questionsToResponseDtos(List<Question> question) {
        if ( question == null ) {
            return null;
        }

        List<QuestionDto.Response> list = new ArrayList<QuestionDto.Response>( question.size() );
        for ( Question question1 : question ) {
            list.add( questionToResponseDto( question1 ) );
        }

        return list;
    }

    protected Member postToMember(QuestionDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Member member = new Member();

        member.setMemberId( post.getMemberId() );

        return member;
    }
}
