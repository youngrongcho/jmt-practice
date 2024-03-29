package CoffeeOrderWebApp.practice.order.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderedCoffeeDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        @NotNull
        @Min(1)
        private long coffeeId;

        @NotNull
        @Min(1)
        private int quantity;
    }

    @Getter
    @Builder
    @Setter
    public static class Response{
        private long coffeeId;
        private String korName;
        private String engName;
        private int price;
        private String coffeeCode;
        private int quantity;
    }
}
