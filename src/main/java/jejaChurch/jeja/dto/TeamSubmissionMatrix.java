package jejaChurch.jeja.dto;

import lombok.Data;

@Data
public class TeamSubmissionMatrix {
    private String teamName;
    private int teamNumber;
    private StageSubmission stage1;
    private StageSubmission stage2;
    private StageSubmission stage3;
    private StageSubmission stage4;

    public void setStageSubmission(int stageNumber, StageSubmission submission) {
        switch (stageNumber) {
            case 1:
                this.stage1 = submission;
                break;
            case 2:
                this.stage2 = submission;
                break;
            case 3:
                this.stage3 = submission;
                break;
            case 4:
                this.stage4 = submission;
                break;
        }
    }
}
