package jejaChurch.jeja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jejaChurch.jeja.service.AdminService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 어드민 메인 페이지
    @GetMapping
    public String adminMain(Model model) {
        try {
            model.addAttribute("teamSubmissions", adminService.getTeamSubmissionMatrix());
            return "admin/dashboard";
        } catch (Exception e) {
            // 오류 발생 시 로그 출력 및 빈 데이터로 처리
            System.err.println("어드민 대시보드 오류: " + e.getMessage());
            model.addAttribute("teamSubmissions", new java.util.ArrayList<>());
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            return "admin/dashboard";
        }
    }

    // 스테이지별 상세 보기
    @GetMapping("/stage/{stageNumber}")
    public String stageDetail(@PathVariable int stageNumber, Model model) {
        // 스테이지 번호 유효성 검사
        if (stageNumber < 1 || stageNumber > 4) {
            model.addAttribute("error", "유효하지 않은 스테이지 번호입니다.");
            return "redirect:/admin";
        }

        try {
            model.addAttribute("stageNumber", stageNumber);
            model.addAttribute("submissions", adminService.getSubmissionsByStage(stageNumber));
            return "admin/stage-detail";
        } catch (Exception e) {
            System.err.println("스테이지 상세 페이지 오류: " + e.getMessage());
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/admin";
        }
    }
}
