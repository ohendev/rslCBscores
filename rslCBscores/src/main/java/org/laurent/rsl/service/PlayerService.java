package org.laurent.rsl.service;

import org.laurent.rsl.model.BDCResult;
import org.laurent.rsl.model.Difficulty;
import org.laurent.rsl.model.Player;
import org.laurent.rsl.model.StatPlayer;
import org.laurent.rsl.repository.PlayerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {

    private final PlayerRepo playerRepo;
    private final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    public PlayerService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public List<Player> saveManyPlayers(List<Player> players) {
        playerRepo.saveAll(players);
        List<Player> actualPlayers = playerRepo.findAll();
        return actualPlayers;
    }

    public Player findByName(String name) {
        return playerRepo.findByName(name);
    }

    public Player updatePlayer(String name, String bdc, Double damage) {
        Difficulty difficulty = Difficulty.valueOf(bdc);
        BDCResult newResult = BDCResult.builder()
                .difficulty(difficulty)
                .datetime(LocalDateTime.now())
                .score(damage)
                .build();
        Player playerToSave = playerRepo.findByName(name);
        List<BDCResult> newResults = playerToSave.getResults();
        newResults.add(newResult);
        playerToSave.setResults(newResults);
        playerRepo.saveAndFlush(playerToSave);
        return playerToSave;
    }

    public List<Player> findAll() {
        return playerRepo.findAll();
    }

    public StatPlayer getOnePlayerStats(String name, String bdc) {
        Player player = playerRepo.findByName(name);
        Difficulty difficulty = Difficulty.valueOf(bdc);
        StatPlayer statPlayer = new StatPlayer();
        statPlayer.setName(name);
        Map<String, Double> damages = new HashMap<>();
        List<BDCResult> results = player.getResults();
        Double averageDamages = results.stream()
                .filter(r -> r.getDifficulty().equals(difficulty))
                .mapToDouble(BDCResult::getScore).average().getAsDouble();
        damages.put(bdc, averageDamages);
        statPlayer.setAverageDamages(damages);
        statPlayer.setDamagesHistory(player.getResults());
        return statPlayer;
    }

    public Double getPercentageOfProgression(String name, LocalDateTime start, LocalDateTime end) {
        Player player = playerRepo.findByName(name);
        List<BDCResult> results = player.getResults();
        Double averageStart = results.stream()
                .filter(bdcResult -> (bdcResult.getDatetime().isEqual(start))) // filter BDCResults by start date
                .mapToDouble(BDCResult::getScore) // map BDCResults to a DoubleStream of the scores
                .summaryStatistics().getAverage(); // reduce the stream to the average score
        LOGGER.info("Average damages on {} is {}", start, averageStart);
        Double averageEnd = results.stream()
                .filter(bdcResult -> (bdcResult.getDatetime().isEqual(end))) // filter BDCResults by end date
                .mapToDouble(BDCResult::getScore)
                .summaryStatistics().getAverage();
        LOGGER.info("Average damages on {} is {}", end, averageEnd);
        Double percentage = ((averageEnd - averageStart) / averageStart) * 100;
        LOGGER.info("Percentage of Progression is: {}", percentage);
        return percentage;
    }

    public Player saveOnePlayer(String name, String bdc, Double damages) {
        Difficulty difficulty = Difficulty.valueOf(bdc);
        BDCResult result = BDCResult.builder()
                .score(damages)
                .difficulty(difficulty)
                .datetime(LocalDateTime.now()).build();
        List<BDCResult> results = new ArrayList<>();
        results.add(result);
        Player player = Player.builder()
                .name(name)
                .results(results)
                .build();
        playerRepo.save(player);
        return player;
    }

    public boolean deletePlayerById(Long id) {
        Player player = playerRepo.getOne(id);
        playerRepo.delete(player);
        return !playerRepo.existsById(id);
    }
}


