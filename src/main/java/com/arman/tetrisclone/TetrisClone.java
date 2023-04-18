package com.arman.tetrisclone;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;


//TODO: IDEA TO FIX COLLISION ISSUE. CALCULATE POSSIBLE COORDINATES WHEN ROTATING AND ONLY ALLOW THE POSITION THAT
//TODO DOES NOT COLLIDE
public class TetrisClone extends Application {

    private Well well;
    GraphicsContext g;
    public long frameNum;
    double[][][] rowFillTetriminosXCoords = new double[Well.NUM_OF_ROWS][Well.NUM_OF_COLS][4]; //1st index: row num, 2nd index: tetrimino num, 3rd index: xCoord
    int[][] rowFillTetriminos = new int[Well.NUM_OF_ROWS][Well.NUM_OF_COLS]; //the tetrimino num
    int[] rowFillTetriminoTracker = new int[Well.NUM_OF_COLS];
    public boolean interruptFall = false;
    public boolean checkRotation = false;
    public int filledRow = -1;
    public boolean shiftAllBlocksDown = false;
    public boolean heldDown = false;
    public boolean newTetrimino = false;
    public boolean doneCheck = false;
    Tetrimino currentTetrimino;
    AnimationTimer timer;

    ArrayList<Tetrimino> tetriminos = new ArrayList<>();
    Tetrimino tetrimino; // the tetris piece

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        well = new Well();
        g = well.getGraphicsContext2D();
        BorderPane root = new BorderPane(well);
        well.draw();
        tetriminos.add(new Tetrimino());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if ((frameNum%1 == 0 && heldDown) || frameNum % 40 == 0) {
                    doTetriminoAction();
                }
                else if (interruptFall){
                    doTetriminoAction();
                    interruptFall = false;
                }

