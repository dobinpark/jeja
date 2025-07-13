package jejaChurch.jeja.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jejaChurch.jeja.entity.Quiz;
import jejaChurch.jeja.repository.QuizRepository;
import jejaChurch.jeja.dto.TeamSubmissionMatrix;
import jejaChurch.jeja.dto.StageSubmission;
import jejaChurch.jeja.dto.StageDetailSubmission;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final QuizRepository quizRepository;

    public List<TeamSubmissionMatrix> getTeamSubmissionMatrix() {
        try {
            List<Quiz> allSubmissions = quizRepository.findAllByOrderByIdDesc();

            if (allSubmissions == null) {
                allSubmissions = new ArrayList<>();
            }

            Map<Integer, List<Quiz>> submissionsByTeam = allSubmissions.stream()
                    .collect(Collectors.groupingBy(Quiz::getTeamNumber));

            List<TeamSubmissionMatrix> matrix = new ArrayList<>();

            // 1조부터 5조까지 처리
            for (int teamNumber = 1; teamNumber <= 5; teamNumber++) {
                TeamSubmissionMatrix teamMatrix = new TeamSubmissionMatrix();
                teamMatrix.setTeamName(teamNumber + "조");
                teamMatrix.setTeamNumber(teamNumber);

                List<Quiz> teamSubmissions = submissionsByTeam.getOrDefault(teamNumber, new ArrayList<>());

                // 스테이지별 답안 설정
                for (int stage = 1; stage <= 4; stage++) {
                    final int stageNum = stage;
                    Optional<Quiz> stageSubmission = teamSubmissions.stream()
                            .filter(quiz -> quiz.getStageNumber() == stageNum)
                            .findFirst();

                    if (stageSubmission.isPresent()) {
                        Quiz quiz = stageSubmission.get();
                        StageSubmission submission = new StageSubmission();
                        submission.setQuestionNumber(quiz.getQuestionNumber());
                        submission.setAnswer(quiz.getAnswer());

                        // null 체크 추가
                        if (quiz.getSubmittedAt() != null) {
                            submission.setSubmittedAt(quiz.getSubmittedAt());
                        }

                        submission.setCompleted(true);
                        teamMatrix.setStageSubmission(stage, submission);
                    } else {
                        // 미제출 상태
                        StageSubmission submission = new StageSubmission();
                        submission.setCompleted(false);
                        teamMatrix.setStageSubmission(stage, submission);
                    }
                }

                matrix.add(teamMatrix);
            }

            return matrix;

        } catch (Exception e) {
            System.err.println("getTeamSubmissionMatrix 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<StageDetailSubmission> getSubmissionsByStage(int stageNumber) {
        try {
            List<Quiz> stageSubmissions = quizRepository.findByStageNumberOrderByTeamNumber(stageNumber);

            if (stageSubmissions == null) {
                return new ArrayList<>();
            }

            return stageSubmissions.stream()
                    .map(quiz -> {
                        StageDetailSubmission detail = new StageDetailSubmission();
                        detail.setTeamName(quiz.getTeamNumber() + "조");
                        detail.setQuestionNumber(quiz.getQuestionNumber());
                        detail.setAnswer(quiz.getAnswer());

                        if (quiz.getSubmittedAt() != null) {
                            detail.setSubmittedAt(quiz.getSubmittedAt());
                        }

                        return detail;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("getSubmissionsByStage 오류: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
