package net.yura.domination.mapstore;

import java.util.Observable;
import java.util.Observer;
import javax.microedition.lcdui.Graphics;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.border.Border;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.RadioButton;
import net.yura.mobile.gui.plaf.Style;

/**
 * @author Yura
 */                             // needs to be RadioButton to support different icon states
public class BadgeButton extends RadioButton implements Observer {

    Border border;
    String badge="0"; // default is 0, and it will not draw anything when set to 0
    
    public BadgeButton() {
        
        try {
            border = DesktopPane.getDefaultTheme("Badge").getBorder(Style.ALL);
        }
        catch (Exception ex) { }

        setHorizontalAlignment(Graphics.HCENTER);
    }

    public void paint(Graphics2D g) {
        super.paint(g);
        
        int w = getWidth();
        g.translate(w, 0);
        MapUpdateService.paintBadge(g,badge,border );
        g.translate(-w, 0);
    }

    public void update(Observable o, Object arg) {
        badge = String.valueOf(arg);
        Component parent = getParent();
        if (parent!=null) {
            parent.repaint();
        }
    }

    public String getDefaultName() {
        return "Button";
    }

    protected void toggleSelection() {
        if (buttonGroup==null) {
            setSelected(false); // act like normal button
        }
        else {
            super.toggleSelection();
        }
    }

}
