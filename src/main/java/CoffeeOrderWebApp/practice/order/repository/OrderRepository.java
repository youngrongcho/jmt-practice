package CoffeeOrderWebApp.practice.order.repository;

import CoffeeOrderWebApp.practice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
