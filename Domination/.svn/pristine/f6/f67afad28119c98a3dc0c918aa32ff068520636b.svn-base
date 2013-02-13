package net.yura.domination.mapstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.WeakHashMap;
import javax.microedition.lcdui.Image;
import net.yura.cache.Cache;
import net.yura.domination.ImageManager;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.ButtonGroup;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.List;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.components.RadioButton;
import net.yura.mobile.gui.components.TextComponent;
import net.yura.mobile.gui.layout.XULLoader;
import net.yura.mobile.gui.plaf.LookAndFeel;
import net.yura.mobile.gui.plaf.SynthLookAndFeel;
import net.yura.mobile.io.FileUtil;
import net.yura.mobile.logging.Logger;
import net.yura.mobile.util.ImageUtil;
import net.yura.mobile.util.Properties;
import net.yura.mobile.util.Url;
import net.yura.swingme.core.CoreUtil;

/**
 * @author Yura Mamyrin
 */
public class MapChooser implements ActionListener,MapServerListener {

    // Nathans server
    //public static final String SERVER_URL="http://maps.domination.yura.net/xml/"
    //public static final String MAP_PAGE=SERVER_URL+"maps.dot";
    //public static final String CATEGORIES_PAGE=SERVER_URL+"categories.dot";

    // yura test server
    //public static final String SERVER_URL="http://domination.sf.net/maps2/maps/";
    //public static final String MAP_PAGE=SERVER_URL+"";
    //public static final String CATEGORIES_PAGE=SERVER_URL+"maps.xml";
    
    // theos server
    public static final String SERVER_URL="http://maps.yura.net/";
    public static final String MAP_PAGE=SERVER_URL+"maps?format=xml&version="+Url.encode( Risk.RISK_VERSION );
    public static final String CATEGORIES_PAGE=SERVER_URL+"categories?format=xml&version="+Url.encode( Risk.RISK_VERSION );


    // these are both weak caches, they only keep a object if someone else holds it or a key
    private static ImageManager iconCache = new ImageManager( adjustSizeToDensityFromMdpi(150),adjustSizeToDensityFromMdpi(94) ); // 150x94
    private static java.util.Map mapCache = new WeakHashMap();
    private static Cache repo;
    static {
        try {
            repo = new Cache("net.yura.domination");
        }
        catch (Throwable ex) {
            System.err.println("[MapChooser] no cache: "+ex);
        }
    }
    
    private Properties resBundle = CoreUtil.wrap(TranslationBundle.getBundle());

    private XULLoader loader;
    private ActionListener al;
    protected MapServerClient client;

    private java.util.List mapfiles;
    private List list;

    public static void loadThemeExtension() {
        try {
            LookAndFeel laf = DesktopPane.getDesktopPane().getLookAndFeel();
            if (laf instanceof SynthLookAndFeel) {
                ((SynthLookAndFeel)laf).load( Midlet.getResourceAsStream("/ms_tabbar.xml") );
            }
            else {
                System.err.println("LookAndFeel not SynthLookAndFeel "+laf);
            }
        }
        catch(Exception ex) {
            // this is a none faital error, we will go on
            RiskUtil.printStackTrace(ex);
        }
    }
    
