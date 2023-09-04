import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;

//import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

public class Main {
    static Point center;
    static BufferStrategy bs;
    static ArrayList<Point> points = new ArrayList<>();
    static ArrayList<myPolygon> polygons = new ArrayList<>();
    static myPolygon selectedPolygon;

    static Font DEFAULT_FONT = new Font(Font.SERIF, Font.PLAIN, 20);
    static Color CENTERPOINT_COLOR = new Color(134, 0, 6);
    static Color backgroundColor = Color.darkGray;
    static Color NEWPOINT_COLOR = new Color(60, 134, 53);
    static Color POLYGONS_DEFAULT_COLOR = new Color(43, 157, 155);

    static ImageIcon CLOSED_EYE = new ImageIcon("img\\closed_eye.png");
    static ImageIcon OPEN_EYE = new ImageIcon("img\\open_eye.png");

    static JFrame mainFrame, menuFrame;

    static JPanel panColorChange, panPolygonPreview, panForPanPreview;
    static JButton btnColor, btnDelete, btnRename, btnVisible;
    static JComboBox comboBox;

    static JSlider xsOfPolygon, ysOfPolygon, sliderDegrees;

    public static void main(String args[]){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        //imageicon constants creation
        CLOSED_EYE = new ImageIcon("src//img//closed_eye16x16.png");
        OPEN_EYE = new ImageIcon("src//img//open_eye16x16.png");

        //create mainFrame
        mainFrame = new JFrame("Patterns 2 - Canvas");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 500);
        mainFrame.setVisible(true);

        //set Default Center Point
        center = new Point(mainFrame.getWidth()/2, mainFrame.getHeight()/2);

        //create menuFrame
        menuFrame = new JFrame("Patterns 2 - Parameters");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(550,500);
//        menuFrame.setResizable(false);
        menuFrame.setLocation(mainFrame.getX()+10+mainFrame.getWidth(), mainFrame.getY());
        menuFrame.setLayout(new BorderLayout());
//        menuFrame.setLayout(new FlowLayout());





        JPanel panSettings = new JPanel();
        panSettings.setLayout(new BorderLayout());
        menuFrame.add(panSettings, BorderLayout.CENTER);

            JPanel panPolygonSettingsAndPreview = new JPanel();
            panPolygonSettingsAndPreview.setLayout(new BorderLayout());
            panPolygonSettingsAndPreview.setBackground(Color.lightGray);
            panSettings.add(panPolygonSettingsAndPreview, BorderLayout.NORTH);

                JPanel panPolygonSettings = new JPanel();
                panPolygonSettings.setBackground(Color.lightGray);
                panPolygonSettings.setLayout(new BoxLayout(panPolygonSettings, BoxLayout.Y_AXIS));
                panPolygonSettingsAndPreview.add(panPolygonSettings, BorderLayout.WEST);

                comboBox = new JComboBox<String>();
                comboBox.setFont(DEFAULT_FONT);
                comboBox.addItem("Select polygon");
                comboBox.addActionListener(e -> {
                    if(!comboBox.getSelectedItem().equals("Select polygon")) {
                        xsOfPolygon.setEnabled(true);
                        ysOfPolygon.setEnabled(true);
                        sliderDegrees.setEnabled(true);
                        btnRename.setEnabled(true);
                        btnDelete.setEnabled(true);

                        //setSelectedPolygon
                        selectedPolygon = myPolygon.findPolygon(comboBox.getSelectedItem().toString(), polygons);

                        //change colorPreview
                        btnColor.setBackground(selectedPolygon.getColor());
                        panColorChange.setBackground(selectedPolygon.getColor());

                        //change degree slider
                        sliderDegrees.setValue(selectedPolygon.getAngleToRotate());
//
                        //show polygon preview
                        panPolygonSettingsAndPreview.remove(panPolygonPreview);
                        panPolygonPreview = new PolygonPreviewPanel(selectedPolygon, center);
                        panPolygonSettingsAndPreview.add(panPolygonPreview, BorderLayout.CENTER);

                        //visible button for selected polygon
                        btnVisible.setEnabled(true);
                        if(selectedPolygon.visible)
                            btnVisible.setIcon(OPEN_EYE);
                        else
                            btnVisible.setIcon(CLOSED_EYE);


                        //update pattern
                        createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                    }else{
                        btnColor.setBackground(POLYGONS_DEFAULT_COLOR);
                        panColorChange.setBackground(POLYGONS_DEFAULT_COLOR);
                        xsOfPolygon.setEnabled(false);
                        ysOfPolygon.setEnabled(false);
                        sliderDegrees.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnRename.setEnabled(false);

                        panPolygonSettingsAndPreview.remove(panPolygonPreview);
                        panPolygonPreview = new PolygonPreviewPanel(null, center);
                        panPolygonSettingsAndPreview.add(panPolygonPreview, BorderLayout.CENTER);

                        btnVisible.setEnabled(false);
                    }

                });

