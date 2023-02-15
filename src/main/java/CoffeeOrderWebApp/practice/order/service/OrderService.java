package CoffeeOrderWebApp.practice.order.service;

import CoffeeOrderWebApp.practice.coffee.service.CoffeeService;
import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.service.MemberService;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final CoffeeService coffeeService;

    public Order createOrder(Order order){
        verifyUser();
        Member foundMember = memberService.findMember(order.getMember().getMemberId());
        order.getOrderedCoffees().stream()
                .forEach(coffee -> {coffeeService.findCoffee(coffee.getCoffee().getCoffeeId());});
        int newStampCount = memberService.addStampCount(order);
        foundMember.getStamp().setStampCount(newStampCount);
        return orderRepository.save(order);
    }

    public Order modifyOrder(Order order){
        Order foundOrder = findOrder(order.getOrderId());
        verifyUserSelf(order.getMember()); //주문 당사자인지 확인
        if(foundOrder.getStatus().getStep()>=2) throw new LogicException(ExceptionEnum.ORDER_CANNOT_CHANGE);
        foundOrder.setOrderedCoffees(order.getOrderedCoffees());
        return foundOrder;
    }

    public Order getOrder(long orderId){
        Order foundOrder = findOrder(orderId);
        verifyAdminOrUserSelf(foundOrder);
        return orderRepository.save(foundOrder);
    }

    public Page<Order> getOrders(Pageable pageable){
        verifyAdmin();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize(), pageable.getSort());
        return orderRepository.findAll(pageRequest);
    }

    public Order deleteOrder(long orderId){
        Order foundOrder = findOrder(orderId);
        verifyUserSelf(foundOrder.getMember());
        if(foundOrder.getStatus().getStep()>=2) throw new LogicException(ExceptionEnum.ORDER_CANNOT_CANCEL);
        foundOrder.setStatus(Order.Status.ORDER_CANCEL);
        return orderRepository.save(foundOrder);
    }

    private Order findOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order foundOrder = optionalOrder.orElseThrow(()->new LogicException(ExceptionEnum.ORDER_NOT_FOUND));
        return foundOrder;
    }

    private static String getPrincipal() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return principal;
    }

    private static String getAuthorities() {
        String authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        return authorities;
    }

    private void verifyAdmin() {
        String authorities = getAuthorities();
        if(!authorities.contains("ROLE_ADMIN")) throw new LogicException(ExceptionEnum.ADMIN_ACCESS_ONLY);;
    }

    private void verifyUser() {
        String authorities = getAuthorities();
        if(!authorities.contains("ROLE_USER")) throw new LogicException(ExceptionEnum.USER_ACCESS_ONLY);
    }

    private void verifyUserSelf(Member member) {
        String principal = getPrincipal();
        if(!member.getEmail().equals(principal)) throw new LogicException(ExceptionEnum.SELF_ACCESS_ONLY);
    }

    private void verifyAdminOrUserSelf(Order order) {
        String principal = getPrincipal();
        String authorities = getAuthorities();
        if(!order.getMember().getEmail().equals(principal) && !authorities.contains("ROLE_ADMIN"))
            throw new LogicException(ExceptionEnum.SELF_AND_ADMIN_ONLY);
    }
}
