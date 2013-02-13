package net.yura.domination.mapstore;

import java.util.List;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;

/**
 * @author Yura Mamyrin
 */
public class GetMap implements MapServerListener {

    MapServerClient client;
    Risk myrisk;
    String filename;
    Exception problem;
    
    public static void getMap(String filename, Risk risk,Exception ex) {
        GetMap get = new GetMap();
        get.filename = filename;
        get.myrisk = risk;
        get.problem = ex;
        get.client = new MapServerClient(get);
        get.client.start();
        get.client.makeRequestXML( MapChooser.MAP_PAGE,"mapfile",filename );
    }

    private void onError(String exception) {
        myrisk.getMapError(exception);
        client.kill();
    }

    public void gotResultXML(String url, String method, Object param) {
        java.util.Map map = (java.util.Map)param;
        List maps = (List)map.get("maps");
        if (maps.size()==1) {
            Map themap = (Map)maps.get(0);
            client.downloadMap( MapChooser.getURL(MapChooser.getContext(url), themap.mapUrl ) );
        }
        else {
            System.err.println( "wrong number of maps on server: "+maps.size()+" for map: "+filename );

            RiskUtil.printStackTrace(problem);
            onError(problem.toString());
        }
    }

    public void downloadFinished(String mapUID) {
        try {
            myrisk.setMap(mapUID);
            client.kill();
        }
        catch (Exception ex) {
            RiskUtil.printStackTrace(ex);
            onError(ex.toString());
        }
    }

    public void onXMLError(String string) {
        onError(string);
    }

    public void onDownloadError(String string) {
        onError(string);
    }

    public void publishImg(Object param) { }

}
