package net.yura.domination.mapstore;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.mobile.gui.Animation;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.cellrenderer.DefaultListCellRenderer;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.ProgressBar;
import net.yura.mobile.gui.plaf.Style;

/**
 * @author Yura Mamyrin
 */
public class MapRenderer extends DefaultListCellRenderer {

    String line2;

    private ProgressBar bar = new ProgressBar();
    private Component list;
    
    private String context;
    
    Image play,download;
    Icon loading;
    
    MapChooser chooser;
    Map map;
    
    public MapRenderer(MapChooser chooser) {
        
        this.chooser = chooser;
        setName("ListRendererCollapsed"); // get rid of any padding

        Sprite spin1 = getSprite( "/ms_strip.png" , 8, 1 );
        bar.setSprite(spin1);
        bar.workoutPreferredSize();
        //add(bar); // YURA do we need this???
        
        play = Midlet.createImage("/ms_play.png");
        download = Midlet.createImage("/ms_download.png");
        
        loading = new Icon("/ms_icon_loading.png");

    }
    
    public static Sprite getSprite(String name,int cols,int rows) {
        Image img = Midlet.createImage(name);
        int w = img.getWidth()/cols;
        int h = img.getHeight()/rows;
        if ( img.getWidth() % w != 0 || img.getHeight() % h != 0) {
            img = Image.createImage(img, 0, 0, w*cols, h*rows, 0);
        }
        return new Sprite(img, w, h); // 29x29
    }
    
    
    public void animate() {
        bar.animate();

        if (list!=null) {
            list.repaint();
        }
    }
    
    
    public void setContext(String c) {
        context = c;
    }
    
    public String getContext() {
        return context;
    }

    public Component getListCellRendererComponent(Component list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        this.list = list;
        
        line2 = null; // reset everything
        map = null;
        String iconUrl=null;

        if (value instanceof Category) {
            Category category = (Category)value;

            setText( category.getName() );
            
            iconUrl = category.getIconURL();
        }
        else if (value instanceof Map) {
            map = (Map)value;

            setText( map.getName() );
            
            String author = map.getAuthorName();
            if (author!=null && !"".equals(author)) {
                line2 = TranslationBundle.getBundle().getString("mapchooser.by").replaceAll("\\{0\\}", author);
            }
            String description = map.getDescription();
            if (description!=null && !"".equals(description)) {
                line2 = (line2==null?"":line2+"\n")+description;
            }

            if (line2!=null) {
                line2 = getFirstLines(line2,4);
            }
            
            iconUrl = map.getPreviewUrl();
        }
        // else just do nothing

        setVerticalTextPosition( line2==null?Graphics.VCENTER:Graphics.TOP);
        
        //iconUrl = "http://www.imagegenerator.net/clippy/image.php?question="+map.getName();

        if (iconUrl!=null) {
            setIcon( MapChooser.getIconForMapOrCategory(value,context,iconUrl,chooser.client) );
        }
        else {
            System.out.println("[MapRenderer] No PreviewUrl for map or category: "+value);
        }

        return c;
    }

    public void paintComponent(Graphics2D g) {
        
        Icon icon = getIcon();
        if (icon==null || icon.getImage()==null) {
            setIcon(loading);
        }

        super.paintComponent(g);
        
        if (line2!=null) {
            Icon i = getIcon();

            int state = getCurrentState();

            // if NOT focused or selected
            if ( (state&Style.FOCUSED)==0 && (state&Style.SELECTED)==0 ) {
                g.setColor( theme.getForeground(Style.DISABLED) );
            }

            g.drawString(line2, padding + (i!=null?i.getIconWidth()+gap:0), padding + getFont().getHeight() + gap);
        }

        if (map!=null) {
        
            int gap = 5;

            String mapUID = MapChooser.getFileUID( map.getMapUrl() );
            
            if ( chooser.client.isDownloading( mapUID ) ) { // we need to check for this first as we may have it and also be updating it

                // position spinner in top right corner
                int x = getWidth()-bar.getWidth()-gap;
                int y = gap;

                g.translate(x, y);

                bar.paintComponent(g);

                g.translate(-x, -y);


                // its ok to register more than once
                Animation.registerAnimated(this);
            }
            else if ( chooser.willDownload( map ) ) {
                g.drawImage(download, getWidth()-download.getWidth()-gap, gap);
            }
            else {
                g.drawImage(play, getWidth()-play.getWidth()-gap, gap);
            }
        }
        
        setIcon(icon);
    }

    public void workoutMinimumSize() {
        super.workoutMinimumSize();

        // TODO could be done better!
        if (line2!=null && getIcon()==null ) {
            height = height + getFont().getHeight()+gap;
        }
    }

    public static String getFirstLines(String input,int lines) {
        int lastchar=0;
        for (int c=0;c<lines;c++) {
            int newline = input.indexOf('\n', lastchar);
            if (newline<0) {
                return input;
            }
            lastchar = newline+1;
        }
        return input.substring(0, lastchar-1)+"...";
    }
    
}
