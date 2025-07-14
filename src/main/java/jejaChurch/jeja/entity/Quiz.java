package jejaChurch.jeja.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "quiz_answers")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stageNumber; // 스테이지 번호
    private int teamNumber; // 조 번호
    private int questionNumber; // 문제 번호
    private String answer; // 제출한 답안
    private LocalDateTime submittedAt;

    // 🆕 문제 선택 시점 기록
    private LocalDateTime questionSelectedAt;

    // 🆕 답안 제출 완료 여부
    private boolean isAnswerSubmitted = false;

    @PrePersist
    protected void onCreate() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }
}