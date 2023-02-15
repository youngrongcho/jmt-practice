package CoffeeOrderWebApp.practice.question.controller;

import CoffeeOrderWebApp.practice.dto.MultiDto;
import CoffeeOrderWebApp.practice.dto.QnADto;
import CoffeeOrderWebApp.practice.question.dto.QuestionDto;
import CoffeeOrderWebApp.practice.question.entity.Question;
import CoffeeOrderWebApp.practice.question.mapper.QuestionMapper;
import CoffeeOrderWebApp.practice.question.service.QuestionService;
import CoffeeOrderWebApp.practice.utils.Uri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/jmt/question")
public class QuestionController {
    private final String DEFAULT_URI = "/jmt/question/";

    QuestionService questionService;
    QuestionMapper mapper;

    public QuestionController(QuestionService questionService, QuestionMapper mapper) {
        this.questionService = questionService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postQuestion(@RequestBody @Valid QuestionDto.Post post) {
        Question question = mapper.questionPostDtoToQuestion(post);
        Question createdQuestion = questionService.createQuestion(question);
        URI location = Uri.createUri(DEFAULT_URI, Long.toString(createdQuestion.getQuestionId()));
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @RequestBody @Valid QuestionDto.Patch patch) {
        Question question = mapper.questionPatchDtoToQuestion(patch);
        question.setQuestionId(questionId);
        Question modifiedQuestion = questionService.modifyQuestion(question);
        return new ResponseEntity(mapper.questionToResponseDto(modifiedQuestion), HttpStatus.OK);
    }

    @GetMapping("/{question-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId) {
        Question question = questionService.getQuestion(questionId);
        return new ResponseEntity( // 질문 조회 시, 답변 있으면 답변도 함께 조회.
                new QnADto(mapper.questionToResponseDto(question), question.getAnswer()),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestions(Pageable pageable) {
        Page<Question> questionPage = questionService.getQuestions(pageable);
        List<Question> questionList = questionPage.getContent();
        questionList.stream().map(question -> questionService.updateOld(question)).collect(Collectors.toList());
        return new ResponseEntity(new MultiDto<>(questionList.stream().map(
                question -> new QnADto(mapper.questionToResponseDto(question), question.getAnswer()))
                .collect(Collectors.toList()), questionPage), HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") @Positive long questionId) {
        questionService.deleteQuestion(questionId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{question-id}/{member-id}") //게시물에 좋아요 추가
    public ResponseEntity postLike(@PathVariable("question-id") @Positive long questionId,
                                   @PathVariable("member-id") @Positive long memberId){
        questionService.addLike(questionId, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}/{member-id}") // 게시물 좋아요 삭제
    public ResponseEntity deleteLike(@PathVariable("question-id") @Positive long questionId,
                                     @PathVariable("member-id") @Positive long memberId){
        questionService.cancelLike(questionId, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
