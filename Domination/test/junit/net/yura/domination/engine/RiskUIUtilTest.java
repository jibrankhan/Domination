package net.yura.domination.engine;

import java.io.File;
import junit.framework.TestCase;
import net.yura.domination.tools.mapeditor.MapEditor;
import net.yura.domination.tools.mapeditor.MapsTools;

/**
 * @author Yura Mamyrin
 */
public class RiskUIUtilTest extends TestCase {
    
    public RiskUIUtilTest(String testName) {
        super(testName);
    }

    public void testGetURL() {
        System.out.println("getURL");

        assertEquals("http://yura.net/", RiskUIUtil.getURL("http://yura.net/"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("hello http://yura.net/ world"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("hello http://yura.net/\nworld"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("hello http://yura.net/"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("hello http://yura.net/ "));
        assertEquals("http://yura.net/", RiskUIUtil.getURL(" http://yura.net/"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("hello http://yura.net/\n"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("\n\nhttp://yura.net/\n\n"));
        assertEquals("http://yura.net/", RiskUIUtil.getURL("  http://yura.net/  "));
        assertEquals(null, RiskUIUtil.getURL("hello world"));
        assertEquals(null, RiskUIUtil.getURL(""));
    }
    
    public void testGetSafeMapID() {
        System.out.println("getSafeMapID");
        
        assertEquals("bob", MapsTools.getSafeMapID("bob.map"));
        assertEquals("bobTheBuilder", MapsTools.getSafeMapID("bob the builder.map"));
        assertEquals("bobTheBuilder", MapsTools.getSafeMapID("bobTheBuilder.map"));
        assertEquals("bob", MapsTools.getSafeMapID("bob .map"));
        assertEquals("Bob", MapsTools.getSafeMapID(" bob.map"));
        assertEquals("bob", MapsTools.getSafeMapID("bob"));
        assertEquals("bob", MapsTools.getSafeMapID("bob "));
        assertEquals("bob", MapsTools.getSafeMapID("bob   "));
        assertEquals("bobTheBuilder", MapsTools.getSafeMapID("bob   the   builder"));
        assertEquals("", MapsTools.getSafeMapID("   "));
        assertEquals("", MapsTools.getSafeMapID(""));
    }

    public void testGetExtension() {
        System.out.println("getExtension");
        
        assertEquals("jpeg", MapEditor.getExtension( new File("file.jpeg") ));
        assertEquals("jpg", MapEditor.getExtension( new File("file.something.jpg") ));
        assertEquals("", MapEditor.getExtension( new File(".file") ));
        assertEquals("", MapEditor.getExtension( new File("file") ));
        assertEquals("JPG", MapEditor.getExtension( new File("Something.JPG") ));
        assertEquals("PNG", MapEditor.getExtension( new File(".FILE.2.PNG") ));
    }
    
}
