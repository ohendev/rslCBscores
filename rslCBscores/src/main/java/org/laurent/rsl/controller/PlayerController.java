package org.laurent.rsl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.laurent.rsl.model.Player;
import org.laurent.rsl.model.StatPlayer;
import org.laurent.rsl.service.PlayerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bdc/v1")
public class PlayerController {

    private final PlayerService playerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PlayerController(PlayerService playerService, ObjectMapper objectMapper) {
        this.playerService = playerService;
        this.objectMapper = objectMapper;
    }



    @PostMapping("/player/update")
    public ResponseEntity<Player> updateOnePlayer(@RequestParam(name = "name") String name,
                                                  @RequestParam(name = "bdc") String bdc,
                                                  @RequestParam(name = "damage") Double damages) {


        Player player = playerService.updatePlayer(name, bdc, damages);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/players/save")
    public ResponseEntity<List<Player>> saveManyPlayers(@RequestBody List<Player> players) {
        List<Player> actualPlayers = playerService.saveManyPlayers(players);
        return new ResponseEntity<>(actualPlayers, HttpStatus.OK);
    }

    @PostMapping("/player/save")
    public ResponseEntity<Player> saveOnePlayer(@RequestParam(name = "name") String name,
                                                @RequestParam(name = "bdc") String bdc,
                                                @RequestParam(name = "damage") Double damages) {
        Player player = playerService.saveOnePlayer(name, bdc, damages);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/player/{name}")
    public ResponseEntity<Player> getOnePlayer(@PathVariable(name = "name") String name) {
        Player player = playerService.findByName(name);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayer() {
        List<Player> players = playerService.findAll();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("stats/{diff}/{name}")
    public ResponseEntity<StatPlayer> getOnePlayerStats(@PathVariable(name = "diff")String bdc, @PathVariable(name = "name")String name) {
        return new ResponseEntity<>(playerService.getOnePlayerStats(name, bdc),
                HttpStatus.OK);
    }

    @GetMapping("stats")
    public ResponseEntity<Double> getOnePlayerStats(@RequestParam(name = "name")String name,
                                                    @RequestParam(name = "start") String start,
                                                    @RequestParam(name = "end") String end) {
        LocalDateTime startDate = objectMapper.convertValue(start, LocalDateTime.class);
        LocalDateTime endDate = objectMapper.convertValue(end, LocalDateTime.class);
        return new ResponseEntity<>(playerService.getPercentageOfProgression(name, startDate, endDate),
                HttpStatus.OK);
    }
}

