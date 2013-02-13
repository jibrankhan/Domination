package net.yura.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yura Mamyrin
 */
public class Cache {

    // as this class is only used on j2se and android, we should use proper logging
    static final Logger logger = Logger.getLogger(Cache.class.getName());
    
    public static final boolean DEBUG = false;
    File cacheDir;
    
    public Cache(String appName) {

        String tmpDir = System.getProperty("java.io.tmpdir");
        
        cacheDir = new File(new File(tmpDir),appName+".cache");
        
        if (!cacheDir.isDirectory() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can not make cache dir: "+cacheDir);
        }

        if (DEBUG) {
            logger.log(Level.INFO, "[yura.net Cache] starting {0}", cacheDir);
        }
        
    }
    
    private File getFileName(String uid) {
        try {
            String fileName = URLEncoder.encode(uid, "UTF-8");
            return new File(cacheDir, fileName);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    public void put(String key, byte[] value) {
        File file = getFileName(key);
        if (file.exists()) {
            logger.log(Level.WARNING, "[yura.net Cache] already has file: {0}", key);
        }
        else {
            try {

                if (DEBUG) {
                    logger.log(Level.INFO, "[yura.net Cache] saving to cache: {0}", key);
                }

                FileOutputStream out = new FileOutputStream(file);
                out.write(value);
                out.close();
            }
            catch (Exception ex) {
                boolean exists = file.exists();
                boolean deleted = false;
                if (exists) {
                    deleted = file.delete();
                }
                logger.log(Level.WARNING, "failed to save data to file: "+file+" exists="+exists+" deleted="+deleted+" key: "+key+" in dir "+cacheDir+" exists="+cacheDir.exists(), ex);
            }
        }
    }
    
    public InputStream get(String key) {
        File file = getFileName(key);
        if (file.exists()) {
            try {
                if (DEBUG) {
                    logger.log(Level.INFO, "[yura.net Cache] getting from cache: {0}", key);
                }
                
                file.setLastModified(System.currentTimeMillis());
                return new FileInputStream(file);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            if (DEBUG) {
                logger.log(Level.INFO, "[yura.net Cache] key not found: {0}", key);
            }
        }
        return null;
    }

    public boolean containsKey(String key) {
        File file = getFileName(key);
        return file.exists();
    }

}
