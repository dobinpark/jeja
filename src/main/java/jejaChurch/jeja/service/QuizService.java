package jejaChurch.jeja.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jejaChurch.jeja.entity.Quiz;
import jejaChurch.jeja.repository.QuizRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    // 해당 조가 해당 스테이지에서 이미 제출했는지 확인
    public boolean hasTeamSubmittedInStage(int stageNumber, int teamNumber) {
        return quizRepository.existsByStageNumberAndTeamNumber(stageNumber, teamNumber);
    }

    public Quiz saveAnswer(int stageNumber, int teamNumber, int questionNumber, String answer) {

        // 이미 제출한 조인지 확인
        if (hasTeamSubmittedInStage(stageNumber, teamNumber)) {
            throw new IllegalStateException("이미 제출하였습니다.");
        }

        Quiz quiz = new Quiz();
        quiz.setStageNumber(stageNumber);
        quiz.setTeamNumber(teamNumber);
        quiz.setQuestionNumber(questionNumber);
        quiz.setAnswer(answer);

        return quizRepository.save(quiz);
    }

    public List<Quiz> getAnswersByTeam(Integer teamNumber) {
        return quizRepository.findByTeamNumberOrderByIdDesc(teamNumber);
    }

    public List<Quiz> getAllAnswers() {
        return quizRepository.findAllByOrderByIdDesc();
    }
}
