package net.yura.domination.ui.flashgui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import javax.swing.border.Border;

/**
 * @author Yura Mamyrin
 */
public class FlashBorder implements Border {

    Image top,bottom,left,right;
    
    public FlashBorder(Image top, Image left, Image bottom, Image right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        
        // top left
        drawImage(g,top,x,y,0,0,left.getWidth(c),top.getHeight(c),c);
        
        // top right
        drawImage(g,top,x+width-right.getWidth(c),y,top.getWidth(c)-right.getWidth(c),0,right.getWidth(c),top.getHeight(c),c);

        // bottom left
        drawImage(g,bottom,x,y+height-bottom.getHeight(c),0,0,left.getWidth(c),bottom.getHeight(c),c);
        
        // bottom right
        drawImage(g,bottom,x+width-right.getWidth(c),y+height-bottom.getHeight(c),bottom.getWidth(c)-right.getWidth(c),0,right.getWidth(c),bottom.getHeight(c),c);

        // top
        g.drawImage(top,
                x+left.getWidth(c), y, x+width-right.getWidth(c), y+top.getHeight(c),
                left.getWidth(c), 0, top.getWidth(c)-right.getWidth(c), top.getHeight(c),
                c);
        
        // bottom
        g.drawImage(bottom,
                x+left.getWidth(c), y+height-bottom.getHeight(c), x+width-right.getWidth(c), y+height,
                left.getWidth(c), 0, bottom.getWidth(c)-right.getWidth(c), bottom.getHeight(c),
                c);

        // left
        g.drawImage(left,
                x, y+top.getHeight(c), x+left.getWidth(c), y+height-bottom.getHeight(c),
                0, 0, left.getWidth(c), left.getHeight(c),
                c);

        // right
        g.drawImage(right,
                x+width-right.getWidth(c), y+top.getHeight(c), x+width, y+height-bottom.getHeight(c),
                0, 0, right.getWidth(c), right.getHeight(c),
                c);    
    }
    
    private void drawImage(Graphics g, Image img, int dest_x, int dest_y, int src_x, int src_y, int w, int h,ImageObserver c) {
        g.drawImage(img, dest_x, dest_y, dest_x+w, dest_y+h, src_x, src_y, src_x+w, src_y+h, c);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(top.getHeight(c), left.getWidth(c), bottom.getHeight(c), right.getWidth(c));
    }

    public boolean isBorderOpaque() {
        return true;
    }
    
}
