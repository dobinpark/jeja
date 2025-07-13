package jejaChurch.jeja.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class StageDetailSubmission {
    private String teamName;
    private int questionNumber;
    private String answer;
    private LocalDateTime submittedAt;
}