    public MapChooser(ActionListener al,java.util.List mapfiles,boolean mapStore) {
        this.al = al;
        this.mapfiles = mapfiles;

        try {
            loader = XULLoader.load( Midlet.getResourceAsStream("/ms_maps.xml") , this, resBundle);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }

        Panel TabBar = (Panel)loader.find("TabBar");
        
        if (TabBar!=null) {
        
            if (mapStore) {
            
                java.util.List buttons = TabBar.getComponents();

                Icon on,off;

                try {
                    on = new Icon("/ms_bar_on.png");
                    off = new Icon("/ms_bar_off.png");
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                int w = off.getIconWidth() / buttons.size();
                for (int c=0;c<buttons.size();c++) {
                    RadioButton b = (RadioButton)buttons.get(c);
                    Icon oni = on.getSubimage(c*w, 0, w, off.getIconHeight());
                    Icon offi = off.getSubimage(c*w, 0, w, off.getIconHeight());

                    b.setIcon(offi);
                    b.setSelectedIcon(oni);
                    b.setRolloverIcon(offi);
                    b.setRolloverSelectedIcon(oni);

                    b.setToolTipText( b.getText() );

                    b.setText("");
                    b.setMargin(0);

                }
            }
            else {
                TabBar.setVisible(false);
            }
        }

        list = (List)loader.find("ResultList");
        if (Midlet.getPlatform() == Midlet.PLATFORM_ME4SE) {
            list.setDoubleClick(true);
        }
        list.setCellRenderer( new MapRenderer(this) );
        list.setFixedCellHeight( XULLoader.adjustSizeToDensity(75) ); // mdpi = 100
        list.setFixedCellWidth(10); // will streach

        client = new MapServerClient(this);
        client.start();

        activateGroup("MapView");

        MapUpdateService.getInstance().addObserver( (BadgeButton)loader.find("updateButton") );
        
    }

    public void destroy() {
        
        MapUpdateService.getInstance().deleteObserver( (BadgeButton)loader.find("updateButton") );
        
        client.kill();
        client=null;

    }

    public static Icon getLocalIconForMap(Map map) {
        return getIconForMapOrCategory(map, null, map.getPreviewUrl(), null);
    }
    
    /**
     * @param key can be a Map or a Category
     */
    public static Icon getIconForMapOrCategory(Object key,String context,String iconUrl,MapServerClient c) {
        Icon aicon = iconCache.get( key );
        if (aicon==null) {
            aicon = iconCache.newIcon(key);

            String url = getURL(context, iconUrl);

            // if this is a remote file
            if ( url.indexOf(':')>0 ) {
                InputStream in = repo!=null?repo.get(url):null;
                if (in!=null) {
                    gotImg(key, in);
                }
                else {
                    // can be null when shut down
                    if (c!=null) c.makeRequest( url,null,key );
                }
            }
            // if this is a locale file
            else {
                InputStream in=null;

                //Map map = (Map)key;
                //String mapName = map.getMapUrl();
                //java.util.Map info = RiskUtil.loadInfo(mapName, false);
                //String prv = (String)info.get("prv");
                //if (prv!=null) {

                if (url.startsWith("preview/")) {
                    try {
                        in = RiskUtil.openMapStream( url ); // "preview/"+prv
                    }
                    catch (Exception ex) {
                        Logger.warn(ex);
                    }
                }
                //if (in==null) {
                //    String pic = (String)info.get("pic");

                else {

                    in = repo!=null?repo.get(url):null;

                    if (in==null) {
                        try {
                            System.out.println("[MapChooser] ### Going to re-encode img: "+url);
                            InputStream min = RiskUtil.openMapStream(url);
                            Image img = MapChooser.createImage(min);                    
                            img = ImageUtil.scaleImage(img, 150, 94);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            ImageUtil.saveImage(img, out);
                            img = null; // drop the small image as soon as we can
                            byte[] bytes = out.toByteArray();
                            out = null; // drop the OutputStream as soon as we can
                            cache(url,bytes);
                            // TODO we should only cache if we are sure it can be opened as a image
                            in = new ByteArrayInputStream(bytes);
                        }
                        catch (OutOfMemoryError err) { // what can we do?
                            Logger.info(err);
                        }
                        catch (Exception ex) {
                            Logger.warn(ex);
                        }
                    }
                }

                if (in!=null) {
                    gotImg(key, in);
                }
            }
            
        }
        return aicon;
    }
    
    private static void gotImg(Object obj,InputStream in) {
        try {
            Image img = MapChooser.createImage(in);
            iconCache.gotImg(obj, img);
        }
        catch (Exception ex) {
            throw new RuntimeException("failed to decode img "+obj, ex);
        }
    }

    public static void gotImgFromServer(Object obj,String url, byte[] data,MapServerListener msl) {
        try {
            gotImg(obj, new ByteArrayInputStream(data) );

            if (msl!=null) {
                msl.publishImg(obj);
            }
        }
        catch (RuntimeException ex) {
            // there was some error with this image
            //ImageManager.gotImg(obj, null); // clear the lazy image, so we can try again
            // not needed as its a week ref and will clear soon enough anyway

            System.err.println("error in image from server with url: "+url);
            throw ex;
        }

        // only cache if publish works fine
        cache(url, data);
    }

    public void publishImg(Object key) {
            if (client!=null) { // if we have shut down, dont need to do anything
                list.repaint();
            }
    }
    
    private static void cache(String url,byte[] data) {
        if (repo!=null && !repo.containsKey( url ) ) {
            repo.put( url , data );
        }
    }
    

    public static int adjustSizeToDensityFromMdpi(int size) {
        return XULLoader.adjustSizeToDensity( (int)(size * 0.75 +0.5) );
    }

    

    public static Map createMap(String file) {
        
        WeakReference wr = (WeakReference)mapCache.get(file);
        if (wr!=null) {
            Map map = (Map)wr.get();
            if (map!=null) {
                return map;
            }
        }


        java.util.Map info = RiskUtil.loadInfo(file, false);

        Map map = new Map();
        map.setMapUrl( file );

        String name = (String)info.get("name");
        if (name==null) {
            if (file.toLowerCase().endsWith(".map")) {
                name = file.substring(0, file.length()-4);
            }
            else {
                name = file;
            }
        }
        map.setName(name);
        map.setDescription( (String)info.get("comment") );

        String prv = (String)info.get("prv");
        if (prv!=null) {
            prv = "preview/"+prv;
            if (!fileExists(prv)) {
                prv=null;
            }
        }

        if (prv==null) {
            prv = (String)info.get("pic");
        }
        map.setPreviewUrl( prv );

        map.setVersion( (String)info.get("ver") );
                
        mapCache.put(file, new WeakReference(map));
                
        return map;
        
    }
    
    public static String getFileUID(String mapUrl) {
            int i = mapUrl.lastIndexOf('/');
            return (i>=0)?mapUrl.substring(i+1):mapUrl;
    }
    
    void makeRequestForMap(String a,String b) {
        client.makeRequestXML( MAP_PAGE , a, b);
    }

    public void actionPerformed(String actionCommand) {
        if ("local".equals(actionCommand)) {
            mainCatList(actionCommand);

            java.util.List riskmaps = new java.util.Vector( mapfiles.size() );
            for (int c=0;c<mapfiles.size();c++) {
                String file = (String)mapfiles.get(c);

                // we create a Map object for every localy stored map
                Map map = createMap(file);
                
                riskmaps.add( map );
            }

            // we want to sort by name for the local map list
            Collections.sort(riskmaps, new Comparator() {
                public int compare(Object t1, Object t2) {
                    Map m1 = (Map)t1;
                    Map m2 = (Map)t2;
                    return String.CASE_INSENSITIVE_ORDER.compare(m1.getName(), m2.getName());
                }
            });
            
            setListData( null, riskmaps );

        }
        else if ("catagories".equals(actionCommand)) {
            mainCatList(actionCommand);

            client.makeRequestXML( CATEGORIES_PAGE , (String)null, (String)null);

        }
        else if ("top25".equals(actionCommand)) {
            mainCatList(actionCommand);

            activateGroup("Top25View");
        }
        else if ("search".equals(actionCommand)) {
            mainCatList(actionCommand);

            actionPerformed("doMapSearch");
        }
        else if ("update".equals(actionCommand)) {
            mainCatList(actionCommand);

            java.util.List mapsToUpdate = MapUpdateService.getInstance().mapsToUpdate;
            
            Component updateAll = loader.find("updateAll");
            if (mapsToUpdate.isEmpty()) {
                updateAll.setVisible(false);
                show("AllUpToDate");
            }
            else {
                updateAll.setVisible(true);
                setListData( MAP_PAGE , mapsToUpdate);
            }
        }
        else if ("updateall".equals(actionCommand)) {
            java.util.List mapsToUpdate = MapUpdateService.getInstance().mapsToUpdate;
            synchronized(mapsToUpdate) {
                for (int c=0;c<mapsToUpdate.size();c++) {
                    click( (Map)mapsToUpdate.get(c) );
                }
            }
        }
        else if ("TOP_NEW".equals(actionCommand)) {
            clearList();
            makeRequestForMap("sort","TOP_NEW" );
        }
        else if ("TOP_RATINGS".equals(actionCommand)) {
            clearList();
            makeRequestForMap("sort","TOP_RATINGS" );
        }
        else if ("TOP_DOWNLOADS".equals(actionCommand)) {
            clearList();
            makeRequestForMap("sort","TOP_DOWNLOADS" );
        }
        else if ("listSelect".equals(actionCommand)) {

            Object value = list.getSelectedValue();
            if (value instanceof Category) {
                Category cat = (Category)value;
                clearList();
                makeRequestForMap("category",cat.getId() );
            }
            else if (value instanceof Map) {
                Map map = (Map)value;
                click(map);
            }
            //else value is null coz the list is empty
        }
        else if ("sameAuthor".equals(actionCommand)) {
            Object value = list.getSelectedValue();
            // TODO
            // TODO its too hard to get to the right click menu, as you need to hold and wait
            // TODO does not work for locale maps, as no author id,
            // TODO does not make sense for categories
            // TODO
            if (value instanceof Map) {
                Map map = (Map)value;
                makeRequestForMap("author", map.getAuthorId() );
            }
        }
        else if ("defaultMap".equals(actionCommand)) {

            chosenMap( RiskGame.getDefaultMap() );

        }
        else if ("cancel".equals(actionCommand)) {

            al.actionPerformed(null);

        }
        else if ("doMapSearch".equals(actionCommand)) {
            TextComponent.closeNativeEditor();
            String text = ((TextComponent)loader.find("mapSearchBox")).getText();
            clearList();
            if (text != null && !"".equals(text)) {
                makeRequestForMap("search", text );
            }
            else {
                setListData(null, null);
            }
        }
        else {
            System.out.println("Unknown command "+actionCommand);
        }
    }
    
    public void click(Map map) {
        String fileUID = getFileUID( map.getMapUrl() );

        String context = ((MapRenderer)list.getCellRenderer()).getContext();

        if (context!=null) { // we have a context, this means this is a remote map

            if (client.isDownloading(fileUID)) { // we may be doing a update

                OptionPane.showMessageDialog(null, "already downloading", "message", 0);
            }
            else if ( mapfiles.contains(fileUID) ) {

                java.util.Map info = RiskUtil.loadInfo(fileUID, false);

                String ver = (String)info.get("ver");

                if (map.getVersion()!=null && !"".equals(map.getVersion()) && !"1".equals(map.getVersion()) && !map.getVersion().equals( ver ) ) {
                    // update needed!!!

                    client.downloadMap( getURL(context, map.mapUrl ) );
                    list.repaint();
                    return;
                }

                String pic = (String)info.get("pic");
                String crd = (String)info.get("crd");
                String imap = (String)info.get("map");
                String prv = (String)info.get("prv");

                if ( !fileExists(pic) || !fileExists(crd) || !fileExists(imap) || (prv!=null && !fileExists("preview/"+prv)) ) {
                    // we are missing a file, need to re-download this map

                    client.downloadMap( getURL(context, map.mapUrl ) );
                    list.repaint();
                    return;

                }

                // so we already have this map, just fire event to load it
                chosenMap(fileUID);

            }
            else {

                client.downloadMap( getURL(context, map.mapUrl ) );
                list.repaint();
            }
        }
        else { // this is a local map, we will fire the event right away that we got it

            chosenMap(fileUID);

        }

    }
    
    public static boolean fileExists(String fileUID) {
        
        java.io.InputStream file=null;
        try {
            file = RiskUtil.openMapStream(fileUID);
        }
        catch (Exception ex) { } // not found?
        finally{ net.yura.mobile.io.FileUtil.close(file); }
        
        return (file!=null); // we already have this file
    }
    
    private void chosenMap(String mapName) {
        
        selectedMap = mapName;
        al.actionPerformed(null);

    }
    
    public static String getURL(String context,String mapUrl) {
        
        if (mapUrl.indexOf(':')<0 && context!=null) { // we do not have a full URL, so we pre-pend the context
            if (mapUrl.startsWith("/")) {
                mapUrl = context.substring(0, context.indexOf('/', "http://.".length()) ) + mapUrl;
            }
            else {
                mapUrl = context + mapUrl;
            }
        }
        
        return mapUrl;
    }

    public void mainCatList(String actionCommand) {
        Enumeration group = ((ButtonGroup)loader.getGroups().get("MapView")).getElements();
        while (group.hasMoreElements()) {
            Button button = (Button)group.nextElement();
            String action = button.getActionCommand();
            Component panel = loader.find(action+"Bar");
            panel.setVisible( action.equals(actionCommand) );
        }

        clearList();

    }

    void clearList() {
        show("Loading");
    }

    public Panel getRoot() {
        return ((Panel)loader.getRoot());
    }

    private String selectedMap;
    public String getSelectedMap() {
        return selectedMap;
    }

    public void gotResultXML(String url,String method,Object param) {

        if ("categories".equals(method)) {
            if (param instanceof java.util.List) {
                setListData( url, (java.util.List)param );
            }
        }
        else if ("maps".equals(method)) {
            if (param instanceof java.util.Map) {
                java.util.Map map = (java.util.Map)param;

                map.get("search");
                map.get("author");
                map.get("category");

                map.get("offset");
                map.get("total");

                setListData( url, (java.util.List)map.get("maps") );
            }
        }

    }
    
    public void onXMLError(String error) {
        show("Error");
    }

    /**
     * currently this is for download or image errors
     */
    public void onDownloadError(String error) {
        // TODO make this better
        OptionPane.showMessageDialog(null, error , "Error!", OptionPane.ERROR_MESSAGE);
    }
    
    public void downloadFinished(String download) {

        if ( !this.mapfiles.contains( download ) ) {
            this.mapfiles.add( download );
        }

        if (((Button)loader.find("updateButton")).isSelected() && MapUpdateService.getInstance().mapsToUpdate.isEmpty()) {
            loader.find("updateAll").setVisible(false);
            show("AllUpToDate");
        }

        //else {
            // this must have been a update or re-download, no need to show message
            //OptionPane.showMessageDialog(null, "got map, but we already have it "+download, "error", 0);
        //}
    }

    public static String getContext(String url) {
        if (url!=null) {
            int i = url.lastIndexOf('/');
            if (i> "http://.".length() ) {
                url = url.substring(0, i+1);
            }
        }
        return url;
    }
    
    private void setListData(String url,java.util.List items) {
        String context = getContext(url);
        ((MapRenderer)list.getCellRenderer()).setContext(context);
        list.setListData( RiskUtil.asVector(items==null?Collections.EMPTY_LIST:items) );
        boolean showNoMatch = items!=null && items.isEmpty();
        show( showNoMatch?"NoMatches":"ResultList" );
    }

    private void show(String name) {

        Component loading = loader.find("Loading");
        Component noMatches = loader.find("NoMatches");
        Component allUpToDate = loader.find("AllUpToDate");
        Component error = loader.find("Error");

        list.setSelectedIndex(-1);
        if (list.getSize()>0) {
            list.ensureIndexIsVisible(0);
        }
        
        list.setVisible( "ResultList".equals(name) );
        noMatches.setVisible( "NoMatches".equals(name) );
        allUpToDate.setVisible( "AllUpToDate".equals(name) );
        loading.setVisible( "Loading".equals(name) );
        error.setVisible( "Error".equals(name) );

        getRoot().revalidate();
        getRoot().repaint();
    }

    private void activateGroup(String string) {

        String mincat = ((ButtonGroup)loader.getGroups().get(string)).getSelection().getActionCommand();
        actionPerformed(mincat);

    }
    
    public boolean willDownload(Map map) {
        
        String mapUID = MapChooser.getFileUID( map.getMapUrl() );
        
        // if we dont have a local file with the same uid
        if ( !mapfiles.contains( mapUID ) ) {
            return true;
        }

        return MapUpdateService.getInstance().mapsToUpdate.contains(map);
    }

    /**
     * @see net.yura.domination.engine.RiskUIUtil#read(java.io.InputStream) 
     */
    public static Image createImage(InputStream in) throws IOException {
        try {
            Image img = Image.createImage(in);
            if (img==null) {
                throw new IOException("Image.createImage returned null");
            }
            return img;
        }
        finally {
            FileUtil.close(in);
        }
    }

}
