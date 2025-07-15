package jejaChurch.jeja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jejaChurch.jeja.service.PlaceService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    // 이미지명으로 구분되는 장소 페이지 (기존 유지)
    @GetMapping("/place/{imageName}")
    public String showPlaceImage(@PathVariable String imageName, Model model) {
        if (!imageName.matches("^[a-zA-Z0-9가-힣_-]+$")) {
            return "redirect:/place/default";
        }

        model.addAttribute("imageName", imageName);
        model.addAttribute("imageUrl", "/images/" + imageName + ".JPG");

        return "place/placeImage";
    }

    // 기본 장소 페이지 (기존 유지)
    @GetMapping("/place/default")
    public String defaultPlace(Model model) {
        model.addAttribute("imageName", "default");
        model.addAttribute("imageUrl", "/images/default.JPG");
        return "place/placeImage";
    }

    // 1번 사업장 QR코드 스캔 시 → 조 선택 페이지로
    @GetMapping("/place1")
    public String place1ToMain(Model model) {
        return "redirect:/place/main1";
    }

    // 2번 사업장 QR코드 스캔 시 → 조 선택 페이지로
    @GetMapping("/place2")
    public String place2ToMain(Model model) {
        return "redirect:/place/main2";
    }

    // 3번 사업장 QR코드 스캔 시 → 조 선택 페이지로
    @GetMapping("/place3")
    public String place3ToMain(Model model) {
        return "redirect:/place/main3";
    }

    // 4번 사업장 QR코드 스캔 시 → 조 선택 페이지로
    @GetMapping("/place4")
    public String place4ToMain(Model model) {
        return "redirect:/place/main4";
    }

    // 1번 사업장 조 선택 페이지
    @GetMapping("/main1")
    public String main1(Model model) {
        model.addAttribute("stageNumber", 1);
        return "place/main1";
    }

    // 2번 사업장 조 선택 페이지
    @GetMapping("/main2")
    public String main2(Model model) {
        model.addAttribute("stageNumber", 2);
        return "place/main2";
    }

    // 3번 사업장 조 선택 페이지
    @GetMapping("/main3")
    public String main3(Model model) {
        model.addAttribute("stageNumber", 3);
        return "place/main3";
    }

    // 4번 사업장 조 선택 페이지
    @GetMapping("/main4")
    public String main4(Model model) {
        model.addAttribute("stageNumber", 4);
        return "place/main4";
    }

    // 1번 사업장 비밀번호 입력 페이지
    @GetMapping("/team1/{teamNumber}")
    public String password1(@PathVariable int teamNumber, Model model) {
        if (!placeService.teamExists(teamNumber)) {
            return "error";
        }

        model.addAttribute("stageNumber", 1);
        model.addAttribute("teamNumber", teamNumber);
        return "place/password1";
    }

    // 2번 사업장 비밀번호 입력 페이지
    @GetMapping("/team2/{teamNumber}")
    public String password2(@PathVariable int teamNumber, Model model) {
        if (!placeService.teamExists(teamNumber)) {
            return "error";
        }

        model.addAttribute("stageNumber", 2);
        model.addAttribute("teamNumber", teamNumber);
        return "place/password2";
    }

    // 3번 사업장 비밀번호 입력 페이지
    @GetMapping("/team3/{teamNumber}")
    public String password3(@PathVariable int teamNumber, Model model) {
        if (!placeService.teamExists(teamNumber)) {
            return "error";
        }

        model.addAttribute("stageNumber", 3);
        model.addAttribute("teamNumber", teamNumber);
        return "place/password3";
    }

    // 4번 사업장 비밀번호 입력 페이지
    @GetMapping("/team4/{teamNumber}")
    public String password4(@PathVariable int teamNumber, Model model) {
        if (!placeService.teamExists(teamNumber)) {
            return "error";
        }

        model.addAttribute("stageNumber", 4);
        model.addAttribute("teamNumber", teamNumber);
        return "place/password4";
    }

    // 1번 사업장 비밀번호 검증 후 해당 조 전용 페이지
    @PostMapping("/team1/{teamNumber}/verify")
    public String verifyPassword1(@PathVariable int teamNumber, @RequestParam String password, Model model) {
        if (!placeService.validateTeamPassword(teamNumber, password)) {
            model.addAttribute("stageNumber", 1);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "place/password1";
        }

        model.addAttribute("stageNumber", 1);
        model.addAttribute("teamNumber", teamNumber);

        return "place/nextPlace1";
    }

    // 2번 사업장 비밀번호 검증 후 해당 조 전용 페이지
    @PostMapping("/team2/{teamNumber}/verify")
    public String verifyPassword2(@PathVariable int teamNumber, @RequestParam String password, Model model) {
        if (!placeService.validateTeamPassword(teamNumber, password)) {
            model.addAttribute("stageNumber", 2);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "place/password2";
        }

        model.addAttribute("stageNumber", 2);
        model.addAttribute("teamNumber", teamNumber);

        return "place/nextPlace2";
    }

    // 3번 사업장 비밀번호 검증 후 해당 조 전용 페이지
    @PostMapping("/team3/{teamNumber}/verify")
    public String verifyPassword3(@PathVariable int teamNumber, @RequestParam String password, Model model) {
        if (!placeService.validateTeamPassword(teamNumber, password)) {
            model.addAttribute("stageNumber", 3);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "place/password3";
        }

        model.addAttribute("stageNumber", 3);
        model.addAttribute("teamNumber", teamNumber);

        return "place/nextPlace3";
    }

    // 4번 사업장 비밀번호 검증 후 해당 조 전용 페이지
    @PostMapping("/team4/{teamNumber}/verify")
    public String verifyPassword4(@PathVariable int teamNumber, @RequestParam String password, Model model) {
        if (!placeService.validateTeamPassword(teamNumber, password)) {
            model.addAttribute("stageNumber", 4);
            model.addAttribute("teamNumber", teamNumber);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "place/password4";
        }

        model.addAttribute("stageNumber", 4);
        model.addAttribute("teamNumber", teamNumber);

        return "place/nextPlace4";
    }
}
