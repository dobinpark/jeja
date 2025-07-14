package jejaChurch.jeja.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jejaChurch.jeja.entity.Quiz;
import jejaChurch.jeja.repository.QuizRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    // 🆕 문제 선택 여부 확인
    public boolean hasTeamSelectedQuestionInStage(int stageNumber, int teamNumber) {
        return quizRepository.existsByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(stageNumber, teamNumber);
    }

    // 🆕 선택된 문제 번호 가져오기
    public Integer getSelectedQuestionNumber(int stageNumber, int teamNumber) {
        return quizRepository.findByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(stageNumber, teamNumber)
                .map(Quiz::getQuestionNumber)
                .orElse(null);
    }

    // 🆕 문제 선택 즉시 기록 (핵심 메서드)
    public void recordQuestionSelection(int stageNumber, int teamNumber, int questionNumber) {
        // 이미 문제를 선택했는지 확인
        if (hasTeamSelectedQuestionInStage(stageNumber, teamNumber)) {
            throw new IllegalStateException("이미 문제를 선택하였습니다.");
        }

        Quiz quiz = new Quiz();
        quiz.setStageNumber(stageNumber);
        quiz.setTeamNumber(teamNumber);
        quiz.setQuestionNumber(questionNumber);
        quiz.setQuestionSelectedAt(LocalDateTime.now());
        quiz.setAnswerSubmitted(false);

        quizRepository.save(quiz);
    }

    // 기존 답안 제출 여부 확인 (완전히 제출 완료된 경우)
    public boolean hasTeamSubmittedInStage(int stageNumber, int teamNumber) {
        Optional<Quiz> quiz = quizRepository.findByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(stageNumber,
                teamNumber);
        return quiz.isPresent() && quiz.get().isAnswerSubmitted();
    }

    public Quiz saveAnswer(int stageNumber, int teamNumber, int questionNumber, String answer) {
        // 문제 선택 기록 찾기
        Optional<Quiz> existingQuiz = quizRepository
                .findByStageNumberAndTeamNumberAndQuestionSelectedAtIsNotNull(stageNumber, teamNumber);

        if (existingQuiz.isEmpty()) {
            throw new IllegalStateException("먼저 문제를 선택해야 합니다.");
        }

        Quiz quiz = existingQuiz.get();

        // 이미 답안을 제출했는지 확인
        if (quiz.isAnswerSubmitted()) {
            throw new IllegalStateException("이미 제출하였습니다.");
        }

        // 선택한 문제와 제출하려는 문제가 같은지 확인
        if (quiz.getQuestionNumber() != questionNumber) {
            throw new IllegalStateException("선택한 문제와 다른 문제의 답안을 제출할 수 없습니다.");
        }

        // 답안 업데이트
        quiz.setAnswer(answer);
        quiz.setAnswerSubmitted(true);
        quiz.setSubmittedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public List<Quiz> getAnswersByTeam(Integer teamNumber) {
        return quizRepository.findByTeamNumberOrderByIdDesc(teamNumber);
    }

    public List<Quiz> getAllAnswers() {
        return quizRepository.findAllByOrderByIdDesc();
    }
}
