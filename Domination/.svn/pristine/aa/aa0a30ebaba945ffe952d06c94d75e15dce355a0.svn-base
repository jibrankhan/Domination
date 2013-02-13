package net.yura.domination.mapstore;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.yura.domination.mapstore.gen.XMLMapAccess;
import net.yura.mobile.gui.Font;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.border.Border;
import net.yura.mobile.io.ServiceLink.Task;
import net.yura.mobile.util.Url;

/**
 * @author Yura
 */
public class MapUpdateService extends Observable {

    static final Logger logger = Logger.getLogger(MapUpdateService.class.getName());
    
    static MapUpdateService updateService;
    
    List mapsToUpdate = new java.util.Vector();

    private MapUpdateService() { }
    public static MapUpdateService getInstance() {
        if (updateService==null) {
            updateService = new MapUpdateService();
        }
        return updateService;
    }

    void notifyListeners() {
        setChanged();
        notifyObservers( new Integer( mapsToUpdate.size() ) );
    }

    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        o.update(this, new Integer( mapsToUpdate.size() ) );
    }

    public void init(List mapsUIDs,String url) {
        
        List gotMaps = getMaps(url, mapsUIDs);
        
        for (int c=0;c<mapsUIDs.size();c++) {
            String uid = (String)mapsUIDs.get(c);
            List theMaps = new ArrayList(1);

            for (int i=0;i<gotMaps.size();i++) {
                Map themap = (Map)gotMaps.get(i);
                String mapUID = MapChooser.getFileUID( themap.getMapUrl() );
                if (mapUID.equals( uid )) { // we found the map
                    theMaps.add(themap);
                    // we do NOT break, just in case there is more then one
                }
            }
            
            if (theMaps.size()==1) {
                Map remoteMap = (Map)theMaps.get(0);
                String ver = remoteMap.getVersion();
                if (ver!=null && !"".equals(ver) && !"1".equals(ver) && !ver.equals( MapChooser.createMap(uid).getVersion() ) ) { // versions do not match, and update is needed
                    mapsToUpdate.add(remoteMap);
                    notifyListeners();
                    //client.downloadMap( MapChooser.getURL(MapChooser.getContext(url), themap.mapUrl ) ); // download 
                }
            }
            // else if 0 then we did not find it, or if more then 1 then some error has happened 
        }
    }
    
    public static List getMaps(String url,List mapsUIDs) {
    
        StringBuffer payload=new StringBuffer();
        
        for (int c=0;c<mapsUIDs.size();c++) {
            String uid = (String)mapsUIDs.get(c);
            if (payload.length()!=0) {
                payload.append('&');
            }
            payload.append( Url.encode("mapfile") );
            payload.append('=');
            payload.append( Url.encode(uid) );
        }

// we print this just in case we get any errors so we know what we sent
System.out.println("URL: "+url+" payload: "+payload);

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( payload.toString() );
            wr.close();

            Task task = (Task)new XMLMapAccess().load( new InputStreamReader(conn.getInputStream(),"UTF-8") );

//System.out.println("got: "+task);
            
            java.util.Map map = (java.util.Map)task.getObject();
            return (List)map.get("maps");
        }
        catch (Throwable ex) {
            logger.log(Level.INFO, "error in getting map versions", ex);
            return Collections.EMPTY_LIST;
        }
    }
    
    public void downloadFinished(String mapUID) {
        for (int c=0;c<mapsToUpdate.size();c++) {
            Map map = (Map)mapsToUpdate.get(c);
            String amapUID = MapChooser.getFileUID( map.getMapUrl() );
            if (mapUID.equals(amapUID)) {
                mapsToUpdate.remove(c);
                notifyListeners();
                return;
            }
        }
    }

    /**
     * TODO not sure where this method should be, but prob not here!!
     */
    public static void paintBadge(Graphics2D g,String badge,Border border) {
        
        if (!"0".equals(badge)) {
        
            Font font = g.getFont();
            
            int tw = font.getWidth(badge.length()==1?" "+badge:badge); // make sure its not too thin
            int th = font.getHeight();

            int l,r,t,b;
            if (border==null) {
                l=r=t=b=3;
            }
            else {
                l=border.getLeft();
                r=border.getRight();
                t=border.getTop();
                b=border.getBottom();
            }

            int x,y,w,h;

            w = l+r+tw;
            h = t+b+th;
            x = - w;
            y = 0;

            int[] clip = g.getClip();
            g.setClip(x, y, w, h);

            if (border==null) {
                g.setColor(0xFFFF0000);
                g.fillOval(x, y, w, h);
            }
            else {
                g.translate( x+border.getLeft() , y+border.getTop());
                border.paintBorder(null, g, w-border.getLeft()-border.getRight(), h-border.getTop()-border.getBottom());
                g.translate( -x-border.getLeft() , -y-border.getTop());
            }

            g.setColor(0xFFFFFFFF);
            g.drawString(badge, 1+ x + (w-font.getWidth(badge))/2, 1+ y + (h-font.getHeight())/2);
            
            g.setClip(clip);
        }

    }
}
