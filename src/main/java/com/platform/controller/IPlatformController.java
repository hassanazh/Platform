package com.platform.controller;

import io.micrometer.core.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Map;

@RequestMapping(path = "/platform")
public interface IPlatformController {
    @GetMapping(value = "valid-moves", produces = "application/json")
    Map<String, ArrayList<String>> validMoves();

    @PostMapping(value = "new-game/{row}/{column}", produces = "application/json")
    Map<String, String> startNewGame(@PathVariable("row") @NonNull Integer row,@PathVariable("column") @NonNull  Integer column);

    @PostMapping(value = "{gameId}/next-move/{nextMove}", produces = "application/json")
    Map<String, String> applyNextMove(@PathVariable("gameId") @NonNull String sessionId, @PathVariable("nextMove") @NonNull String nextMove);

    @GetMapping(value = "{gameId}/current-position", produces = "application/json")
    Map<String, String> getCurrentValue(@PathVariable("gameId") @NonNull String gameId);

    @GetMapping(value = "{gameId}/path", produces = "application/json")
    Map<String, ArrayList<String>> getPath(@PathVariable("gameId") @NonNull String gameId);

    @PostMapping(value = "{gameId}/unique-path", produces = "application/json")
    Map<String, ArrayList<String>> getUniquePath(@PathVariable("gameId") @NonNull String gameId);

    @PostMapping(value = "{gameId}/end-game", produces = "application/json")
    Map<String, String> endGame(@PathVariable("gameId") @NonNull String gameId);
}
