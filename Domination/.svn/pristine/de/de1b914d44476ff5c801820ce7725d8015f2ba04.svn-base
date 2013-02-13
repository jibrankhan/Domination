package net.yura.domination.tools.mapeditor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.mapstore.Map;
import net.yura.domination.mapstore.MapChooser;
import net.yura.domination.mapstore.MapUpdateService;
import net.yura.domination.mapstore.gen.XMLMapAccess;
import net.yura.mobile.io.ServiceLink.Task;
import net.yura.mobile.io.UTF8InputStreamReader;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

/**
 * @author Yura
 */
public class MapsTools {
    
    public static final String MAPS_XML_FILE = "maps.xml";

    public static List loadMaps() {

        try {

            File mapsDir = RiskUIUtil.getSaveMapDir();
            File xml = new File(mapsDir,MAPS_XML_FILE);

            if (xml.exists()) {

                XMLMapAccess access = new XMLMapAccess();
                
                Task task = (Task)access.load( new InputStreamReader(new FileInputStream(xml), "UTF-8") );
                
                // load big XML file
                List maps = (List)task.getObject();
        
                return maps;
            }
            else {
                // only make a blank empty vector if there is no current file
                return new java.util.Vector();
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        
    }
    
    public static void saveMaps(List maps) {
        
        try {
            
            Task task = new Task("categories", maps);
        
            File mapsDir = RiskUIUtil.getSaveMapDir();
            File xml = new File(mapsDir,MAPS_XML_FILE);

            XMLMapAccess access = new XMLMapAccess();
            
            access.save( new FileOutputStream(xml) , task);
            
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
    
//    public static void saveMapsHTML(Vector maps) {
//        String a = "<div class='thumbnail'><a href=\"maps/haiti.zip\">"
//                + "<img src=\"images/maps/haiti.jpg\" border=\"1\" width=\"150\" height=\"94\"><br>"
//                + "29. Haiti Map</a><br> by Louis-Pierre Charbonneau </div>";
//    }

    public static Map findMap(List maps,String fileName) {

        // find entry fot this map
        for (int c=0;c<maps.size();c++) {

            Map map = (Map)maps.get(c);

            if (fileName.equals( map.getMapUrl() )) {

                // yay we found the correct map
                return map;
            }
        }
    
        return null;
    }

    public static String getSafeMapID(String file) {

        if (file.toLowerCase().endsWith(".map")) {
            file = file.substring(0, file.length()-4);
        }
        
        int i;
        while ((i=file.indexOf(' '))>=0) {
            file = file.substring(0, i)+
            ((i<(file.length()-1) )?Character.toUpperCase(file.charAt(i+1))+file.substring(i+2):"");
        }
        
        return file;
    }
    
    public static final String PREVIEW = "preview";
    
    public static String makePreview(String mapUID, BufferedImage prvimg,File previewDir, String format) throws Exception {

            // we do NOT have a preview, we need to generate one
            String prv = getSafeMapID(mapUID)+"."+format;

            boolean done = ImageIO.write( prvimg , format , new File(previewDir,prv) );

            if (!done) throw new Exception("not done "+format);

            return PREVIEW+"/"+prv;

    }
    
    
    static String publish(Map map, String[] myCategoriesIds) {
        
            try {     

                File mapsDir = RiskUIUtil.getSaveMapDir();
                
                File zipFile = new File(mapsDir, getSafeMapID(map.getMapUrl())+".zip" );
                if (zipFile.exists() && !zipFile.delete()) {
                    throw new RuntimeException("can not del "+zipFile);
                }
                    
                java.util.Map info = RiskUtil.loadInfo( map.getMapUrl() , false);

                List files = new java.util.Vector();
                files.add( map.getMapUrl() );
                files.add( info.get("pic") );
                files.add( info.get("map") );

                String cardsFile = (String)info.get("crd");
                if (!"risk.cards".equals(cardsFile) && !"nomission.cards".equals(cardsFile)) { // these 2 files come with ALL installs
                    files.add( cardsFile );
                }

                String preview = (String)info.get("prv");
                if (preview!=null) {
                    files.add( PREVIEW+"/"+preview );
                }

                makeZipFile(zipFile, mapsDir, (String[])files.toArray( new String[files.size()] ));
                
                // create the multipart request and add the parts to it
                MultipartEntity requestContent = new MultipartEntity();
                
                requestContent.addPart("first_name", makeStringBody( map.getAuthorName() ));
                requestContent.addPart("email", makeStringBody( map.getAuthorId() ));
                
                requestContent.addPart("name", makeStringBody( map.getName() ));
                requestContent.addPart("description", makeStringBody( map.getDescription() ));
                
                for (int c=0;c<myCategoriesIds.length;c++) {
                    requestContent.addPart("categories", makeStringBody( myCategoriesIds[c] ) );
                }
                
                requestContent.addPart("mapZipFile", new FileBody(zipFile));

                return doPost( MapChooser.SERVER_URL+"upload-unauthorised", requestContent );

            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
    }
    
    static StringBody makeStringBody(String string) {
        try {
            return new StringBody(string, Charset.forName("UTF-8"));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void makeZipFile(File zipFile,File root, String[] files) {
        
        
        byte[] buf = new byte[1024];

        try {
            // Create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

            // Compress the files
            for (int i=0; i<files.length; i++) {
                FileInputStream in = new FileInputStream( new File(root, files[i] ));

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry( files[i] ));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        
    }
    
    
        public static String doPost(String url, MultipartEntity requestContent) throws IOException {

        	StringBuffer buffer = new StringBuffer();

            	URLConnection conn = new URL(url).openConnection();
        	conn.setDoOutput(true);

                org.apache.http.Header contentType = requestContent.getContentType();
                conn.setRequestProperty(contentType.getName(), contentType.getValue());

                // this seems to be null and does not seem to be needed
                //org.apache.http.Header contentEncoding = requestContent.getContentEncoding();
                //conn.setRequestProperty(contentEncoding.getName(), contentEncoding.getValue());

                conn.setRequestProperty("Content-Length", String.valueOf(requestContent.getContentLength()) );

                OutputStream out = conn.getOutputStream();
                requestContent.writeTo( out );
        	out.close();

        	// Get the response
        	BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        	String line;
        	while ((line = rd.readLine()) != null) {
			buffer.append(line);
                        buffer.append("\n");
        	}
        	rd.close();
		return buffer.toString();

    }

    public static Map getOnlineMap(String uid) {
        List maps = MapUpdateService.getMaps(MapChooser.MAP_PAGE,Arrays.asList( new String[] {uid} ));
        return maps.size()>0 ? (Map)maps.get(0) : null;
    }
        
    public static List getCategories() {
        try {
            URLConnection conn = new URL( MapChooser.CATEGORIES_PAGE ).openConnection();
            XMLMapAccess access = new XMLMapAccess();
            InputStream in = conn.getInputStream();
            net.yura.mobile.io.ServiceLink.Task result = (net.yura.mobile.io.ServiceLink.Task)access.load( new UTF8InputStreamReader( in ) );
            in.close();
            return (List)result.getObject();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
