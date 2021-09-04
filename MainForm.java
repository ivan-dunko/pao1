import sun.security.util.ArrayUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MainForm {
    private JPanel panel1;
    private JPanel botPanel;
    JLabel label;
    private JScrollPane scrollPane;
    private BufferedImage origImage, image;
    private JLabel redLabel, greenLabel, blueLabel;
    private JLabel hueLabel, saturLabel, valueLabel;
    private JLabel LLabel, aLabel, bLabel;
    private final static double Xn = 95.04, Yn = 100, Zn = 108.88;
    private JSlider hueSlider, saturSlider, valueSlider;

    private static final Matrix cieMat = new Matrix(3, 3, new double[]{
            0.4124564, 0.3575761, 0.1804375,
            0.2126729, 0.7151522, 0.0721750,
            0.0193339, 0.1191920, 0.9503041
    });

    MainForm() throws IOException, URISyntaxException {
        JFrame frame = new JFrame("PAO1");
        try {

            String fname = getClass().getClassLoader().getResource("Lenna.png").getFile();
            System.out.println(fname);
            File f = new File(fname);

            //origImage = ImageIO.read(f);
            origImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Lenna.png"));
            image = new BufferedImage(origImage.getColorModel(),
                    origImage.copyData(null),
                    origImage.isAlphaPremultiplied(), null);
        } catch (Exception e) {
            e.printStackTrace();
            origImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
        label = new JLabel(new ImageIcon(image));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int xpos = e.getX(), ypos = e.getY();
                if (xpos >= image.getWidth() || ypos >= image.getHeight())
                    return;

                int rgb = image.getRGB(e.getX(), e.getY());
                Color c = new Color(rgb);
                redLabel.setText("Red: " + c.getRed());
                greenLabel.setText("Green: " + c.getGreen());
                blueLabel.setText("Blue: " + c.getBlue());

                double hue = HSVColor.getHue(c);
                double satur = HSVColor.getSatur(c);
                double value = HSVColor.getV(c);

                hueLabel.setText("Hue: " + hue);
                saturLabel.setText("Saturation: " + satur);
                valueLabel.setText("Value: " + value);

                Vector xyz = cieMat.mul(new Vector(new double[]{c.getRed(), c.getGreen(), c.getBlue()})).
                        mul(1.0 / 255.0);
                double x = colorFun(xyz.get(0)), y = colorFun(xyz.get(1)), z = colorFun(xyz.get(2));

                double L = 116.0 * f(y / Yn) - 16.0;
                double a = 500.0 * (f(x / Xn) - f(y / Yn));
                double b = 200.0 * (f(y / Yn) - f(z / Zn));

                LLabel.setText("L*: " + L);
                aLabel.setText("a*: " + a);
                bLabel.setText("b*: " + b);
            }
        });

        scrollPane = new JScrollPane(label);

        //botPanel = new JPanel();

        //botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.Y_AXIS));
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.add(scrollPane);
        //topPanel.add(new JLabel("Text"));

        redLabel = new JLabel("Red:");
        greenLabel = new JLabel("Green:");
        blueLabel = new JLabel("Blue:");
        hueLabel = new JLabel("Hue:");
        saturLabel = new JLabel("Saturation:");
        valueLabel = new JLabel("Value:");
        LLabel = new JLabel();
        aLabel = new JLabel();
        bLabel = new JLabel();
        hueSlider = new JSlider(0, 360);
        saturSlider = new JSlider(0, 100);
        valueSlider = new JSlider(0, 100);
        hueSlider.setMajorTickSpacing(60);
        hueSlider.setValue(0);
        hueSlider.setMinorTickSpacing(30);
        hueSlider.setPaintLabels(true);
        hueSlider.setPaintTicks(true);

        saturSlider.setMajorTickSpacing(10);
        saturSlider.setMinorTickSpacing(5);
        saturSlider.setValue(0);
        saturSlider.setPaintLabels(true);
        saturSlider.setPaintTicks(true);


        valueSlider.setMajorTickSpacing(10);
        valueSlider.setMinorTickSpacing(5);
        valueSlider.setValue(0);
        valueSlider.setPaintLabels(true);
        valueSlider.setPaintTicks(true);
        /*
        hueSlider.addChangeListener(new SliderChange());
        saturSlider.addChangeListener(new SliderChange());
        valueSlider.addChangeListener(new SliderChange());
        */

        JButton openFile = new JButton("Open...");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                int retVal = fc.showOpenDialog(frame);

                if (retVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        origImage = ImageIO.read(fc.getSelectedFile());
                        image = new BufferedImage(origImage.getColorModel(),
                                origImage.copyData(null),
                                origImage.isAlphaPremultiplied(), null);
                        label.setIcon(new ImageIcon(image));
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        JButton applyHSV = new JButton("Apply HSV");
        applyHSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeHSV();
            }
        });

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(openFile);
        innerPanel.add(redLabel);
        innerPanel.add(greenLabel);
        innerPanel.add(blueLabel);
        innerPanel.add(new JLabel("- - -"));
        innerPanel.add(hueLabel);
        innerPanel.add(saturLabel);
        innerPanel.add(valueLabel);
        innerPanel.add(new JLabel("- - -"));
        innerPanel.add(LLabel);
        innerPanel.add(aLabel);
        innerPanel.add(bLabel);
        innerPanel.add(Box.createVerticalStrut(40));
        innerPanel.add(new JLabel("Hue"));
        innerPanel.add(hueSlider);
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(new JLabel("Saturation"));
        innerPanel.add(saturSlider);
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(new JLabel("Value"));
        innerPanel.add(valueSlider);
        innerPanel.add(applyHSV);
        innerPanel.add(Box.createVerticalStrut(40));
        JButton lHistButton = new JButton("Show L hist");
        lHistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int IMG_W = 540, IMG_H = 540;
                final int CANV_W = 512, CANV_H = 512;
                Image img = new BufferedImage(520, IMG_H, BufferedImage.TYPE_INT_RGB);
                Graphics g = img.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, IMG_W, IMG_H);

                int[] hist = new int[101];
                for (int i = 0; i < image.getWidth(); ++i) {
                    for (int j = 0; j < image.getHeight(); ++j) {
                        double L = getL(new Color(image.getRGB(i, j)));
                        hist[(int) Math.round(L)]++;
                    }
                }

                double dx = (double) CANV_W / 101.0;
                int maxy = -1;
                for (int x : hist)
                    maxy = Integer.max(maxy, x);

                Color[] cols = {Color.BLUE, Color.magenta, Color.ORANGE};
                for (int i = 0; i < 101; ++i) {
                    g.setColor(cols[i % 3]);
                    int h = (int) (495 * ((double) hist[i] / maxy));
                    g.fillRect((int) (i * dx),
                            CANV_H - h,
                            (int) dx,
                            h);

                }

                g.setColor(Color.BLACK);
                for (int i = 0; i <= 10; ++i) {
                    int x = (int) (10 * i * dx);
                    int y = 505;
                    g.drawLine(x + (int) (dx * 0.5), CANV_H - 5, x + (int) (dx * 0.5), CANV_H + 5);
                    g.drawString(String.valueOf(i * 10), x, CANV_H + 15);

                }

                JDialog dial = new LHistDialog(img);
                dial.pack();
                dial.setVisible(true);
            }
        });
        innerPanel.add(lHistButton);
        //panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        JScrollPane botScrollPane = new JScrollPane(innerPanel);
        panel1.add(botScrollPane);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*
        frame.getContentPane().setLayout(new GridLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(topPanel);
        */
        frame.getContentPane().add(panel1);
        //frame.setSize(new Dimension(image.getWidth(), image.getHeight()));
        frame.pack();
        frame.setVisible(true);
    }


    public static int getMax(Color c) {
        return Integer.max(Integer.max(c.getRed(), c.getGreen()), c.getBlue());
    }

    public static double colorFun(double x) {
        double res = 0.0;
        if (x > 0.04045)
            res = Math.pow((x + 0.055) / 1.055, 2.4);
        else
            res /= 12.92;

        return res * 100.0;
    }

    public static double f(double x) {
        if (x > Math.pow(6.0 / 29.0, 3.0))
            return Math.pow(x, 1.0 / 3.0);
        return (1.0 / 3.0) * Math.pow(29.0 / 6.0, 2.0) * x + 4.0 / 29.0;
    }

    public static double getL(Color c) {
        Vector xyz = cieMat.mul(new Vector(new double[]{c.getRed(), c.getGreen(), c.getBlue()})).
                mul(1.0 / 255.0);
        double x = colorFun(xyz.get(0)), y = colorFun(xyz.get(1)), z = colorFun(xyz.get(2));

        double L = 116.0 * f(y / Yn) - 16.0;
        return L;
    }

    private void changeHSV() {
        int dh = hueSlider.getValue();
        double ds = saturSlider.getValue() / 100.0;
        double dv = valueSlider.getValue() / 100.0;

        int dism = 0;
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                Color oldRGB = new Color(origImage.getRGB(i, j));
                HSVColor hsv = HSVColor.RGB2HSV(oldRGB);
                /*
                if (!oldRGB.equals(HSVColor.HSV2RGB(hsv))){
                    dism++;
                    System.out.println(oldRGB + ";" + HSVColor.HSV2RGB(hsv));
                }

                 */

                hsv.shiftHSV(dh, ds, dv);

                Color newRGB = HSVColor.HSV2RGB(hsv);
                image.setRGB(i, j, newRGB.getRGB());
            }
        }

        System.out.println(dism);

        label.repaint();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-12324328));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    class SliderChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            changeHSV();
        }
    }

    public static void main(String[] args) {
        try {
            new MainForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
