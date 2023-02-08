package CoffeeOrderWebApp.practice.answer.controller;

import CoffeeOrderWebApp.practice.answer.dto.AnswerDto;
import CoffeeOrderWebApp.practice.answer.entity.Answer;
import CoffeeOrderWebApp.practice.answer.mapper.AnswerMapper;
import CoffeeOrderWebApp.practice.answer.service.AnswerService;
import CoffeeOrderWebApp.practice.utils.Uri;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@Validated
@RequestMapping("/jmt/answer")
public class AnswerController {
    private final String DEFAULT_URI = "/jmt/answer/";
    private AnswerMapper mapper;
    private AnswerService answerService;

    public AnswerController(AnswerMapper mapper, AnswerService answerService) {
        this.mapper = mapper;
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity postAnswer(@RequestBody @Valid AnswerDto.Post post){
        Answer answer = mapper.answerPostDtoToAnswer(post);
        Answer createdAnswer = answerService.createAnswer(answer);
        URI location = Uri.createUri(DEFAULT_URI, Long.toString(createdAnswer.getAnswerId()));
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{answer-id}")
    public ResponseEntity patchAnswer(@PathVariable("answer-id") @Positive long answerId,
                                      @RequestBody @Valid AnswerDto.Patch patch){
        Answer answer = mapper.answerPatchDtoToAnswer(patch);
        answer.setAnswerId(answerId);
        Answer modifiedAnswer = answerService.modifyAnswer(answer);
        return new ResponseEntity(mapper.answerToResponseDto(modifiedAnswer), HttpStatus.OK);
    }

    @DeleteMapping("/{answer-id}")
    public ResponseEntity deleteAnswer(@PathVariable("answer-id") @Positive long answerId){
        answerService.deleteAnswer(answerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
