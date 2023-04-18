package com.arman.tetrisclone;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/** The Tetris block that comes in 5 main forms*/
class Tetrimino{

    /**
     * TOP-LEFT coordinates of the 4 squares making the tetrimino
     */
    public double[] xCoords = new double[4];
    public double[] yCoords = new double[4];

    public static final double SQUARE_SIZE = 20; /** The size of each square in a Tetrimino. 40 squares can be placed horizontally and 60 squares, vertically
     in the 'Well'*/

    Color color;

    public static int NUM = 0;

    public static final int IOTA = 0;
    public static final int OMICRON = 1;

    public static final int LAMEDH_L = 2;
    public static final int LAMEDH_J = 3;

    public static final int ZETA_Z = 4;
    public static final int ZETA_S = 5;

    public static final int TAU = 6;


    public int TYPE;

    public int fallingSpeed, blockCount;
    private double tetriminoAngle;

    public boolean placed, MOVE_LEFT, MOVE_RIGHT;
    public double pivotX,pivotY;
    public static int prevTYPE = -1;

    public Tetrimino(){
        NUM++;
        blockCount = 0;
        MOVE_LEFT = true;
        MOVE_RIGHT = true;
        placed = false;
        fallingSpeed = (int)SQUARE_SIZE;
        tetriminoAngle = 90;
        while (prevTYPE == TYPE){
            TYPE = (int) (Math.random()*7);
        }
//        TYPE = ZETA_S;
        setTypeProperties(TYPE);
        prevTYPE = TYPE;
    }

    public void update(boolean interruptFall){
        if(!interruptFall){
            moveY(fallingSpeed);
        }
        for (double xCoord : xCoords) {
            if (xCoord > Well.WIDTH) {
                moveX(-20 * 2);
//                setPoints();
                return;
            } else if (xCoord < 0) {
                moveX(20 * 2);
//                setPoints();
                return;
            }
        }
    }

//    public void setPoints(){
//        for (int i = 0, j = 0; j < this.getPoints().size(); i++,j++) {
//            this.getPoints().set(j,xCoords[i]);
//            j++;
//            this.getPoints().set(j,yCoords[i]);
//
////            System.out.println("xCoords = " + xCoords[i] + "    yCoords = " + yCoords[i]);
//        }
//    }


    public void moveX(double steps){

        double[] newArray = new double[xCoords.length];
        for (int i = 0; i < newArray.length; i++) {
            if((xCoords[i] >= Well.WIDTH-SQUARE_SIZE && !(steps < 0)) || (xCoords[i] <= 0 && !(steps > 0))) {
                return;
            }
            newArray[i] = xCoords[i]+steps;
        }
        pivotX += steps;
        xCoords = newArray;
    }

    public void moveY(double steps){
        double[] newArray = new double[yCoords.length];
        for (int i = 0; i < newArray.length; i++) {
            if(yCoords[i] >= Well.HEIGHT-SQUARE_SIZE){
                placed = true;
                return;
            }
            newArray[i] = yCoords[i]+steps;
        }
        pivotY += steps;
        yCoords = newArray;
    }
    public void draw(GraphicsContext g){
        g.setFill(color);
        for (int i = 0; i < xCoords.length; i++) {
            g.fillRect(xCoords[i],yCoords[i],SQUARE_SIZE,SQUARE_SIZE);
        }
    }

    public void rotate(){
        if (this.TYPE == OMICRON){
            return;
        }

        Point2D point;
        Rotate rotate = new Rotate();
        if(!(TYPE == TAU || TYPE == LAMEDH_L || TYPE == LAMEDH_J)){
            tetriminoAngle = -tetriminoAngle;
        }
        else
            tetriminoAngle = 90;
        rotate.setAngle(tetriminoAngle);
        rotate.setPivotX(pivotX);
        rotate.setPivotY(pivotY); // Todo: Implement IOTA AND OMICRON exceptions
        for (int i = 0; i < xCoords.length; i++) {
            point = rotate.transform(xCoords[i],yCoords[i]);
            xCoords[i] = Math.round(point.getX()) + 0d;
            yCoords[i] = Math.round(point.getY()) + 0d;
        }
    }

