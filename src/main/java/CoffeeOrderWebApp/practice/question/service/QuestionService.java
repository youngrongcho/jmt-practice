package CoffeeOrderWebApp.practice.question.service;

import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.service.MemberService;
import CoffeeOrderWebApp.practice.question.entity.Like;
import CoffeeOrderWebApp.practice.question.entity.Question;
import CoffeeOrderWebApp.practice.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberService memberService;

    public Question createQuestion(Question question) {
        memberService.findMember(question.getMember().getMemberId()); // 질문 등록자가 회원인지 확인.
        // 이미 있는 질문 어떻게 거를 것인지? -> 일단 패스
        return questionRepository.save(question);
    }

    public Question modifyQuestion(Question question) {
        Question foundQuestion = findQuestion(question.getQuestionId());
        verifyUserSelf(foundQuestion.getMember()); //당사자만 변경 가능

        Optional.ofNullable(question.getTitle()).ifPresent(foundQuestion::setTitle);
        Optional.ofNullable(question.getContent()).ifPresent(foundQuestion::setContent);
        Optional.ofNullable(question.getOpenToOthers()).ifPresent(foundQuestion::setOpenToOthers);
        Optional.ofNullable(question.getStatus()).ifPresent(foundQuestion::setStatus);
        Optional.ofNullable(question.isOpenLike()).ifPresent(foundQuestion::setOpenLike);

        return questionRepository.save(foundQuestion);
    }

    public Question getQuestion(long questionId) {
        Question question = findQuestion(questionId);
        if (question.getStatus() == Question.Status.QUESTION_DELETE)
            throw new LogicException(ExceptionEnum.QUESTION_DELETED);
        question.setViewCount(question.getViewCount() + 1); //조회 수 증가
        return updateOld(question);
    }

    public Page<Question> getQuestions(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        return questionRepository.findAll(pageRequest);
    }

    public void deleteQuestion(long questionId) {
        Question question = findQuestion(questionId);
        verifyUserSelf(question.getMember());
        question.setStatus(Question.Status.QUESTION_DELETE);
        questionRepository.save(question);
    }

    public Question findQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.orElseThrow(() -> new LogicException(ExceptionEnum.QUESTION_NOT_FOUND));
        return question;
    }

    public void addLike(long questionId, long memberId) {
        Question question = findQuestion(questionId); //질문 확인
        Member member = memberService.findMember(memberId); // 회원 확인
        verifyUserSelf(member);

        //이미 라이크 했으면 익셉션 던져야 함.
        question.getLikeList().stream()
                .filter(like -> like.getMember().getMemberId() == memberId)
                .findFirst()
                .ifPresent(l -> {
                    throw new LogicException(ExceptionEnum.ALREADY_LIKED);
                });

        //라이크 새로 만들어주기 - 캐스캐이드 확인! 새로 합치는 거니까 merge 해야함.
        Like like = new Like();
        like.setQuestion(question);
        like.setMember(member);

        question.setLikeCount(question.getLikeCount() + 1);
        questionRepository.save(question);
    }

    public void cancelLike(long questionId, long memberId) {
        Question question = findQuestion(questionId); //질문 확인
        Member member = memberService.findMember(memberId); // 회원 확인
        verifyUserSelf(member);

        // 좋아요가 있는지 확인 후, 없으면 익셉션
        question.getLikeList().stream()
                .filter(like -> like.getMember().getMemberId() == memberId)
                .findFirst()
                .orElseThrow(() -> new LogicException(ExceptionEnum.THERE_IS_NO_LIKE));

        question.setLikeList(question.getLikeList().stream().filter(like -> like.getMember() != member)
                .collect(Collectors.toList()));

        question.setLikeCount(question.getLikeCount() - 1);
        questionRepository.save(question);
    }

    public Question updateOld(Question question) { //최신글 로직 10초 기준 잡기.
        if (LocalDateTime.now().isAfter(question.getCreatedAt().plusSeconds(10))) {
            question.setNewQuestion(Question.NewQuestion.QUESTION);
        }
        return questionRepository.save(question);
    }

    private static String getPrincipal() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return principal;
    }

    private static void verifyUserSelf(Member member) {
        String principal = getPrincipal();
        if (!member.getEmail().equals(principal)) throw new LogicException(ExceptionEnum.SELF_ACCESS_ONLY);
    }
}
