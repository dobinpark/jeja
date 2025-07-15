package jejaChurch.jeja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlaceController {

    // 이미지명으로 구분되는 장소 페이지
    @GetMapping("/place/{imageName}")
    public String showPlaceImage(@PathVariable String imageName, Model model) {

        // 이미지명 유효성 검사 (영문, 숫자, 한글, 하이픈, 언더스코어만 허용)
        if (!imageName.matches("^[a-zA-Z0-9가-힣_-]+$")) {
            return "redirect:/place/default"; // 잘못된 이미지명이면 기본 이미지로
        }

        model.addAttribute("imageName", imageName);
        model.addAttribute("imageUrl", "/images/" + imageName + ".jpg");

        return "place/placeImage";
    }

    // 기본 장소 페이지 (fallback)
    @GetMapping("/place/default")
    public String defaultPlace(Model model) {
        model.addAttribute("imageName", "default");
        model.addAttribute("imageUrl", "/images/default.jpg");
        return "place/placeImage";
    }
}
