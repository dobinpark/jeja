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

    private int stageNumber;
    private int teamNumber;
    private int questionNumber;
    private String answer;
    private LocalDateTime submittedAt;

    // 🆕 새로 추가된 필드들
    private LocalDateTime questionSelectedAt;
    private boolean isAnswerSubmitted = false;

    @PrePersist
    protected void onCreate() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }
}