package com.platform.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class PlatformServiceTest {

    @InjectMocks
    private PlatformService platformService = new PlatformService();

    @Test
    public void shouldReturnGameId(){
        assertFalse(platformService.startNewGame(2, 2).isEmpty());
    }

    @Test
    public void shouldReturnCurrentPosition() {
        String gameId = platformService.startNewGame(3, 2);

        assertEquals(platformService.getCurrentValue(gameId), "23");
    }

    @Test
    public void shouldReturnNewPositionOnApplyingNewMove() {
        String gameId = platformService.startNewGame(3, 2);

        assertEquals(platformService.applyNextMove(gameId, "NW"), "01");
    }

    @Test
    public void shouldReturnPath() {
        String gameId = platformService.startNewGame(3, 2);
        platformService.applyNextMove(gameId, "NW");

        ArrayList<String> path = new ArrayList<>();
        path.add("23");
        path.add("01");

        assertEquals(platformService.getPath(gameId), path);
    }

    @Test
    public void shouldHaveAllCellsInPath() {
        String gameId = platformService.startNewGame(0, 1);
        ArrayList<String> generatedPath = platformService.generatePath(gameId, 0);

        AtomicInteger at = new AtomicInteger();
        generatedPath.forEach(m -> {
            System.out.print(m + " ");
            if(at.incrementAndGet() % 10 == 0) {
                System.out.println();
            }
        });

        assertEquals(generatedPath.size(), 100);
    }
}
