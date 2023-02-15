package CoffeeOrderWebApp.practice.answer.service;

import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.answer.repository.AnswerRepository;
import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.question.entity.Question;
import CoffeeOrderWebApp.practice.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public Answer createAnswer(Answer answer) {
        verifyAdmin();
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
        verifyAdmin();
        Answer foundAnswer = verifyExistAnswer(answer.getAnswerId());

        Optional.ofNullable(answer.getMember().getMemberId()).ifPresent(id -> foundAnswer.getMember().setMemberId(id));
        Optional.ofNullable(answer.getContent()).ifPresent(content -> foundAnswer.setContent(content));

        return answerRepository.save(foundAnswer);
    }

    public void deleteAnswer(long answerId) {
        verifyAdmin();
        verifyExistAnswer(answerId);
        answerRepository.deleteById(answerId);
    }

    private Answer verifyExistAnswer(long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer answer = optionalAnswer.orElseThrow(() -> new LogicException(ExceptionEnum.ANSWER_NOT_FOUND));
        return answer;
    }

    private void verifyAdmin() {
        String authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(!authorities.contains("ROLE_ADMIN")) throw new LogicException(ExceptionEnum.ADMIN_ACCESS_ONLY);;
    }
}
