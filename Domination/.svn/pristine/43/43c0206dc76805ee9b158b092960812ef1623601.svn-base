package net.yura.domination.engine.guishared;

import java.awt.Container;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.mapstore.MapUpdateService;

/**
 * @author Yura
 */
public class BadgeButton extends JButton implements Observer {

    Object border;
    String badge="0"; // default is 0, and it will not draw anything when set to 0
    
    public BadgeButton(String string) {
        super(string);
        
        try {
            border = net.yura.mobile.gui.border.MatteBorder.load9png( javax.microedition.lcdui.Image.createImage("/ms_ic_notification_overlay.9.png") );
        }
        catch (Throwable ex) { }

        try {
            MapUpdateService.getInstance().addObserver(this);
        }
        catch (Throwable ex) { }
    }

    public void paint(Graphics g) {
        super.paint(g);
        
        try {
            int overlap = 5;

            int w = getWidth();
            g.translate(w+overlap, -overlap);
            MapUpdateService.paintBadge(new net.yura.mobile.gui.Graphics2D( new javax.microedition.lcdui.Graphics(g) ),badge,(net.yura.mobile.gui.border.Border)border );
            g.translate(-w-overlap, +overlap);
        }
        catch (Throwable th) {
            RiskUtil.printStackTrace(th);
        }
    }

    public void update(Observable o, Object arg) {
        badge = String.valueOf(arg);
        Container parent = getParent();
        if (parent!=null) {
            parent.repaint();
        }
    }

}
