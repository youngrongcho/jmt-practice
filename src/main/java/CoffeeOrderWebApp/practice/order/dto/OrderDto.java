package CoffeeOrderWebApp.practice.order.dto;

import CoffeeOrderWebApp.practice.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    @Getter
    @Builder
    public static class Post{
        @NotNull
        private long memberId;

        @Size(min =1)
        private List<OrderedCoffeeDto.Request> orderedCoffees;
    }

    @Getter
    @Builder
    public static class Patch {
        private Order.Status status;
    }

    @Getter
    @Builder
    public static class Response {
        private long orderId;
        private long memberId;
        private Order.Status status;
        private List<OrderedCoffeeDto.Response> orderedCoffees;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
