package CoffeeOrderWebApp.practice.question.repository;

import CoffeeOrderWebApp.practice.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT q FROM Question q WHERE q.status <> 'QUESTION_DELETE' and q.openToOthers <> 'QUESTION_SECRET'")
    Page<Question> findAll(Pageable pageable);
}
