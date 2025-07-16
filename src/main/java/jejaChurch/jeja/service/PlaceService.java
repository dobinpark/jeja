package jejaChurch.jeja.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jejaChurch.jeja.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final TeamRepository teamRepository;

    // 팀별 현재 진행 단계를 메모리에 저장
    private final Map<Integer, Integer> teamProgress = new HashMap<>();

    // 팀 비밀번호 확인
    public boolean validateTeamPassword(int teamNumber, String password) {
        return teamRepository.findByTeamNumber(teamNumber)
                .map(team -> team.getPassword().equals(password))
                .orElse(false);
    }

    // 팀 존재 여부 확인
    public boolean teamExists(int teamNumber) {
        return teamRepository.findByTeamNumber(teamNumber).isPresent();
    }

    // 현재 단계 가져오기
    public int getCurrentStep(int teamNumber) {
        return teamProgress.getOrDefault(teamNumber, 0);
    }
}
