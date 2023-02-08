package CoffeeOrderWebApp.practice.answer.service;

import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.answer.repository.AnswerRepository;
import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.question.entity.Question;
import CoffeeOrderWebApp.practice.question.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;
    private QuestionService questionService;

    public AnswerService(AnswerRepository answerRepository, QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
    }

    public Answer createAnswer(Answer answer) {
        // 존재하는 질문인지 확인
        Question question = questionService.findQuestion(answer.getQuestion().getQuestionId());

        // 질문 상태(질문등록/답변완료/질문삭제) 확인
        if (question.getStatus() == Question.Status.QUESTION_ANSWERED) {
            throw new LogicException(ExceptionEnum.QUESTION_ANSWERED);
        }
        if (question.getStatus() == Question.Status.QUESTION_DELETE) {
            throw new LogicException(ExceptionEnum.QUESTION_DELETED);
        }

        //질문 공개여부 확인(공개글/비밀글)하여 답변 공개 여부 결정
        if (question.getOpenToOthers() == Question.OpenToOthers.QUESTION_SECRET) {
            answer.setOpenToOthers(Answer.OpenToOthers.ANSWER_SECRET);
        }

        // 질문 상태 : 답변완료 로 변경
        answer.getQuestion().setStatus(Question.Status.QUESTION_ANSWERED);

        return answerRepository.save(answer);
    }

    public Answer modifyAnswer(Answer answer) {
        Answer foundAnswer = verifyExistAnswer(answer.getAnswerId());

        Optional.ofNullable(answer.getMember().getMemberId()).ifPresent(id -> foundAnswer.getMember().setMemberId(id));
        Optional.ofNullable(answer.getContent()).ifPresent(content -> foundAnswer.setContent(content));

        return answerRepository.save(foundAnswer);
    }

    public void deleteAnswer(long answerId) {
        verifyExistAnswer(answerId);
        answerRepository.deleteById(answerId);
    }

    private Answer verifyExistAnswer(long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer answer = optionalAnswer.orElseThrow(()->new LogicException(ExceptionEnum.ANSWER_NOT_FOUND));
        return answer;
    }
}
