package CoffeeOrderWebApp.practice.order.controller;

import CoffeeOrderWebApp.practice.dto.MultiDto;
import CoffeeOrderWebApp.practice.order.dto.OrderDto;
import CoffeeOrderWebApp.practice.order.entity.Order;
import CoffeeOrderWebApp.practice.order.mapper.OrderMapper;
import CoffeeOrderWebApp.practice.order.service.OrderService;
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

@RestController
@Validated
@RequestMapping("/jmt/order")
public class OrderController {
    private final String DEFAULT_URI = "/jmt/order/";

    private OrderMapper mapper;
    private OrderService orderService;

    public OrderController(OrderMapper mapper, OrderService orderService) {
        this.mapper = mapper;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity postOrder(@RequestBody @Valid OrderDto.Post postDto){
        Order order = mapper.orderPostDtoToOrder(postDto);
        Order createdOrder = orderService.createOrder(order);
        URI location = Uri.createUri(DEFAULT_URI, Long.toString(createdOrder.getOrderId()));
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity patchOrder(@PathVariable("order-id") @Positive long orderId, @RequestBody OrderDto.Patch patchDto){
        Order order = mapper.orderPatchDtoToOrder(patchDto);
        order.setOrderId(orderId);
        Order modifiedOrder = orderService.modifyOrder(order);
        return new ResponseEntity(mapper.orderToOrderResponseDto(modifiedOrder), HttpStatus.OK);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") @Positive long orderId){
        Order order = orderService.getOrder(orderId);
        return new ResponseEntity(mapper.orderToOrderResponseDto(order), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(Pageable pageable){
        Page<Order> orderPage = orderService.getOrders(pageable);
        List<Order> orderList = orderPage.getContent();
        return new ResponseEntity(
                new MultiDto<>(mapper.ordersToOrderResponseDtos(orderList),orderPage), HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity deleteOrder(@PathVariable("order-id") @Positive long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
