package jejaChurch.jeja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jejaChurch.jeja.entity.Quiz;
import jejaChurch.jeja.service.QuizService;
import jejaChurch.jeja.service.TeamService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InsideController {

    private final QuizService quizService;
    private final TeamService teamService;

    // 조 선택 페이지
    @GetMapping("/main{stageNumber}")
    public String homePage(@PathVariable int stageNumber, Model model) {
        model.addAttribute("stageNumber", stageNumber);
        return "inside/main" + stageNumber;
    }

    // 패스워드 입력 페이지
    @GetMapping("/team{stageNumber}/{teamNumber}")
    public String passwordPage(@PathVariable int stageNumber, @PathVariable int teamNumber, Model model) {
        model.addAttribute("stageNumber", stageNumber);
        model.addAttribute("teamNumber", teamNumber);
        return "inside/password" + stageNumber;
    }

    // 비밀번호 검증
    @PostMapping("/team{stageNumber}/{teamNumber}/verify")
    public String verifyPassword(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @RequestParam String password,
            Model model) {

        if (!teamService.verifyPassword(teamNumber, password)) {
            model.addAttribute("stageNumber", stageNumber);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "inside/password" + stageNumber;
        }

        // 🔄 전체 스테이지 기준으로 팀 상태 확인
        String teamStatus = quizService.getTeamStatusGlobal(teamNumber);

        switch (teamStatus) {
            case "COMPLETED":
                // 🆕 이미 다른 스테이지에서 완료했다면
                Quiz completedQuiz = quizService.getTeamSelectedQuiz(teamNumber);
                model.addAttribute("stageNumber", completedQuiz.getStageNumber());
                model.addAttribute("teamNumber", teamNumber);
                return "inside/already-submitted" + completedQuiz.getStageNumber();

            case "IN_PROGRESS":
                // 🆕 다른 스테이지에서 진행 중이라면 해당 스테이지로 리다이렉트
                Quiz inProgressQuiz = quizService.getTeamSelectedQuiz(teamNumber);
                return "redirect:/team" + inProgressQuiz.getStageNumber() + "/" + teamNumber + "/quiz/"
                        + inProgressQuiz.getQuestionNumber();

            default: // NOT_STARTED
                // 🆕 현재 스테이지에서 새로 시작
                return "redirect:/team" + stageNumber + "/" + teamNumber + "/select";
        }
    }

    // 문제 선택 페이지
    @GetMapping("/team{stageNumber}/{teamNumber}/select")
    public String questionSelectPage(@PathVariable int stageNumber, @PathVariable int teamNumber, Model model) {

        // 이미 답안을 완전히 제출했는지 확인
        if (quizService.hasTeamSubmittedInStage(stageNumber, teamNumber)) {
            return "inside/already-submitted" + stageNumber;
        }

        // 🆕 이미 문제를 선택했다면 해당 문제로 강제 리다이렉트
        if (quizService.hasTeamSelectedQuestionInStage(stageNumber, teamNumber)) {
            Integer selectedQuestionNumber = quizService.getSelectedQuestionNumber(stageNumber, teamNumber);
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/quiz/" + selectedQuestionNumber;
        }

        model.addAttribute("stageNumber", stageNumber);
        model.addAttribute("teamNumber", teamNumber);
        return "inside/question" + stageNumber;
    }

    // 문제 선택 처리 - 즉시 DB에 저장
    @PostMapping("/team{stageNumber}/{teamNumber}/select")
    public String selectQuestion(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @RequestParam int questionNumber,
            Model model) {

        try {
            // 🆕 문제 선택 즉시 DB에 기록
            quizService.recordQuestionSelection(stageNumber, teamNumber, questionNumber);
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/quiz/" + questionNumber;

        } catch (IllegalStateException e) {
            // 이미 선택한 경우 기존 선택한 문제로 리다이렉트
            Integer selectedQuestionNumber = quizService.getSelectedQuestionNumber(stageNumber, teamNumber);
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/quiz/" + selectedQuestionNumber;
        }
    }

    // 문제 페이지
    @GetMapping("/team{stageNumber}/{teamNumber}/quiz/{questionNumber}")
    public String quizPage(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @PathVariable int questionNumber,
            Model model) {

        // 이미 답안을 완전히 제출했는지 확인
        if (quizService.hasTeamSubmittedInStage(stageNumber, teamNumber)) {
            return "inside/already-submitted" + stageNumber;
        }

        // 🆕 선택한 문제와 일치하는지 확인
        Integer selectedQuestionNumber = quizService.getSelectedQuestionNumber(stageNumber, teamNumber);

        if (selectedQuestionNumber == null) {
            // 문제를 선택하지 않았다면 선택 페이지로 리다이렉트
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/select";
        }

        if (!selectedQuestionNumber.equals(questionNumber)) {
            // 🚨 다른 문제에 접근하려고 하면 선택한 문제로 강제 리다이렉트
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/quiz/" + selectedQuestionNumber;
        }

        model.addAttribute("stageNumber", stageNumber);
        model.addAttribute("teamNumber", teamNumber);
        model.addAttribute("questionNumber", questionNumber);

        return "inside/quiz" + stageNumber;
    }

    // 답안 제출 확인 페이지로 이동
    @PostMapping("/team{stageNumber}/{teamNumber}/quiz/{questionNumber}")
    public String submitToConfirm(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @PathVariable int questionNumber,
            @RequestParam(required = false) String answer,
            Model model) {

        // 이미 답안을 완전히 제출했는지 확인
        if (quizService.hasTeamSubmittedInStage(stageNumber, teamNumber)) {
            return "inside/already-submitted" + stageNumber;
        }

        // 🆕 선택한 문제와 일치하는지 확인
        Integer selectedQuestionNumber = quizService.getSelectedQuestionNumber(stageNumber, teamNumber);
        if (!selectedQuestionNumber.equals(questionNumber)) {
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/quiz/" + selectedQuestionNumber;
        }

        model.addAttribute("stageNumber", stageNumber);
        model.addAttribute("teamNumber", teamNumber);
        model.addAttribute("questionNumber", questionNumber);
        model.addAttribute("answer", answer);

        return "inside/confirm" + stageNumber;
    }

    // 답안 제출 확인 페이지
    @GetMapping("/team{stageNumber}/{teamNumber}/confirm/{questionNumber}")
    public String confirmPage(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @PathVariable int questionNumber,
            @RequestParam String answer,
            Model model) {

        model.addAttribute("stageNumber", stageNumber);
        model.addAttribute("teamNumber", teamNumber);
        model.addAttribute("questionNumber", questionNumber);
        model.addAttribute("answer", answer);

        return "inside/confirm" + stageNumber;
    }

    // 최종 답안 제출
    @PostMapping("/team{stageNumber}/{teamNumber}/submit")
    public String submitAnswer(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @RequestParam int questionNumber,
            @RequestParam String answer,
            Model model) {

        try {
            quizService.saveAnswer(stageNumber, teamNumber, questionNumber, answer);

            model.addAttribute("stageNumber", stageNumber);
            model.addAttribute("teamNumber", teamNumber);
            return "inside/complete" + stageNumber;

        } catch (IllegalStateException e) {
            // 🚨 중복 제출 시 예외 처리 추가
            model.addAttribute("stageNumber", stageNumber);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", e.getMessage());
            return "inside/already-submitted" + stageNumber;
        }
    }
}
