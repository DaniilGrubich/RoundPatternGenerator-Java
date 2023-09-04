import java.awt.*;
import java.util.ArrayList;

class myPolygon extends Polygon {
    Color color;
    static int order = 1;
    String label;
    int angleToRotate = 10;
    int[] initXs, initYs;
    boolean visible = true;
//    int xforTransformation, yforTransformation;

    public myPolygon(int[] xs, int[] ys, Color color) {
        super(xs, ys, xs.length);
        this.color = color;

        initXs = xs;
        initYs = ys;

        if(color != null) {
            label = "Polygon - " + order;
            order++;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getAngleToRotate() {
        return angleToRotate;
    }

    public void setAngleToRotate(int angleToRotate) {
        this.angleToRotate = angleToRotate;
    }

    public int[] getInitXs() {
        return initXs;
    }

    public int[] getInitYs() {
        return initYs;
    }

    @Override
    public String toString() {
        return label;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    static myPolygon findPolygon(String name, ArrayList<myPolygon> list) {
        for (myPolygon p : list) {
            if (p.getLabel().equals(name))
                return p;
        }
        return null;
    }

    public Point[] getPoints() {
        int[] xs = this.xpoints;
        int[] ys = this.ypoints;
        Point[] points = new Point[xs.length];
        for (int i = 0; i < xs.length; i++)
            points[i] = new Point(xs[i], ys[i]);

        return points;
    }

    public int getHighestXcoordinatate() {
        int highest = 0;
        for (int x : initXs) {
            if (x > highest)
                highest = x;
        }
        return highest;
    }

    public int getHighestYcoordinate() {
        int highest = 0;
        for (int y : initYs) {
            if (y > highest)
                highest = y;
        }
        return highest;
    }

    public int getLowestXcoordinate(){
        int lowest = initXs[initXs.length-1];
        for(int x : initXs) {
            if(x<lowest)
                lowest = x;
        }
        return lowest;
    }

    public int getLowestYcoordinate(){
        int lowest = initYs[initYs.length-1];
        for(int y : initYs){
            if(y<lowest)
                lowest = y;
        }
        return lowest;
    }
}
