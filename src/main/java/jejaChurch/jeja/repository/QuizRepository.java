package jejaChurch.jeja.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jejaChurch.jeja.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 🔄 해당 조가 답안을 제출했는지 확인 (스테이지 무관)
    boolean existsByTeamNumberAndIsAnswerSubmittedTrue(int teamNumber);

    // 🔄 해당 조가 문제를 선택했는지 확인 (스테이지 무관)
    boolean existsByTeamNumberAndQuestionSelectedAtIsNotNull(int teamNumber);

    // 🔄 해당 조가 선택한 문제 정보 가져오기 (스테이지 무관) - 최신 것 하나만
    Optional<Quiz> findFirstByTeamNumberAndQuestionSelectedAtIsNotNullOrderByIdDesc(int teamNumber);

    // 해당 스테이지에서 해당 조가 제출했는지 확인 (기존 방식과 호환)
    boolean existsByStageNumberAndTeamNumber(int stageNumber, int teamNumber);

    // 🔄 스테이지별 문제 선택 여부 확인
    boolean existsByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(int stageNumber, int teamNumber);

    // 🔄 스테이지별 선택된 문제 정보 가져오기
    Optional<Quiz> findByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(int stageNumber, int teamNumber);

    // 특정 스테이지의 제출 현황 (팀 번호 순)
    List<Quiz> findByStageNumberOrderByTeamNumber(int stageNumber);

    List<Quiz> findByTeamNumberOrderByIdDesc(Integer teamNumber);

    List<Quiz> findAllByOrderByIdDesc();
}
