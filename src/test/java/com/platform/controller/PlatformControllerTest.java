package com.platform.controller;

import com.platform.service.PlatformService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PlatformController.class)
@ActiveProfiles("test")
public class PlatformControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlatformService platformService;

    @Test
    public void shouldReturn200OnCreateNewGameReturnsGameId() throws Exception {

        when(platformService.startNewGame(any(Integer.class), any(Integer.class)))
                .thenReturn("84ac550e-51a2-43df-a094-18fff3c4245f");

        mockMvc.perform(
                post("/platform/new-game/3/2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("game-id").value("84ac550e-51a2-43df-a094-18fff3c4245f"));
    }

    @Test
    public void shouldThrowExceptionIfWrongGameIdIsProvided() throws Exception {

        when(platformService.getCurrentValue(anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                        "Game id is either empty or game is not started yet."));

        mockMvc.perform(
                get("/platform/84ac550e-51a2-43df-a094-18fff3c4245f/current-position")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    public void shouldReturn200IfCorrectGameIdIsProvided() throws Exception {

        when(platformService.getCurrentValue(anyString()))
                .thenReturn("32");

        mockMvc.perform(
                get("/platform/84ac550e-51a2-43df-a094-18fff3c4245f/current-position")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("current-value").value("32"));
    }

    @Test
    public void shouldReturn200WithValidMoves() throws Exception {

        ArrayList<String> validMoves = new ArrayList<>();
        validMoves.add("Press E for East");
        validMoves.add("Press W for West");
        validMoves.add("Press N for North");
        validMoves.add("Press S for South");
        validMoves.add("Press NE for NorthEast");
        validMoves.add("Press SE for SouthEast");
        validMoves.add("Press SW for SouthWest");
        validMoves.add("Press NW for NorthWest");

        when(platformService.validMoves())
                .thenReturn(validMoves);

        mockMvc.perform(
                get("/platform/valid-moves")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("valid-moves").value(validMoves));
    }

    @Test
    public void shouldReturn200WhenNewPositionIsSet() throws Exception {

        when(platformService.applyNextMove(anyString(), anyString()))
                .thenReturn("56");

        mockMvc.perform(
                post("/platform/84ac550e-51a2-43df-a094-18fff3c4245f/next-move/SE")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("current-position").value("56"));
    }

    @Test
    public void shouldReturn200WithPath() throws Exception {

        ArrayList<String> path = new ArrayList<>();
        path.add("32");
        path.add("10");
        when(platformService.getPath(anyString()))
                .thenReturn(path);

        mockMvc.perform(
                get("/platform/84ac550e-51a2-43df-a094-18fff3c4245f/path")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("game-path").value(path));
    }


    @Test
    public void shouldReturn200WithUniquePath() throws Exception {

        String[] uniqueIds = {
                "32",
                "14",
                "06",
                "28",
                "25",
                "43",
                "61",
                "91",
                "94",
                "72",
                "50",
                "72",
                "54",
                "36",
                "39",
                "69",
                "47",
                "44",
                "74",
                "96",
                "74",
                "56",
                "86",
                "64",
                "42",
                "24",
                "27",
                "49",
                "67",
                "85",
                "55",
                "52",
                "30",
                "60",
                "82",
                "94",
                "97",
                "79",
                "97",
                "75",
                "78",
                "96",
                "74",
                "52",
                "70",
                "92",
                "74",
                "77",
                "99",
                "77",
                "99",
                "77",
                "95",
                "98",
                "76",
                "94",
                "76",
                "98",
                "76",
                "94",
                "72",
                "54",
                "72",
                "54",
                "36",
                "58",
                "36",
                "18",
                "36",
                "54",
                "57",
                "35",
                "13",
                "16",
                "19",
                "37",
                "19",
                "37",
                "07",
                "04",
                "26",
                "23",
                "01",
                "23",
                "41",
                "63",
                "41",
                "23",
                "20",
                "42",
                "60",
                "82",
                "60",
                "82",
                "94",
                "72",
                "94",
                "76",
                "58",
                "76"};
        ArrayList<String> path = new ArrayList<>(Arrays.asList(uniqueIds));
        when(platformService.generatePath(anyString(), any(Integer.class)))
                .thenReturn(path);

        mockMvc.perform(
                post("/platform/84ac550e-51a2-43df-a094-18fff3c4245f/unique-path")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("unique-path").value(path));
    }

    @Test
    public void shouldReturn200WhenGameIsEndedByRequest() throws Exception {

        when(platformService.finishGame(anyString()))
                .thenReturn(true);

        mockMvc.perform(
                post("/platform/84ac550e-51a2-43df-a094-18fff3c4245f/end-game")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
