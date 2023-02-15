package CoffeeOrderWebApp.practice.member.service;

import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.repository.MemberRepository;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.security.utils.CustomAuthorityUtils;
import CoffeeOrderWebApp.practice.stamp.Stamp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService { // publisher, transaction 공부해서 추가
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Member createMember(Member member) {
        verifyExistMember(member.getEmail());
        member.setStamp(new Stamp());

        //패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        //권한 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        return memberRepository.save(member);
    }

    public Member modifyMember(Member member) {
        Member foundMember = findMember(member.getMemberId());
        verifyUserSelf(foundMember);

        Optional.ofNullable(member.getName()).ifPresent(foundMember::setName);
        Optional.ofNullable(member.getPhone()).ifPresent(foundMember::setPhone);
        //비밀번호 변경 추가
        Optional.ofNullable(member.getPassword()).ifPresent(
                password -> foundMember.setPassword(passwordEncoder.encode(password)));

        return memberRepository.save(foundMember);
    }

    public Member getMember(long memberId) {
        Member member = findMember(memberId);
        verifyAdminOrUserSelf(member);
        return member;
    }

    public Page<Member> getMembers(Pageable pageable) {
        //관리자만 조회 가능하도록
        String authorities = getAuthorities();
        if (!authorities.contains("ROLE_ADMIN")) throw new LogicException(ExceptionEnum.ADMIN_ACCESS_ONLY);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        Page<Member> memberPage = memberRepository.findAll(pageRequest);
        return memberPage;
    }

    public void deleteMember(long memberId) {
        Member member = findMember(memberId);
        verifyUserSelf(member);
        memberRepository.deleteById(memberId);
    }

    private void verifyExistMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.ifPresent(e -> {
            throw new LogicException(ExceptionEnum.MEMBER_EXISTS);
        });
    }

    public Member findMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() -> new LogicException(ExceptionEnum.MEMBER_NOT_FOUND));
        return findMember;
    }

    public int addStampCount(Order order) {
        Member foundMember = findMember(order.getMember().getMemberId());
        Stamp stamp = foundMember.getStamp();

        int stampCount = stamp.getStampCount();
        int quantity = order.getOrderedCoffees().stream()
                .map(orderedCoffee -> orderedCoffee.getQuantity()).mapToInt(i -> i).sum();
        return stampCount + quantity;
    }

    private static String getPrincipal() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return principal;
    }

    private static String getAuthorities() {
        String authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        return authorities;
    }

    private void verifyUserSelf(Member member) {
        String principal = getPrincipal();
        if (!member.getEmail().equals(principal)) throw new LogicException(ExceptionEnum.SELF_ACCESS_ONLY);
    }

    private void verifyAdminOrUserSelf(Member member) {
        String principal = getPrincipal();
        String authorities = getAuthorities();
        if (!member.getEmail().equals(principal) && !authorities.contains("ROLE_ADMIN"))
            throw new LogicException(ExceptionEnum.SELF_AND_ADMIN_ONLY);
    }
}
