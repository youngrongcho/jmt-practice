package CoffeeOrderWebApp.practice.order.mapper;

import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.order.dto.OrderDto;
import CoffeeOrderWebApp.practice.order.dto.OrderedCoffeeDto;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.order.entity.OrderedCoffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order orderPostDtoToOrder(OrderDto.Post postDto){
        // memberId -> member, List<OrderedCoffeeDto> orderedCoffees -> List<OrderedCoffee> orderedCoffees
        Member member = new Member();
        Order order = new Order();

        member.setMemberId(postDto.getMemberId());
        order.setMember(member);

        order.setOrderedCoffees(
                postDto.getOrderedCoffees()
                        .stream().map(orderedCoffeeRequestDto -> {
                            OrderedCoffee orderedCoffee = new OrderedCoffee();
                            Coffee coffee = new Coffee();

                            coffee.setCoffeeId(orderedCoffeeRequestDto.getCoffeeId());
                            orderedCoffee.setQuantity(orderedCoffeeRequestDto.getQuantity());
                            orderedCoffee.setOrder(order);
                            orderedCoffee.setCoffee(coffee);

                            return orderedCoffee;
        }).collect(Collectors.toList()));

        return order;
    }

    Order orderPatchDtoToOrder(OrderDto.Patch patchDto);

    @Mapping(source = "coffee.coffeeId", target = "coffeeId")
    @Mapping(source = "coffee.korName", target = "korName")
    @Mapping(source = "coffee.engName", target = "engName")
    @Mapping(source = "coffee.price", target = "price")
    @Mapping(source = "coffee.coffeeCode", target = "coffeeCode")
    OrderedCoffeeDto.Response orderedCoffeeToOrderedCoffeeResponseDto(OrderedCoffee orderedCoffee);

    @Mapping(source = "member.memberId", target = "memberId")
    OrderDto.Response orderToOrderResponseDto(Order order);

    List<OrderDto.Response> ordersToOrderResponseDtos(List<Order> orderEntities);
}
