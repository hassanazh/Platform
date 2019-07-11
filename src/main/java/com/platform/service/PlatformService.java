package com.platform.service;

import com.platform.api.NextMove;
import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PlatformService {
    private final static Logger logger = Logger.getLogger(PlatformService.class);

    private final Map<String, Position> runningSessions = new HashMap<>();

    public String startNewGame(Integer row, Integer column) {
        String gameId = UUID.randomUUID().toString();

        Position startPosition = new Position(column, row);
        runningSessions.put(gameId, startPosition);

        ArrayList<String> startPath = new ArrayList<>();
        startPath.add(startPosition.toString());

        logger.info("New game [" + gameId + "] started with position: " + startPosition);
        return gameId;
    }

    public boolean finishGame(String gameId) {
        validateGameId(gameId);
        runningSessions.remove(gameId);

        logger.info("Finishing the game [" + gameId + "]");
        return runningSessions.containsKey(gameId);
    }

    public ArrayList<String> validMoves() {
        ArrayList<String> validMoves = new ArrayList<>();
        validMoves.add("Press E for East");
        validMoves.add("Press W for West");
        validMoves.add("Press N for North");
        validMoves.add("Press S for South");
        validMoves.add("Press NE for NorthEast");
        validMoves.add("Press SE for SouthEast");
        validMoves.add("Press SW for SouthWest");
        validMoves.add("Press NW for NorthWest");

        logger.info(validMoves);

        return validMoves;
    }

    public String getCurrentValue(String gameId) {
        validateGameId(gameId);

        String currentValue = runningSessions.get(gameId).toString();
        logger.debug("Current value is: " + currentValue);

        return currentValue;
    }

    public ArrayList<String> getPath(String gameId) {
        validateGameId(gameId);

        return runningSessions.get(gameId).getPath();
    }

    public ArrayList<String> generatePath(String gameId, int iterationValue) {
        validateGameId(gameId);

        Position currentValue = runningSessions.get(gameId);
        // Apply E
        moveEast(gameId, currentValue, currentValue.getColumn() + 3);
        // Apply SE
        moveSouthEast(gameId, currentValue, currentValue.getColumn() + 2, currentValue.getRow() + 2);
        // Apply S
        moveSouth(gameId, currentValue, currentValue.getRow() + 3);
        // Apply SW
        moveSouthWest(gameId, currentValue, currentValue.getRow() + 2, currentValue.getColumn() - 2);
        // Apply W
        moveWest(gameId, currentValue, currentValue.getColumn() - 3);
        // Apply NW
        moveNorthWest(gameId, currentValue, currentValue.getColumn() - 2, currentValue.getRow() - 2);
        // Apply N
        moveNorth(gameId, currentValue, currentValue.getRow() - 3);
        // Apply NE
        moveNorthEast(gameId, currentValue, currentValue.getColumn() + 2, currentValue.getRow() - 2);

        if (runningSessions.get(gameId).getPath().size() <= 100 && iterationValue < 1000) {
            generatePath(gameId, iterationValue + 1);
        }

        return runningSessions.get(gameId).getPath();
    }

    private void moveNorthEast(String gameId, Position currentValue, int eastColumn, int northRow) {
        if (isDiagonalMovePossible(currentValue, eastColumn, northRow, northRow >= 0) && eastColumn <= 9) {
            applyNextMove(gameId, NextMove.NE.name());
            logger.debug("Moved NE at new position: " + runningSessions.get(gameId));
            moveNorthEast(gameId, runningSessions.get(gameId), eastColumn + 2, northRow - 2);
        }
    }

    private void moveNorthWest(String gameId, Position currentValue, int westColumn, int northRow) {
        if (isDiagonalMovePossible(currentValue, westColumn, northRow, northRow >= 0 && westColumn >= 0)) {
            applyNextMove(gameId, NextMove.NW.name());
            logger.debug("Moved NW at new position: " + runningSessions.get(gameId));
            moveNorthWest(gameId, runningSessions.get(gameId), westColumn - 2, northRow - 2);
        }
    }

    private void moveSouthWest(String gameId, Position currentValue, int southRow, int westColumn) {
        if (isDiagonalMovePossible(currentValue, westColumn, southRow, southRow <= 9 && westColumn >= 0)) {
            applyNextMove(gameId, NextMove.SW.name());
            logger.debug("Moved SW at new position: " + runningSessions.get(gameId));
            moveSouthWest(gameId, runningSessions.get(gameId), southRow + 2, westColumn - 2);
        }
    }

    private void moveSouthEast(String gameId, Position currentValue, int eastColumn, int southRow) {
        if (isDiagonalMovePossible(currentValue, eastColumn, southRow, southRow <= 9 && eastColumn <= 9)) {
            applyNextMove(gameId, NextMove.SE.name());
            logger.debug("Moved SE at new position: " + runningSessions.get(gameId));
            moveSouthEast(gameId, runningSessions.get(gameId), eastColumn + 2, southRow + 2);
        }
    }

    private void moveNorth(String gameId, Position currentValue, int northRow) {
        if (isVerticalMovePossible(currentValue, northRow, northRow >= 0)) {
            applyNextMove(gameId, NextMove.N.name());
            logger.debug("Moved N at new position: " + runningSessions.get(gameId));
            moveNorth(gameId, runningSessions.get(gameId), northRow - 3);
        }
    }

    private void moveWest(String gameId, Position currentValue, int westColumn) {
        if (isHorizontalMovePossible(0, 0, currentValue, westColumn, westColumn >= 0)) {
            applyNextMove(gameId, NextMove.W.name());
            logger.debug("Moved W at new position: " + runningSessions.get(gameId));
            moveWest(gameId, runningSessions.get(gameId), westColumn - 3);
        }
    }

    private void moveSouth(String gameId, Position currentValue, int southRow) {
        if (isVerticalMovePossible(currentValue, southRow, southRow <= 9)) {
            applyNextMove(gameId, NextMove.S.name());
            logger.debug("Moved S at new position: " + runningSessions.get(gameId));
            moveSouth(gameId, runningSessions.get(gameId), southRow + 3);
        }
    }

    private void moveEast(String gameId, Position currentValue, int eastColumn) {
        if (isHorizontalMovePossible(0, 0, currentValue, eastColumn, eastColumn <= 9)) {
            applyNextMove(gameId, NextMove.E.name());
            logger.debug("Moved E at new position: " + runningSessions.get(gameId));
            moveEast(gameId, runningSessions.get(gameId), eastColumn + 3);
        }
    }

    public String applyNextMove(String gameId, String nextMove) {
        validateNextMove(nextMove);
        validateGameId(gameId);

        NextMove inputtedNextMove = EnumUtils.getEnum(NextMove.class, nextMove);

        Position currentValue = runningSessions.get(gameId);
//        logger.debug("Current value of the game [" + gameId + "] is: " + currentValue);

        Position calculatedValue = calculateNextValue(currentValue, inputtedNextMove);

        runningSessions.put(gameId, calculatedValue);
        return calculatedValue.toString();
    }

    private void validateGameId(String gameId) {
        if (Objects.isNull(gameId) || gameId.isEmpty() || !runningSessions.containsKey(gameId)) {
            logger.error("Game id is either empty or game is not started yet.");

            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                    "Game id is either empty or game is not started yet.");
        }
    }

    private void validateNextMove(String nextMove) {
        if (!EnumUtils.isValidEnumIgnoreCase(NextMove.class, nextMove)) {
            logger.error("Only following moves are allowed: \n" + validMoves());

            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                    "Only following moves are allowed: \n" + validMoves());
        }
    }

    private Position calculateNextValue(Position currentValue, NextMove nextMove) {
        switch (nextMove) {
            case E:
                return performNextMove(3, 0, 0, 0, currentValue);
            case W:
                return performNextMove(0, 3, 0, 0, currentValue);
            case N:
                return performNextMove(0, 0, 3, 0, currentValue);
            case S:
                return performNextMove(0, 0, 0, 3, currentValue);
            case NE:
                return performNextMove(2, 0, 2, 0, currentValue);
            case NW:
                return performNextMove(0, 2, 2, 0, currentValue);
            case SE:
                return performNextMove(2, 0, 0, 2, currentValue);
            case SW:
                return performNextMove(0, 2, 0, 2, currentValue);
            default:
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                        "Only following moves are allowed: \n" + validMoves());
        }
    }

    private Position performNextMove(int east, int west, int north, int south, Position currentValue) {
        if (east != 0) {
            int column = currentValue.getColumn() + east;
            if (column <= 9) {
                applyNorthOrSouthIfApplicable(north, south, currentValue, column);
            }
        } else if (west != 0) {
            int column = currentValue.getColumn() - west;
            if (column >= 0) {
                applyNorthOrSouthIfApplicable(north, south, currentValue, column);
            }
        } else if (north != 0) {
            int row = currentValue.getRow() - north;
            if (isVerticalMovePossible(currentValue, row, row >= 0)) {
                currentValue.setRow(row);
                currentValue.addPosition();
            }
        } else {
            int row = currentValue.getRow() + south;
            if (isVerticalMovePossible(currentValue, row, row <= 9)) {
                currentValue.setRow(row);
                currentValue.addPosition();
            }
        }

        return currentValue;
    }

    private void applyNorthOrSouthIfApplicable(int north, int south, Position currentValue, int column) {
        if (isHorizontalMovePossible(north, south, currentValue, column, true)) {
            currentValue.setColumn(column);
            currentValue.addPosition();
        } else if (north != 0 && south == 0) {
            int row = currentValue.getRow() - north;
            if (isDiagonalMovePossible(currentValue, column, row, row >= 0)) {
                currentValue.setColumn(column);
                currentValue.setRow(row);
                currentValue.addPosition();
            }
        } else {
            int row = currentValue.getRow() + south;
            if (isDiagonalMovePossible(currentValue, column, row, row <= 9)) {
                currentValue.setColumn(column);
                currentValue.setRow(row);
                currentValue.addPosition();
            }
        }
    }

    private boolean isAlreadyVisited(Position currentValue, int column, int row) {
        final boolean contains = currentValue.getPath().contains(new Position(column, row).toString());
        if (!contains) {
            logger.error("Already visited " + currentValue.toString());
        }

        return !contains;
    }

    private boolean isHorizontalMovePossible(int north, int south, Position currentValue, int column, boolean b) {
        return b && north == 0 && south == 0 && isAlreadyVisited(currentValue, column, currentValue.getRow());
    }

    private boolean isDiagonalMovePossible(Position currentValue, int column, int row, boolean b) {
        return b && isAlreadyVisited(currentValue, column, row);
    }

    private boolean isVerticalMovePossible(Position currentValue, int row, boolean b) {
        return b && isAlreadyVisited(currentValue, currentValue.getColumn(), row);
    }
}

/*
E W N S
3 0 0 0
2 0 0 2
0 0 0 3
0 2 0 2
0 3 0 0
0 2 2 0
0 0 3 0
2 0 2 0
*/
