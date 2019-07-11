package com.platform.controller;

import com.platform.service.PlatformService;
import io.micrometer.core.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class PlatformController implements IPlatformController {
    @Autowired
    PlatformService platformService;

    @Override
    public Map<String, ArrayList<String>> validMoves() {
        return Collections.singletonMap("valid-moves", platformService.validMoves());
    }

    @Override
    public Map<String, String> applyNextMove(@NonNull String gameId, @NonNull String nextMove) {
        return Collections.singletonMap("current-position", platformService.applyNextMove(gameId, nextMove));
    }

    @Override
    public Map<String, String> startNewGame(Integer row, Integer column) {
        return Collections.singletonMap("game-id", platformService.startNewGame(row, column));
    }

    @Override
    public Map<String, String> endGame(@NonNull String gameId) {
        if(platformService.finishGame(gameId)) {
            return Collections.singletonMap("removed-game-id",  gameId);
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> getCurrentValue(@NonNull String gameId) {
        return Collections.singletonMap("current-value", platformService.getCurrentValue(gameId));
    }

    @Override
    public Map<String, ArrayList<String>> getPath(@NonNull String gameId) {
        return Collections.singletonMap("game-path", platformService.getPath(gameId));
    }

    @Override
    public Map<String, ArrayList<String>> getUniquePath(@NonNull String gameId) {
        return Collections.singletonMap("unique-path", platformService.generatePath(gameId, platformService.getPath(gameId).size()));
    }
}
