package CoffeeOrderWebApp.practice.member.service;

import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.repository.MemberRepository;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.order.entity.OrderedCoffee;
import CoffeeOrderWebApp.practice.stamp.Stamp;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Autowired
    private Gson gson;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void findMember() {
        //given
        Long memberId = 1L;
        given(memberRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(null));

        //when, then
        assertThrows(LogicException.class, () -> memberService.findMember(memberId));
    }

    @Test
    void addStampCount() {
        //given
        Order order = new Order();
        Member member = new Member();

        member.setMemberId(1L);
        member.setStamp(new Stamp());
        member.getStamp().setStampCount(3);

        OrderedCoffee orderedCoffee1 = new OrderedCoffee();
        OrderedCoffee orderedCoffee2 = new OrderedCoffee();

        orderedCoffee1.setQuantity(3);
        orderedCoffee2.setQuantity(3);

        order.setOrderedCoffees(List.of(orderedCoffee1, orderedCoffee2));

        //when
        int stampCount = member.getStamp().getStampCount();
        int quantity = order.getOrderedCoffees().stream()
                .map(orderedCoffee -> orderedCoffee.getQuantity()).mapToInt(i->i).sum();

        // then
        assertEquals(9, stampCount+quantity);
    }
}