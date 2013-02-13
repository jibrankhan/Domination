package net.yura.domination.mapstore;

/**
 * @author Yura Mamyrin
 */
public interface MapServerListener {

    public void gotResultXML(String url, String method, Object param);
    public void onXMLError(String string);

    public void downloadFinished(String mapUID);
    public void onDownloadError(String string); // (map file download errors)

    public void publishImg(Object key); // image has been set to the icon for a Map/Category
}
