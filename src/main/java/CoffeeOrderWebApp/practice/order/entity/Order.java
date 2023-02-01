package CoffeeOrderWebApp.practice.order.entity;

import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order extends TimeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ORDER_REQUEST;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderedCoffee> orderedCoffees = new ArrayList<>();

    @Getter
    public enum Status{
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRMED(2, "주문 확인"),
        ORDER_COMPLETED(3, "주문 완료"),
        ORDER_CANCEL(4, "주문 취소");

        final int step;
        final String status;

        Status(int step, String status) {
            this.step = step;
            this.status = status;
        }
    }

}
