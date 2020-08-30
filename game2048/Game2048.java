package com.javarush.games.game2048;

import com.javarush.engine.cell.*;
import javafx.geometry.Side;

import java.sql.Array;
import java.util.Arrays;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame() {
        score = 0;
        setScore(score);
        gameField = new int [SIDE][SIDE];
        for (int i = 0; i < 2; i++) {
            createNewNumber();
        }
    }

    private void drawScene() {
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                setCellColoredNumber(i, j, gameField[i][j]);
            }
        }
    }

    private void createNewNumber() {
        if(getMaxTileValue() == 2048){
            win();
        }
        int i = getRandomNumber(SIDE);
        int i1 = getRandomNumber(SIDE);
        int i2 = getRandomNumber(10) == 9 ? 4 : 2;
        if (gameField[i][i1] == 0) {
            gameField[i][i1] = i2;
        } else {
            createNewNumber();
        }
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.NONE, "WIN!", Color.GREEN, 75);
    }
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.NONE, "GAME OVER!", Color.RED, 75);
    }

    private void setCellColoredNumber(int coordX, int coordY, int value) {
        if (value == 0) {
            setCellValueEx(coordX, coordY, Color.AQUA, "", Color.BLACK,30);
        } else {
            setCellValueEx(coordX, coordY, getColorByValue(value), String.valueOf(value), Color.BLACK, 30);
        }
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 0:
                return Color.WHITE;
            case 2:
                return Color.DEEPPINK;
            case 4:
                return Color.PURPLE;
            case 8:
                return Color.BLUE;
            case 16:
                return Color.DEEPSKYBLUE;
            case 32:
                return Color.GREEN;
            case 64:
                return Color.LIGHTGREEN;
            case 128:
                return Color.ORANGE;
            case 256:
                return Color.ROSYBROWN;
            case 512:
                return Color.INDIANRED;
            case 1024:
                return Color.HOTPINK;
            default:
                return Color.LIGHTPINK;
        }
    }

    private boolean compressRow(int[] row) {

        boolean flag = false;
        for (int i = 0; i < row.length; i++) {
            if (row[i] == 0) {
                for (int j = i; j < row.length; j++) {
                    if (row[j] != 0) {
                        row[i] = row[j];
                        row[j] = 0;
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    private boolean mergeRow(int[] row) {
        boolean flag = false;
        for (int i = 0; i < row.length - 1; i++) {
            if (row[i] == row[i + 1] && row[i] != 0) {
                row[i] = row[i] * 2;
                row[i + 1] = 0;
                flag = true;
                score += row[i];
                setScore(score);
            }
        }
        return flag;
    }

    @Override
    public void onKeyPress(Key key) {
        if(key.equals(Key.SPACE)) {
            if (isGameStopped) {
                isGameStopped = false;
                createGame();
                drawScene();
                return;
            } else {
                return;
            }
        }
        if(isGameStopped){return;}
        if(!canUserMove()){
            gameOver();
            return;
        }
        switch (key) {
            case UP:
                moveUp();
                drawScene();
                return;
            case DOWN:
                moveDown();
                drawScene();
                return;
            case LEFT:
                moveLeft();
                drawScene();
                return;
            case RIGHT:
                moveRight();
                drawScene();
                return;
        }
    }

    private void moveUp() {
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveLeft() {
        boolean flag = false;
        for (int i = 0; i < SIDE; i++) {
            if (compressRow(gameField[i])) {
                flag = true;
            }
            if (mergeRow(gameField[i])) {
                flag = true;
            }
            if (compressRow(gameField[i])) {
                flag = true;
            }
        }
        if (flag) {
            createNewNumber();
        }
    }

    private void moveRight() {
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] arr = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                arr[i][j] = gameField[gameField.length - j - 1][i];
            }
        }
        gameField = arr;
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (max < gameField[i][j]) {
                    max = gameField[i][j];
                }
            }

        }
        return max;
    }

    private boolean canUserMove(){
        int counOfZero = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int i1 = 0; i1 < SIDE; i1++) {
                if (gameField[i][i1] == 0) return true;
                if (i < SIDE-1){
                    if(gameField[i][i1] == gameField[i+1][i1]){
                        return true;
                    }
                }
                if (i1 < SIDE-1){
                    if (gameField[i][i1] == gameField[i][i1+1]){
                        return true;
                    }
                }
            }
        }return false;

    }
}