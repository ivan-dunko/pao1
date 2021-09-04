import java.awt.*;

public class HSVColor {
    private double h, s, v;

    HSVColor(double h, double s, double v){
        this.h = h;
        this.s = s;
        this.v = v;
    }

    public double getH() {
        return h;
    }

    public double getS() {
        return s;
    }

    public double getV() {
        return v;
    }

    public static double getHue(Color c){
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        double max = Double.max(Double.max(r, g), b);
        double min = Double.min(Double.min(r, g), b);
        double hue = 0;

        double denum = max - min;
        double num = 0.0;
        double factor = 60.0;
        double shft = 0.0;

        if (r == max && g >= b){
            num = (double) (g - b);
        }
        else if (r == max && g < b) {
            num = g - b;
            shft = 360.0;
        }
        else if (g == max){
            num = b - r;
            shft = 120.0;
        }
        else{
            num = r - g;
            shft = 240.0;
        }

        hue = factor * num / denum + shft;

        return hue;
    }

    public static double getSatur(Color c){
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        if (Integer.max(Integer.max(r, g), b) == 0)
            return 0;

        double max = Double.max(Double.max(r, g), b);
        double min = Double.min(Double.min(r, g), b);

        return 1.0 - min / max;
    }

    public static double getV(Color c){
        return (double)MainForm.getMax(c) / 255.0;
    }

    public static HSVColor RGB2HSV(Color color){
        double h = getHue(color);
        double s = getSatur(color);
        double v = getV(color);

        return new HSVColor(h, s, v);
    }

    public static Color HSV2RGB(HSVColor color){
        double h = color.h, s = color.s, v = color.v;

        /*
        double c = v *s ;
        int tmp = 1 - Math.abs(((int)h / 60) % 2 - 1);
        double x = c * tmp;
        double m = v - c;

        double r_, g_, b_;
        if (h >=0 && h < 60){
            r_ = c;
            g_ = x;
            b_ = 0;
        }
        else if (h >= 60 && h < 120){
            r_ = x;
            g_ = c;
            b_ = 0;
        }
        else if (h >= 120 && h < 180){
            r_ = 0;
            g_ = c;
            b_ = x;
        }
        else if (h >= 180 && h < 240){
            r_ = 0;
            g_ = x;
            b_ = c;
        }
        else if (h >= 240 && h < 300){
            r_ = x;
            g_ = 0;
            b_ = c;
        }
        else {
            r_ = c;
            g_ = 0;
            b_ = x;
        }

        int r = (int) Math.round((r_ + m) * 255.0);
        int g = (int) Math.round((g_ + m) * 255.0);
        int b = (int) Math.round((b_ + m) * 255.0);

        return new Color(r, g, b);
         */

        s *= 100.0;
        v *= 100.0;
        int hi = (int)h / 60;
        double vmin = (100.0 - s) * v / 100.0;
        double a = (v - vmin) * ((double)((int)h % 60) / 60.0);
        double vinc = vmin + a;
        double vdec = v - a;
        double r = 0, g = 0, b = 0;

        switch (hi){
            case 0:{
                r = v;
                g = vinc;
                b = vmin;
                break;
            }
            case 1:{
                r = vdec;
                g = v;
                b = vmin;
                break;
            }
            case 2:{
                r = vmin;
                g = v;
                b = vinc;
                break;
            }
            case 3:{
                r = vmin;
                g = vdec;
                b = v;
                break;
            }
            case 4:{
                r = vinc;
                g = vmin;
                b = v;
                break;
            }
            case 5:{
                r = v;
                g = vmin;
                b = vdec;
                break;
            }
        }

        try {
            int R = (int) Math.round(r * 2.55);
            int G = (int) Math.round(g * 2.55);
            int B = (int) Math.round(b * 2.55);

            return new Color(R, G, B);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //return new Color(R,G,B);
        return new Color(0, 0, 0);
    }

    public HSVColor shiftHSV(double dh, double ds, double dv){
        h += dh;
        if (h > 360.0)
            h -= 360.0;
        s += ds;
        if (s > 1.0)
            s -= 1.0;
        v += dv;
        if (v > 1.0)
            v -= 1.0;

        return this;
    }
}
