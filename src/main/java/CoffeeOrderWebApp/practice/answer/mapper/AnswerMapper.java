package CoffeeOrderWebApp.practice.answer.mapper;

import CoffeeOrderWebApp.practice.answer.dto.AnswerDto;
import CoffeeOrderWebApp.practice.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    @Mapping(source = "questionId", target = "question.questionId")
    @Mapping(source = "memberId", target = "member.memberId")
    Answer answerPostDtoToAnswer(AnswerDto.Post post);

    @Mapping(source = "memberId", target = "member.memberId")
    Answer answerPatchDtoToAnswer(AnswerDto.Patch patch);

    @Mapping(source = "question.questionId", target = "questionId")
    @Mapping(source = "member.memberId", target = "memberId")
    AnswerDto.Response answerToResponseDto(Answer answer);
}
