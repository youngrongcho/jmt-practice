package CoffeeOrderWebApp.practice.member.controller;

import CoffeeOrderWebApp.practice.dto.MultiDto;
import CoffeeOrderWebApp.practice.member.dto.MemberDto;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.mapper.MemberMapper;
import CoffeeOrderWebApp.practice.member.service.MemberService;
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

@Validated
@RestController
@RequestMapping("/jmt/member")
public class MemberController {
    private final String DEFAULT_URI = "/jmt/member/";

    private MemberMapper mapper;
    private MemberService memberService;

    public MemberController(MemberMapper mapper, MemberService memberService) {
        this.mapper = mapper;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity postMember(@RequestBody @Valid MemberDto.postDto postDto){
        Member member = mapper.MemberPostDtoToMember(postDto);
        Member createdMember = memberService.createMember(member);
        URI location = Uri.createUri(DEFAULT_URI, Long.toString(createdMember.getMemberId()));
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId, @RequestBody MemberDto.patchDto patchDto){
        Member member = mapper.MemberPatchDtoToMember(patchDto);
        member.setMemberId(memberId);
        Member modified = memberService.modifyMember(member);
        return new ResponseEntity(mapper.MemberToMemberResponseDto(modified), HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member member = memberService.getMember(memberId);
        return new ResponseEntity(mapper.MemberToMemberResponseDto(member), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(Pageable pageable){
        Page<Member> memberPage = memberService.getMembers(pageable);
        List<Member> memberList = memberPage.getContent();
        return new ResponseEntity(
                new MultiDto<>(mapper.MemberListToMemberResponseDtos(memberList), memberPage), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId){
        memberService.deleteMember(memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
