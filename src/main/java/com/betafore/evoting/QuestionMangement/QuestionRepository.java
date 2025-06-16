package com.betafore.evoting.QuestionMangement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
            select q from Question q where q.question = ?1
        """)
    Optional<Question> findQuestionByQuestion(String question);

    Optional<Question> findByQuestionAndExpoId(String question,Long expoId);

    @Query("""
          select count (*) from Question u where u.expoId =?1
        """)
    Integer totalQuestion(Long expoId);

    List<Question> findAllByExpoId(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
