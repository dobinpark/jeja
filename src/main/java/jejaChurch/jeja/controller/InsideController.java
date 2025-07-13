package jejaChurch.jeja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // 패스워드 검증
    @PostMapping("/team{stageNumber}/{teamNumber}/verify")
    public String verifyPassword(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @RequestParam String password,
            Model model) {

        if (teamService.verifyPassword(teamNumber, password)) {
            return "redirect:/team" + stageNumber + "/" + teamNumber + "/select";
        } else {
            model.addAttribute("stageNumber", stageNumber);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "inside/password" + stageNumber;
        }
    }

    // 제출 여부 확인 메서드 추가
    private boolean checkIfAlreadySubmitted(int stageNumber, int teamNumber, Model model) {
        if (quizService.hasTeamSubmittedInStage(stageNumber, teamNumber)) {
            model.addAttribute("stageNumber", stageNumber);
            model.addAttribute("teamNumber", teamNumber);
            return true;
        }
        return false;
    }

    // 문제 선택 페이지
    @GetMapping("/team{stageNumber}/{teamNumber}/select")
    public String questionSelectPage(@PathVariable int stageNumber, @PathVariable int teamNumber, Model model) {

        // 🚨 이미 제출한 조인지 확인 추가
        if (checkIfAlreadySubmitted(stageNumber, teamNumber, model)) {
            return "inside/already-submitted" + stageNumber;
        }

        model.addAttribute("stageNumber", stageNumber);
        model.addAttribute("teamNumber", teamNumber);
        return "inside/question" + stageNumber;
    }

    // 선택한 문제 페이지로 이동
    @PostMapping("/team{stageNumber}/{teamNumber}/select")
    public String selectQuestion(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @RequestParam int questionNumber,
            Model model) {

        return "redirect:/team" + stageNumber + "/" + teamNumber + "/quiz/" + questionNumber;
    }

    // 문제 페이지
    @GetMapping("/team{stageNumber}/{teamNumber}/quiz/{questionNumber}")
    public String quizPage(
            @PathVariable int stageNumber,
            @PathVariable int teamNumber,
            @PathVariable int questionNumber,
            Model model) {

        // 🚨 이미 제출한 조인지 확인 추가
        if (checkIfAlreadySubmitted(stageNumber, teamNumber, model)) {
            return "inside/already-submitted" + stageNumber;
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

        // 🚨 이미 제출한 조인지 확인 추가
        if (checkIfAlreadySubmitted(stageNumber, teamNumber, model)) {
            return "inside/already-submitted" + stageNumber;
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