                frameNum++;
            }
        };

        well.requestFocus();
        well.setFocusTraversable(true);


        well.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            currentTetrimino = tetriminos.get(tetriminos.size()-1);
            if(currentTetrimino.placed || !doneCheck){
                return;
            }
            if(code == KeyCode.LEFT && currentTetrimino.MOVE_LEFT){
                interruptFall = true;
                currentTetrimino.moveX(-Tetrimino.SQUARE_SIZE);
            }
            else if(code == KeyCode.RIGHT && currentTetrimino.MOVE_RIGHT) {
                interruptFall = true;
                currentTetrimino.moveX(Tetrimino.SQUARE_SIZE);
            }
            else if(code == KeyCode.DOWN){
                heldDown = true;
            }
            // Todo: COLLISION HAS TO BE FIXED WHEN ROTATING TETRIMINO BLOCKS LIKE THE L AND LINE BLOCKS SINCE
            // Todo: IT IS NOT DETECTING COLLISION WITH THEIR ROTATIONS BECAUSE IT'S A "BIG JUMP"
            else if(code == KeyCode.SPACE){
                currentTetrimino.rotate();
                checkRotation = true;
                interruptFall = true;
            }

        });

        well.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()){
                timer.start();
            }
            else
                timer.stop();
        });

        well.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if(code == KeyCode.DOWN){
                heldDown = false;
            }
        });

        timer.start();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tetris Clone");
        primaryStage.show();
    }

    // I delete a row of blocks only when the row is filled. What that means is that the y coordinates
    // of all
    int[] returnFilledRows(int[][] rowTetriminos){
        int[] filledRows = new int[Well.NUM_OF_ROWS];
        int j = 0;
        for (int i = 0; i < 30 ; i++) {
            if(rowTetriminos[i][Well.NUM_OF_COLS-1] != 0){ // CHECK if last available tetrimino
                filledRows[j] = i;
            }
            else{
                filledRows[i] = -1;
            }
        }
        return filledRows;
    }
    int returnFilledRow(int[][] rowTetriminos){
        int selectedRow = -1;
        for (int i = 0; i < 30 ; i++) {
            if(rowTetriminos[i][Well.NUM_OF_COLS-1] != 0){ // CHECK if last available tetrimino
                selectedRow = i;
                break;
            }
        }
        return selectedRow;
    }


    void doTetriminoAction(){
        well.draw();
        doneCheck = false;
        boolean rowFill;
        int rowNum;

        Tetrimino currentTetrimino = tetriminos.get(tetriminos.size()-1);
        currentTetrimino.update(interruptFall);
        currentTetrimino.draw(g);
        currentTetrimino.MOVE_LEFT = true;
        currentTetrimino.MOVE_RIGHT = true;

        bigloop: for(int l = 0; l < tetriminos.size(); l++) {
            tetrimino = tetriminos.get(l);
            if (tetriminos.size() != 1 && tetrimino.placed) {
                for (int i = 0; i < tetrimino.xCoords.length; i++) {
                    rowNum = (int)((Well.HEIGHT-currentTetrimino.yCoords[i])/10)/2-1; // supposedly 0 - 29

                    System.out.println(rowNum);
                    System.out.println();

                    //Todo: ISSUE: I think all blocks including one
                    if(shiftAllBlocksDown && tetrimino.yCoords[i] <= Well.HEIGHT -
                            (filledRow*Tetrimino.SQUARE_SIZE + Tetrimino.SQUARE_SIZE)){
                        tetrimino.yCoords[i]+=Tetrimino.SQUARE_SIZE;
                    }
                    // Todo: implement Tetris delete row functionality
                    if (currentTetrimino.contains(tetrimino.xCoords[i], tetrimino.yCoords[i])) {
                        currentTetrimino.placed = true;
                        currentTetrimino.MOVE_LEFT = false;
                        currentTetrimino.MOVE_RIGHT = false;
                        //break bigloop;
                    }
                    if (!currentTetrimino.canMoveLeft(tetrimino.xCoords[i], tetrimino.yCoords[i])) {
                        currentTetrimino.MOVE_LEFT = false;
                    }
                    if (!currentTetrimino.canMoveRight(tetrimino.xCoords[i], tetrimino.yCoords[i])) {
                        currentTetrimino.MOVE_RIGHT = false;
                    }
                }
                tetrimino.draw(g);
            }
        }
        shiftAllBlocksDown=false;


        if(currentTetrimino.placed){
            tetriminos.add(new Tetrimino());
            // Todo: NEW CODE!!! EDIT ACCORDINGLY
            //Todo: FIRST ROW DELETE WORKS BUT AFTER THAT THE SECOND ROW ARRAY IS NOT FULL AND HAS SOME ZEROES?? AND
            //tODO: THE FIRST ROW STILL HAS ELEMENTS??? ACTUALLY WAIT THE SECOND ROW HAS THE ELEMENTS OF THE FIRST ROW???
            for (int i = 0; i < currentTetrimino.xCoords.length; i++) {
                rowNum = (int)((Well.HEIGHT-currentTetrimino.yCoords[i])/10)/2-1; // supposedly 0 - 29

                rowFillTetriminos[rowNum][rowFillTetriminoTracker[rowNum]] = tetriminos.size()-1;
                rowFillTetriminoTracker[rowNum]++;

                System.out.println(Arrays.deepToString(rowFillTetriminos));

                filledRow = returnFilledRow(rowFillTetriminos);
                if(filledRow != -1){
                    double yCoord = Well.HEIGHT - (filledRow*Tetrimino.SQUARE_SIZE + Tetrimino.SQUARE_SIZE);
                    for (int j = 0; j < Well.NUM_OF_COLS; j++) {
                        tetriminos.get(rowFillTetriminos[filledRow][j]-1).deleteBlock(yCoord);
                        rowFillTetriminos[filledRow][j] = 0;
                    }
                    rowFillTetriminoTracker[filledRow] = 0;
                    System.out.println(Arrays.deepToString(rowFillTetriminos));
                    shiftAllBlocksDown = true;
                }
            }
        }
        doneCheck = true;
        well.drawGrid(g);
    }
}