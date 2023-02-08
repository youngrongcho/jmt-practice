package CoffeeOrderWebApp.practice.question.mapper;

import CoffeeOrderWebApp.practice.question.dto.QuestionDto;
import CoffeeOrderWebApp.practice.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    Question questionPostDtoToQuestion(QuestionDto.Post post);

    Question questionPatchDtoToQuestion(QuestionDto.Patch patch);

//    @Mapping(target = "likeCount", expression = "java(question.getLikeList().size())")
    QuestionDto.Response questionToResponseDto(Question question);

    List<QuestionDto.Response> questionsToResponseDtos(List<Question> question);
}
