import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class PolygonPreviewPanel extends JPanel {
    myPolygon polygon;
    Point center;
    int width, height;


    public PolygonPreviewPanel(myPolygon polygon, Point center){
        this.polygon = polygon;
        this.center = center;
        width = getWidth();
        height = getHeight();
        setBackground(Main.backgroundColor);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //draw polygon
        if(polygon != null) {
            g2.setStroke(new BasicStroke(5));
            g2.setColor(polygon.getColor());
            g2.draw(compressPolygon(polygon, getWidth(), getHeight()));
        }
    }

    private Polygon compressPolygon(myPolygon polygon, int width, int height){
        int[] xsForNewPolygon = new int[polygon.getPoints().length];
        int[] ysForNewPolygon = new int[polygon.getPoints().length];


        for(int i = 0; i < xsForNewPolygon.length; i++){
            Point compressedPoint = compressPoint(polygon.getInitXs()[i], polygon.getInitYs()[i],
                    polygon.getHighestXcoordinatate(), polygon.getHighestYcoordinate(), width, height);

            xsForNewPolygon[i] = compressedPoint.x;
            ysForNewPolygon[i] = compressedPoint.y;
        }
        myPolygon tempPolygon = new myPolygon(xsForNewPolygon, ysForNewPolygon, null);

        //center polygon
        int translateXby = tempPolygon.getLowestXcoordinate()/2;
        int translateYby = tempPolygon.getLowestYcoordinate()/2;
        for(int i = 0; i < tempPolygon.getPoints().length; i++){
            xsForNewPolygon[i] -= translateXby;
            ysForNewPolygon[i] -= translateYby;
        }


        return new Polygon(xsForNewPolygon, ysForNewPolygon, xsForNewPolygon.length);
    }

    private Point compressPoint(int x, int y, int maxX, int maxY, int maxWidth, int maxHeight){
         x = x*maxWidth/maxX;
         y = y*maxHeight/maxY;
         return new Point(x, y);
    }
}
