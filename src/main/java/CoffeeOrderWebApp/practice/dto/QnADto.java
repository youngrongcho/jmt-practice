package CoffeeOrderWebApp.practice.dto;

import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.question.dto.QuestionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnADto{
    private QuestionDto.Response question;
    private String answer;

    public QnADto(QuestionDto.Response question, Answer answer) {
        this.question = question;
        this.answer = (answer==null ? "" : answer.getContent());
    }
}
