package CoffeeOrderWebApp.practice.coffee.entity;

import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.order.entity.OrderedCoffee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coffee extends TimeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coffeeId;

    @Column(nullable = false)
    private String korName;

    @Column(nullable = false)
    private String engName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 3)
    private String coffeeCode;

    @OneToMany(mappedBy = "coffee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderedCoffee> orderedCoffees = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.COFFEE_ON;

    public enum Status{
        COFFEE_ON("판매 중"),
        COFFEE_OFF("판매 중지"),
        COFFEE_SOLD_OUT("재고 소진");

        final String status;

        Status(String status) {
            this.status = status;
        }
    }
}
