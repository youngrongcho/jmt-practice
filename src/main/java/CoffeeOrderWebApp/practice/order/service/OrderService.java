package CoffeeOrderWebApp.practice.order.service;

import CoffeeOrderWebApp.practice.coffee.service.CoffeeService;
import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.service.MemberService;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.order.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private MemberService memberService;
    private CoffeeService coffeeService;

    public OrderService(OrderRepository orderRepository, MemberService memberService, CoffeeService coffeeService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.coffeeService = coffeeService;
    }

    public Order createOrder(Order order){
        Member foundMember = memberService.findMember(order.getMember().getMemberId());
        order.getOrderedCoffees().stream()
                .forEach(coffee -> {coffeeService.findCoffee(coffee.getCoffee().getCoffeeId());});
        int newStampCount = memberService.addStampCount(order);
        foundMember.getStamp().setStampCount(newStampCount);
        return orderRepository.save(order);
    }

    public Order modifyOrder(Order order){
        Order foundOrder = findOrder(order.getOrderId());
        if(foundOrder.getStatus().getStep()>=2) throw new LogicException(ExceptionEnum.ORDER_CANNOT_CHANGE);
        foundOrder.setOrderedCoffees(order.getOrderedCoffees());
        return foundOrder;
    }

    public Order getOrder(long orderId){
        Order foundOrder = findOrder(orderId);
        return orderRepository.save(foundOrder);
    }

    public Page<Order> getOrders(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize(), pageable.getSort());
        return orderRepository.findAll(pageRequest);
    }

    public Order deleteOrder(long orderId){
        Order foundOrder = findOrder(orderId);
        if(foundOrder.getStatus().getStep()>=2) throw new LogicException(ExceptionEnum.ORDER_CANNOT_CANCEL);
        foundOrder.setStatus(Order.Status.ORDER_CANCEL);
        return orderRepository.save(foundOrder);
    }

    private Order findOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order foundOrder = optionalOrder.orElseThrow(()->new LogicException(ExceptionEnum.ORDER_NOT_FOUND));
        return foundOrder;
    }
}
