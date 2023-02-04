package CoffeeOrderWebApp.practice.order.mapper;

import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.order.dto.OrderDto;
import CoffeeOrderWebApp.practice.order.dto.OrderedCoffeeDto;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.order.entity.OrderedCoffee;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-03T21:52:59+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order orderPatchDtoToOrder(OrderDto.Patch patchDto) {
        if ( patchDto == null ) {
            return null;
        }

        Order order = new Order();

        order.setStatus( patchDto.getStatus() );

        return order;
    }

    @Override
    public OrderedCoffeeDto.Response orderedCoffeeToOrderedCoffeeResponseDto(OrderedCoffee orderedCoffee) {
        if ( orderedCoffee == null ) {
            return null;
        }

        OrderedCoffeeDto.Response.ResponseBuilder response = OrderedCoffeeDto.Response.builder();

        Long coffeeId = orderedCoffeeCoffeeCoffeeId( orderedCoffee );
        if ( coffeeId != null ) {
            response.coffeeId( coffeeId );
        }
        response.korName( orderedCoffeeCoffeeKorName( orderedCoffee ) );
        response.engName( orderedCoffeeCoffeeEngName( orderedCoffee ) );
        Integer price = orderedCoffeeCoffeePrice( orderedCoffee );
        if ( price != null ) {
            response.price( price );
        }
        response.coffeeCode( orderedCoffeeCoffeeCoffeeCode( orderedCoffee ) );
        response.quantity( orderedCoffee.getQuantity() );

        return response.build();
    }

    @Override
    public OrderDto.Response orderToOrderResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto.Response.ResponseBuilder response = OrderDto.Response.builder();

        Long memberId = orderMemberMemberId( order );
        if ( memberId != null ) {
            response.memberId( memberId );
        }
        response.orderId( order.getOrderId() );
        response.status( order.getStatus() );
        response.orderedCoffees( orderedCoffeeListToResponseList( order.getOrderedCoffees() ) );
        response.createdAt( order.getCreatedAt() );
        response.modifiedAt( order.getModifiedAt() );

        return response.build();
    }

    @Override
    public List<OrderDto.Response> ordersToOrderResponseDtos(List<Order> orderEntities) {
        if ( orderEntities == null ) {
            return null;
        }

        List<OrderDto.Response> list = new ArrayList<OrderDto.Response>( orderEntities.size() );
        for ( Order order : orderEntities ) {
            list.add( orderToOrderResponseDto( order ) );
        }

        return list;
    }

    private Long orderedCoffeeCoffeeCoffeeId(OrderedCoffee orderedCoffee) {
        if ( orderedCoffee == null ) {
            return null;
        }
        Coffee coffee = orderedCoffee.getCoffee();
        if ( coffee == null ) {
            return null;
        }
        Long coffeeId = coffee.getCoffeeId();
        if ( coffeeId == null ) {
            return null;
        }
        return coffeeId;
    }

    private String orderedCoffeeCoffeeKorName(OrderedCoffee orderedCoffee) {
        if ( orderedCoffee == null ) {
            return null;
        }
        Coffee coffee = orderedCoffee.getCoffee();
        if ( coffee == null ) {
            return null;
        }
        String korName = coffee.getKorName();
        if ( korName == null ) {
            return null;
        }
        return korName;
    }

    private String orderedCoffeeCoffeeEngName(OrderedCoffee orderedCoffee) {
        if ( orderedCoffee == null ) {
            return null;
        }
        Coffee coffee = orderedCoffee.getCoffee();
        if ( coffee == null ) {
            return null;
        }
        String engName = coffee.getEngName();
        if ( engName == null ) {
            return null;
        }
        return engName;
    }

    private Integer orderedCoffeeCoffeePrice(OrderedCoffee orderedCoffee) {
        if ( orderedCoffee == null ) {
            return null;
        }
        Coffee coffee = orderedCoffee.getCoffee();
        if ( coffee == null ) {
            return null;
        }
        Integer price = coffee.getPrice();
        if ( price == null ) {
            return null;
        }
        return price;
    }

    private String orderedCoffeeCoffeeCoffeeCode(OrderedCoffee orderedCoffee) {
        if ( orderedCoffee == null ) {
            return null;
        }
        Coffee coffee = orderedCoffee.getCoffee();
        if ( coffee == null ) {
            return null;
        }
        String coffeeCode = coffee.getCoffeeCode();
        if ( coffeeCode == null ) {
            return null;
        }
        return coffeeCode;
    }

    private Long orderMemberMemberId(Order order) {
        if ( order == null ) {
            return null;
        }
        Member member = order.getMember();
        if ( member == null ) {
            return null;
        }
        Long memberId = member.getMemberId();
        if ( memberId == null ) {
            return null;
        }
        return memberId;
    }

    protected List<OrderedCoffeeDto.Response> orderedCoffeeListToResponseList(List<OrderedCoffee> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderedCoffeeDto.Response> list1 = new ArrayList<OrderedCoffeeDto.Response>( list.size() );
        for ( OrderedCoffee orderedCoffee : list ) {
            list1.add( orderedCoffeeToOrderedCoffeeResponseDto( orderedCoffee ) );
        }

        return list1;
    }
}
