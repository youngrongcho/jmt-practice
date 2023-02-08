package CoffeeOrderWebApp.practice.answer.repository;

import CoffeeOrderWebApp.practice.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