                panPolygonSettings.add(comboBox);
                panPolygonSettings.add(Box.createVerticalStrut(3));


                panColorChange = new JPanel();
                panPolygonSettings.add(panColorChange);
                btnColor = new JButton("Change Color");
//                btnColor.setFont(DEFAULT_FONT);
                btnColor.setAlignmentX(Box.LEFT_ALIGNMENT);
                btnColor.setBackground(POLYGONS_DEFAULT_COLOR);
                panColorChange.setBackground(POLYGONS_DEFAULT_COLOR);
                btnColor.addActionListener(e -> {
                    if(!comboBox.getSelectedItem().equals("Select polygon")){
                        //show color chooser
                        selectedPolygon.setColor(JColorChooser.showDialog(null, "Choose a color", POLYGONS_DEFAULT_COLOR));

                        //update color button
                        btnColor.setBackground(selectedPolygon.getColor());
                        panColorChange.setBackground(btnColor.getBackground());
                        //update pattern
                        System.out.println("++");
                        createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                    }else{
                        btnColor.setBackground(POLYGONS_DEFAULT_COLOR);
                        panColorChange.setBackground(POLYGONS_DEFAULT_COLOR);
                    }
                });
                panColorChange.add(btnColor);

                btnRename = new JButton("Rename");
                btnRename.setBackground(Color.darkGray);
                btnRename.setEnabled(false);
                btnRename.addActionListener(e->{
                    selectedPolygon.setLabel(JOptionPane.showInputDialog(null, "New name:", selectedPolygon.getLabel()));

                });
                panColorChange.add(btnRename);


                btnDelete = new JButton("Delete");
                btnDelete.setBackground(new Color(134, 0, 6));
                btnDelete.setEnabled(false);
                btnDelete.addActionListener(e-> {
                    polygons.remove(selectedPolygon);
                    comboBox.removeItem(selectedPolygon);
                    createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                });
                panColorChange.add(btnDelete);

                panColorChange.add(Box.createHorizontalStrut(10));

                btnVisible = new JButton(CLOSED_EYE);
                btnVisible.setBackground(Color.darkGray);
                btnVisible.setEnabled(false);
                btnVisible.addActionListener(e -> {
                    if(selectedPolygon.visible) {
                        selectedPolygon.setVisible(false);
                        btnVisible.setIcon(CLOSED_EYE);
                    }else{
                        selectedPolygon.setVisible(true);
                        btnVisible.setIcon(OPEN_EYE);
                    }

                    createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                });
                panColorChange.add(btnVisible);

                panPolygonSettings.add(Box.createVerticalStrut(5));

                JPanel panPositionOfSelectedPolygon = new JPanel();
                panPositionOfSelectedPolygon.setBackground(Color.lightGray);
                panPositionOfSelectedPolygon.setLayout(new BoxLayout(panPositionOfSelectedPolygon, BoxLayout.Y_AXIS));
                panPolygonSettings.add(panPositionOfSelectedPolygon);
                panPositionOfSelectedPolygon.add(new JLabel("Transformation:"));


                xsOfPolygon = new JSlider(SwingConstants.HORIZONTAL, -10, 10, 0);
                ysOfPolygon = new JSlider(SwingConstants.HORIZONTAL, -10, 10, 0);
                xsOfPolygon.setEnabled(false);
                ysOfPolygon.setEnabled(false);

                xsOfPolygon.addChangeListener(e->{
                    JSlider slider = (JSlider) e.getSource();
                    if(!comboBox.getSelectedItem().equals("Select polygon")) {
                        int val = slider.getValue();
                        selectedPolygon.translate(val, 0);
                        createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                    }
                    slider.setValue(0);
                });

                ysOfPolygon.addChangeListener(e->{
                    JSlider slider = (JSlider) e.getSource();
                    if(!comboBox.getSelectedItem().equals("Select polygon")) {
                        int val = slider.getValue();
                        selectedPolygon.translate(0, val);
                        createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                    }
                    slider.setValue(0);
                });

