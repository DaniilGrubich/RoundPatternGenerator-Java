import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.security.cert.PolicyNode;

public class MouseHandler extends MouseInputAdapter {
      static Graphics2D g;
      static BufferStrategy bufferStrategy;
      static Point center;
//    private


    public MouseHandler(BufferStrategy bs){
        bufferStrategy = bs;

    }

    public void mouseClicked(MouseEvent e) {
//        super.mouseClicked(e);
        if (e.getButton() == 1) {
            //reset central point
            g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.setColor(Main.backgroundColor);
            g.fillRect(0, 0, Main.mainFrame.getWidth(), Main.mainFrame.getHeight());
            g.setColor(Main.CENTERPOINT_COLOR);

            Main.center = center = new Point(e.getX(), e.getY());
            g.fillOval(center.x - 10 / 2, center.y - 10 / 2, 10, 10);
        } else if (e.getButton() == 2) {
            Main.createPattern((Graphics2D)bufferStrategy.getDrawGraphics());
            g.dispose();
            bufferStrategy.show();
        } else if (e.getButton() == 3) {
            Main.addPoint(g, e.getX(), e.getY());
        }
    }


}
