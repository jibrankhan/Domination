package net.yura.domination.mobile.flashgui;

import javax.microedition.lcdui.Image;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.border.Border;
import net.yura.mobile.gui.components.Component;

/**
 * @author Yura
 */
public class BackgroundBorder implements Border {

    Image img;
    
    public BackgroundBorder(Image i) {
        img = i;
    }
    
    public void paintBorder(Component cmpnt, Graphics2D g, int w, int h) {

        double s = Math.max(h/(double)img.getHeight(),w/(double)img.getWidth());
        
        int dw = (int) (img.getWidth() * s);
        int dh = (int) (img.getHeight()* s);

        g.drawScaledImage(img, (w - dw) / 2, (h - dh) / 2, dw, dh);
        
    }

    public int getTop() {
        return 0;
    }

    public int getBottom() {
        return 0;
    }

    public int getRight() {
        return 0;
    }

    public int getLeft() {
        return 0;
    }

    public boolean isBorderOpaque() {
        return true;
    }
    
}
