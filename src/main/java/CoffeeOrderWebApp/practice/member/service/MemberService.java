package CoffeeOrderWebApp.practice.member.service;

import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.repository.MemberRepository;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.stamp.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member){
        verifyExistMember(member.getEmail());
        member.setStamp(new Stamp());
        return memberRepository.save(member);
    }

    public Member modifyMember(Member member){
        Member foundmember = findMember(member.getMemberId());

        Optional.ofNullable(member.getName()).ifPresent(name->foundmember.setName(name));
        Optional.ofNullable(member.getPhone()).ifPresent(phone->foundmember.setPhone(phone));

        return memberRepository.save(foundmember);
    }

    public Member getMember(long memberId){
        Member member = findMember(memberId);
        return member;
    }

    public Page<Member> getMembers(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize(), pageable.getSort());
        Page<Member> memberPage = memberRepository.findAll(pageRequest);
        return memberPage;
    }

    public void deleteMember(long memberId){
        findMember(memberId);
        memberRepository.deleteById(memberId);
    }

    private void verifyExistMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.ifPresent( e -> { throw new LogicException(ExceptionEnum.MEMBER_EXISTS); });
    }

    public Member findMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(()->new LogicException(ExceptionEnum.MEMBER_NOT_FOUND));
        return findMember;
    }

    public int addStampCount(Order order) {
        Member foundMember = findMember(order.getMember().getMemberId());
        Stamp stamp = foundMember.getStamp();

        int stampCount = stamp.getStampCount();
        int quantity = order.getOrderedCoffees().stream()
                .map(orderedCoffee -> orderedCoffee.getQuantity()).mapToInt(i->i).sum();
        return stampCount+quantity;
    }
}