                JPanel panXsPosition = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panXsPosition.setBackground(Color.lightGray);
                xsOfPolygon.setBackground(Color.lightGray);
                panPositionOfSelectedPolygon.add(panXsPosition);
                panXsPosition.add(new JLabel("X:"));
                panXsPosition.add(xsOfPolygon);

                JPanel panYsPosition = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panYsPosition.setBackground(Color.lightGray);
                ysOfPolygon.setBackground(Color.lightGray);
                panPositionOfSelectedPolygon.add(panYsPosition);
                panYsPosition.add(new JLabel("Y:"));
                panYsPosition.add(ysOfPolygon);

                JPanel panAngle = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panPositionOfSelectedPolygon.add(panAngle);
                panAngle.setBackground(Color.lightGray);
                panAngle.add(new JLabel("Deg.:"));
                sliderDegrees = new JSlider(JSlider.HORIZONTAL, 1, 45, 10);
                sliderDegrees.setEnabled(false);
                sliderDegrees.setBackground(Color.lightGray);
                sliderDegrees.addChangeListener(e-> {
                    selectedPolygon.setAngleToRotate(sliderDegrees.getValue());
                    createPattern((Graphics2D)MouseHandler.bufferStrategy.getDrawGraphics());
                });
                panAngle.add(sliderDegrees);

                panPolygonPreview = new PolygonPreviewPanel(new myPolygon(new int[]{1, 0, 0}, new int[]{1, 0, 0}, null), center);
                panPolygonSettingsAndPreview.add(panPolygonPreview, BorderLayout.CENTER);

//                panPositionOfSelectedPolygon.add(Box.createHorizontalStrut(20));
//                panPositionOfSelectedPolygon.add(ysOfPolygon);





            JPanel panCenterPatterSetting = new JPanel();
            panCenterPatterSetting.setLayout(new BoxLayout(panCenterPatterSetting, BoxLayout.Y_AXIS));
            panCenterPatterSetting.setBackground(Color.darkGray);
            panSettings.add(panCenterPatterSetting, BorderLayout.CENTER);




        menuFrame.setVisible(true);


        //add Canvas and create buffer strategy
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.darkGray);
        mainFrame.add(canvas);
        canvas.createBufferStrategy(1);
        BufferStrategy bs = canvas.getBufferStrategy();



//        add Listeners
        canvas.addMouseListener(new MouseHandler(bs));

        mainFrame.invalidate();
        mainFrame.validate();
        mainFrame.repaint();


    }

    public static void addPoint(Graphics2D g, int x, int y){
        MouseHandler.g = (Graphics2D) MouseHandler.bufferStrategy.getDrawGraphics();
        g = MouseHandler.g;

        //label point on canvas
        g.setColor(NEWPOINT_COLOR);
        g.fillOval(x-4/2, y-4/2, 4, 4);

        //add point to arraylist
        points.add(new Point(x, y));

    }

    public static void createPattern(Graphics2D g){

        //create polygon from points
        if(points.size()>1) {
            int xs[] = new int[points.size()], ys[] = new int[points.size()];
            for (int i = 0; i < points.size(); i++) {
                xs[i] = points.get(i).x;
                ys[i] = points.get(i).y;
            }
            polygons.add(new myPolygon(xs, ys, POLYGONS_DEFAULT_COLOR));
            comboBox.addItem(polygons.get(polygons.size() - 1));
        }

        //remove points from points array
        points.clear();

        //clean background
        g.setColor(backgroundColor);
        g.fillRect(0, 0, mainFrame.getWidth(), mainFrame.getHeight());

        //make sure central point exist
        if(center == null)
            center = new Point(mainFrame.getWidth()/2, mainFrame.getHeight()/2);

        //create pattern
//        int totalAngle = 0;
//        while(totalAngle<360){
//            for(myPolygon p : polygons) {
//                g.setColor(p.getColor());
//                g.draw(p);
//            }
//
//            g.rotate(stepAngle, center.x, center.y);
//            totalAngle+=stepAngle;
//        }

        for(myPolygon p : polygons){
            if(p.visible) {
                int totalAngle = 0;
                while (totalAngle < 360) {
                    g.setColor(p.getColor());
                    g.draw(p);

                    g.rotate(p.getAngleToRotate(), center.x, center.y);
                    totalAngle += p.angleToRotate;
                }
                g.rotate(360 - totalAngle, center.x, center.y);
            }
        }

        menuFrame.invalidate();
        menuFrame.validate();
        menuFrame.repaint();

    }

//    public createPreviewPatternForSelectedPolygon(myPolygon polygon)
}

