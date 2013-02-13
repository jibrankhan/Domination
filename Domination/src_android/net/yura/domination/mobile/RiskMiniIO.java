package net.yura.domination.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.microedition.io.Connector;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskIO;
import net.yura.domination.engine.RiskUtil;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.io.FileUtil;

/**
 * @author Yura Mamyrin
 */
public class RiskMiniIO implements RiskIO {

    public static String mapsdir = "file:///android_asset/maps/";

    public InputStream openStream(String name) throws IOException {
        return Connector.openInputStream("file:///android_asset/"+name);
    }

    public InputStream openMapStream(String name) throws IOException {
        try {
            File userMaps = MiniUtil.getSaveMapDir();
            File newFile = new File(userMaps, name);
            return new FileInputStream(newFile);
        }
        catch (Exception ex) {
            try {
                return FileUtil.getInputStreamFromFileConnector(mapsdir+name);
            }
            catch (Exception ex2) {
                IOException exception = new IOException( ex2.toString() );
                exception.initCause(ex); // in android 1.6
                throw exception;
            }
        }
    }

    public ResourceBundle getResourceBundle(Class c, String n, Locale l) {
        return ResourceBundle.getBundle(c.getPackage().getName()+"."+n, l );
    }

    public void openURL(URL url) throws Exception {
        Midlet.openURL(url.toString());
    }

    public void openDocs(String doc) throws Exception {
        Midlet.openURL("file:///android_asset/" + doc );
    }

    public InputStream loadGameFile(String fileUrl) throws Exception {
        return FileUtil.getInputStreamFromFileConnector(fileUrl);
    }

    public void saveGameFile(String fileUrl, Object obj) throws Exception {
        OutputStream fileout = FileUtil.getWriteFileConnection(fileUrl).openOutputStream();
        ObjectOutputStream objectout = new ObjectOutputStream(fileout);
        objectout.writeObject(obj);
        objectout.close();
    }

    public OutputStream saveMapFile(String fileName) throws Exception {
        return RiskUtil.getOutputStream( MiniUtil.getSaveMapDir(), fileName);
    }

    public void getMap(String filename, Risk risk, Exception ex) {
        net.yura.domination.mapstore.GetMap.getMap(filename, risk, ex);
    }

    public void renameMapFile(String oldName, String newName) {
        File oldFile = new File( MiniUtil.getSaveMapDir() ,oldName);
        File newFile = new File( MiniUtil.getSaveMapDir() ,newName);
        RiskUtil.rename(oldFile, newFile);
    }

}
