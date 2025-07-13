package jejaChurch.jeja.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jejaChurch.jeja.entity.Team;
import jejaChurch.jeja.repository.TeamRepository;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public boolean verifyPassword(Integer teamNumber, String password) {
        Optional<Team> team = teamRepository.findByTeamNumber(teamNumber);
        return team.isPresent() && team.get().getPassword().equals(password);
    }

    public Optional<Team> getTeamByNumber(Integer teamNumber) {
        return teamRepository.findByTeamNumber(teamNumber);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }
}
