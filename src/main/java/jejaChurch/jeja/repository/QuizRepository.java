package jejaChurch.jeja.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jejaChurch.jeja.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 해당 스테이지에서 해당 조가 제출했는지 확인
    boolean existsByStageNumberAndTeamNumber(int stageNumber, int teamNumber);

    // 🆕 문제 선택 여부 확인
    boolean existsByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(int stageNumber, int teamNumber);

    // 🆕 선택된 문제 정보 가져오기
    Optional<Quiz> findByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(int stageNumber, int teamNumber);

    // 특정 스테이지의 제출 현황 (팀 번호 순)
    List<Quiz> findByStageNumberOrderByTeamNumber(int stageNumber);

    List<Quiz> findByTeamNumberOrderByIdDesc(Integer teamNumber); // id로 정렬

    List<Quiz> findAllByOrderByIdDesc(); // id로 정렬
}
