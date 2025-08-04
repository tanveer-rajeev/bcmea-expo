package com.betafore.evoting.VoteMangement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<VoteList> findAllByExpoId(Long expoId);

    List<VoteList> findAllByHall_Slug(String hallSlug);

    List<VoteList> findAllByStall_Slug(String stallSlug);

    List<VoteList> findAllByQuestion_Id(Long questionId);

    List<VoteList> findAllByHall_SlugAndStall_SlugAndQuestion_Id(String hallSlug, String stallSlug, Long questionId);

    List<VoteList> findAllByHall_SlugAndStall_Slug(String hallSlug, String stallSlug);

    List<VoteList> findAllByHall_SlugAndQuestion_Id(String hallSlug, Long questionId);

    List<VoteList> findAllByStall_SlugAndQuestion_Id(String stallSlug, Long questionId);

    @Query(
        value =
            "SELECT e.id as id,e.expoId as expoId, e.hall as hall, e.stall as stall, e.question as question, COUNT(e.stall) as totalVote " +
                "FROM Vote e " +
                "where e.expoId =?1 " +
                "GROUP BY e.hall, e.stall, e.question " +
                "ORDER BY e.hall ASC, e.question ASC, totalVote DESC "

    )
    List<VoteResult> voteCalculation(Long expoId);

    @Query(
        value = """
                SELECT e.id as id,e.expoId as expoId, e.hall as hall, e.stall as stall, e.question as question, COUNT(e.stall) as totalVote
                FROM Vote e
                WHERE e.hall.slug = ?1
                GROUP BY e.hall, e.stall, e.question
                ORDER BY e.hall ASC, e.question ASC, totalVote DESC
            """
    )
    List<VoteResult> resultOfAHall(String hallSlug);

    @Query(
        value = """
                SELECT e.id as id, e.hall as hall, e.stall as stall, e.question as question, COUNT(e.stall) as totalVote
                FROM Vote e
                WHERE e.stall.slug = ?1
                GROUP BY e.hall, e.stall, e.question
                ORDER BY e.hall ASC, e.question ASC, totalVote DESC
            """
    )
    List<VoteResult> resultOfAStall(String stallSlug);

    @Query(
        value = """
                SELECT e.id as id, e.hall as hall, e.stall as stall, e.question as question, COUNT(e.stall) as totalVote
                FROM Vote e
                WHERE e.hall.slug = ?1 AND e.stall.slug = ?2
                GROUP BY e.hall, e.stall, e.question
                ORDER BY e.hall ASC, e.question ASC, totalVote DESC
            """
    )
    List<VoteResult> resultOfAHallAndStall(String hallSlug, String stallSlug);

    @Query("SELECT COUNT(v) = 0 FROM Vote v WHERE  v.hall.id = ?1 AND v.user.id = ?2 ")
    boolean eligibility(Long hallId, Long userId);

    @Query("""
          select count (*) from Vote v where v.expoId =?1
        """)
    Integer totalVote(Long expoId);


    @Query(
        value = "SELECT q as question, u as user, h as hall, s as stall, vote.id as id, vote.createdAt as createdAt " +
            "FROM Hall h " +
            "LEFT JOIN Vote vote ON h.id = vote.hall.id and vote.expoId = :expoId " +
            "LEFT JOIN User u ON vote.user.id = u.id " +
            "LEFT JOIN Question q ON vote.question.id = q.id and q.expoId = :expoId " +
            "LEFT JOIN Stall s ON s.id = vote.stall.id and s.expoId = :expoId " +
            "WHERE h.expoId = :expoId"
    )
    List<VoteList> analytics(@Param("expoId") Long expoId);

    @Query(
        value = "SELECT q as question, u as user, h as hall, s as stall, vote.id as id, vote.createdAt as createdAt " +
            "FROM Hall h " +
            "LEFT JOIN Vote vote ON h.id = vote.hall.id and vote.expoId = :expoId " +
            "LEFT JOIN User u ON vote.user.id = u.id " +
            "LEFT JOIN Question q ON vote.question.id = q.id and q.expoId = :expoId " +
            "LEFT JOIN Stall s ON s.id = vote.stall.id and s.expoId = :expoId " +
            "WHERE h.slug = :hallSlug and h.expoId = :expoId"
    )
    List<VoteList> filterAnalyticsByHall(@Param("hallSlug") String hallSlug,
                                         @Param("expoId") Long expoId);

    void deleteAllByExpoId(Long expoId);

    void deleteAllByHallId(Long hallId);
}
