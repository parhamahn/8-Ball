package model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private static double DEFAULT_STARTX = 0;
    private static double DEFAULT_STARTY = 200;
    private static double DEFAULT_ENDX = 1200;
    private static double DEFAULT_ENDY = 800;

    private final double startX;
    private final double endX;
    private final double startY;
    private final double endY;
    private final double cushionThickness;
    private final List<Pocket> pockets;

    public Table(){
        this.startX = DEFAULT_STARTX;
        this.startY = DEFAULT_STARTY;
        this.endX = DEFAULT_ENDX;
        this.endY = DEFAULT_ENDY;
        this.cushionThickness = Pocket.getDefaultRadius();
        this.pockets = new ArrayList<>();
        pockets.add(new Pocket(startX + Pocket.getDefaultRadius(), startY + Pocket.getDefaultRadius(), 1));
        pockets.add(new Pocket((endX - startX)/2, startY + Pocket.getDefaultRadius(), 2));
        pockets.add(new Pocket(endX - Pocket.getDefaultRadius(), startY + Pocket.getDefaultRadius(), 3));
        pockets.add(new Pocket(startX + Pocket.getDefaultRadius(), endY - Pocket.getDefaultRadius(), 4));
        pockets.add(new Pocket((endX - startX)/2, endY - Pocket.getDefaultRadius(), 5));
        pockets.add(new Pocket(endX - Pocket.getDefaultRadius(), endY - Pocket.getDefaultRadius(), 6));
    }

    public static double getDefaultStartx() {
        return DEFAULT_STARTX;
    }

    public static double getDefaultStarty() {
        return DEFAULT_STARTY;
    }

    public static double getDefaultEndx() {
        return DEFAULT_ENDX;
    }

    public static double getDefaultEndy() {
        return DEFAULT_ENDY;
    }

    public double getStartX() {
        return startX;
    }

    public double getEndX() {
        return endX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndY() {
        return endY;
    }

    public double getCushionThickness() {
        return cushionThickness;
    }

    public List<Pocket> getPockets() {
        return pockets;
    }

    public double getWidth(){
        return endX - startX;
    }

    public double getHeight(){
        return endY - startY;
    }
}