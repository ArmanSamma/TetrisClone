package com.arman.tetrisclone;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Well extends Canvas {
    Color bgColor;

    public static final int NUM_OF_COLS= 20;
    public static final int NUM_OF_ROWS= 30;
    public static final double WIDTH = Tetrimino.SQUARE_SIZE*NUM_OF_COLS; // 400
    public static final double HEIGHT = Tetrimino.SQUARE_SIZE*NUM_OF_ROWS; // 600

    public Well(){
        super(WIDTH,HEIGHT);
        bgColor = Color.DARKSLATEBLUE;
    }

    public void draw(){
        GraphicsContext g = this.getGraphicsContext2D();
        g.setFill(bgColor.darker().darker().darker().darker());
        g.fillRect(0,0,this.getWidth(),this.getHeight());

    }

    public void drawGrid(GraphicsContext g){
        g.setStroke(bgColor.darker());
        for (int x = 0; x < WIDTH; x+=Tetrimino.SQUARE_SIZE) {
            for (int y = 0; y < HEIGHT; y+=Tetrimino.SQUARE_SIZE) {
                g.strokeRect(x,y,Tetrimino.SQUARE_SIZE,Tetrimino.SQUARE_SIZE);
            }
        }
    }

}