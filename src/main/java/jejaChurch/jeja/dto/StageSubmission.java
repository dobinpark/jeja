package jejaChurch.jeja.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class StageSubmission {
    private boolean completed;
    private int questionNumber;
    private String answer;
    private LocalDateTime submittedAt;
}