    private void setTypeProperties(int TYPE){
        double startX = Well.WIDTH/2-SQUARE_SIZE;
        switch (TYPE){
            case IOTA:
                addBlock(startX,0);
                addBlock(startX,SQUARE_SIZE);
                addBlock(startX,SQUARE_SIZE*2);
                addBlock(startX,SQUARE_SIZE*3);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE*4,
//                        startX+SQUARE_SIZE,SQUARE_SIZE*4,
//                        startX+SQUARE_SIZE,0d
//                );
                pivotX = startX+SQUARE_SIZE/2;
                pivotY = SQUARE_SIZE*2+SQUARE_SIZE/2;

                color = Color.CYAN;
                break;
            case OMICRON:
                addBlock(startX,0);
                addBlock(startX,SQUARE_SIZE);
                addBlock(startX+SQUARE_SIZE,SQUARE_SIZE);
                addBlock(startX+SQUARE_SIZE,0);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE*2,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE*2,0d
//                );

                color = Color.YELLOW;
                break;
            case LAMEDH_L:
                addBlock(startX,0);
                addBlock(startX,SQUARE_SIZE);
                addBlock(startX,SQUARE_SIZE*2);
                addBlock(startX+SQUARE_SIZE,SQUARE_SIZE*2);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE*3,
//                        startX+SQUARE_SIZE*2,SQUARE_SIZE*3,
//                        startX+SQUARE_SIZE*2,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE,0d
//
//                );
                pivotX = startX+SQUARE_SIZE/2;
                pivotY = SQUARE_SIZE*2-SQUARE_SIZE/2;

                color = Color.ORANGE;
                break;
            case LAMEDH_J:
                startX+=SQUARE_SIZE;
                addBlock(startX,0);
                addBlock(startX,SQUARE_SIZE);
                addBlock(startX,SQUARE_SIZE*2);
                addBlock(startX-SQUARE_SIZE,SQUARE_SIZE*2);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE*3,
//                        startX-SQUARE_SIZE*2,SQUARE_SIZE*3,
//                        startX-SQUARE_SIZE*2,SQUARE_SIZE*2,
//                        startX-SQUARE_SIZE,SQUARE_SIZE*2,
//                        startX-SQUARE_SIZE,0d
//
//                );
                pivotX = startX-SQUARE_SIZE/2;
                pivotY = SQUARE_SIZE*2-SQUARE_SIZE/2;

                color = Color.ORANGE;
                break;
            case ZETA_Z:
                addBlock(startX,0);
                addBlock(startX+SQUARE_SIZE,0);
                addBlock(startX+SQUARE_SIZE,SQUARE_SIZE);
                addBlock(startX+SQUARE_SIZE*2,SQUARE_SIZE);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE,
//                        startX+SQUARE_SIZE,SQUARE_SIZE,
//                        startX+SQUARE_SIZE,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE*3,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE*3,SQUARE_SIZE,
//                        startX+SQUARE_SIZE*2,SQUARE_SIZE,
//                        startX+SQUARE_SIZE*2,0d
//                );
                pivotX = startX+SQUARE_SIZE*2-SQUARE_SIZE/2;
                pivotY = SQUARE_SIZE-SQUARE_SIZE/2;

                color = Color.GREEN;
                break;
            case ZETA_S:
                startX = startX+SQUARE_SIZE;
                addBlock(startX,0);
                addBlock(startX-SQUARE_SIZE,0);
                addBlock(startX-SQUARE_SIZE,SQUARE_SIZE);
                addBlock(startX-SQUARE_SIZE*2,SQUARE_SIZE);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE,
//                        startX-SQUARE_SIZE,SQUARE_SIZE,
//                        startX-SQUARE_SIZE,SQUARE_SIZE*2,
//                        startX-SQUARE_SIZE*3,SQUARE_SIZE*2,
//                        startX-SQUARE_SIZE*3,SQUARE_SIZE,
//                        startX-SQUARE_SIZE*2,SQUARE_SIZE,
//                        startX-SQUARE_SIZE*2,0d
//                );
                pivotX = startX-SQUARE_SIZE*2+SQUARE_SIZE/2;
                pivotY = SQUARE_SIZE - SQUARE_SIZE/2;

                color = Color.GREEN;
                break;
            case TAU:
                startX = startX-SQUARE_SIZE;
                addBlock(startX,0);
                addBlock(startX+SQUARE_SIZE,0);
                addBlock(startX+SQUARE_SIZE*2,0);
                addBlock(startX+SQUARE_SIZE,SQUARE_SIZE);

//                this.getPoints().addAll(
//                        startX,0d,
//                        startX,SQUARE_SIZE,
//                        startX+SQUARE_SIZE,SQUARE_SIZE,
//                        startX+SQUARE_SIZE,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE*2,SQUARE_SIZE*2,
//                        startX+SQUARE_SIZE*2,SQUARE_SIZE,
//                        startX+SQUARE_SIZE*3,SQUARE_SIZE,
//                        startX+SQUARE_SIZE*3,0d
//                );
                pivotX = startX+SQUARE_SIZE+SQUARE_SIZE/2;
                pivotY = SQUARE_SIZE/2;

                color = Color.RED;
                break;
        }
    }

    private double[] newArray(double[] originArray){
        double[] newArray = new double[originArray.length+1];

        System.arraycopy(originArray, 0, newArray, 0, originArray.length);
        return newArray;
    }

    private void addBlock(double x, double y){
        xCoords[blockCount] = x;
        yCoords[blockCount] = y;
        blockCount++;
    }

    public void deleteBlock(double y){
        double[] newXCoords = new double[xCoords.length-1];
        double[] newYCoords = new double[yCoords.length-1];
        int j = 0;
        for (int i = 0; i < xCoords.length && j <xCoords.length-1; i++) {
            System.out.println(yCoords[i] + " VS " + y);
            if(yCoords[i] != y){
                newXCoords[j] = xCoords[i];
                newYCoords[j] = yCoords[i];
                j++;
            }
        }
        xCoords = newXCoords;
        yCoords = newYCoords;
        blockCount--;

    }

    public boolean contains(double x, double y){
        for (int i = 0; i < xCoords.length; i++) {
            if(xCoords[i] == x){
                if(yCoords[i] == y-SQUARE_SIZE){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveLeft(double x, double y){
        for (int i = 0; i < xCoords.length; i++) {
            if(xCoords[i]-SQUARE_SIZE == x){
                if(yCoords[i] == y-SQUARE_SIZE){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean canMoveRight(double x, double y){
        for (int i = 0; i < xCoords.length; i++) {
            if(xCoords[i]+SQUARE_SIZE == x){
                if(yCoords[i] == y-SQUARE_SIZE){
                    return false;
                }
            }
        }
        return true;
    }

    public void doPlacedAnimation(GraphicsContext g){

    }
}