/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.mapstore;

import java.io.File;
import java.util.List;
import java.util.Vector;
import junit.framework.TestCase;
import net.yura.domination.engine.RiskUIUtil;

/**
 *
 * @author yuramamyrin
 */
public class MapUpdateServiceTest extends TestCase {
    
    
    
    /*
     * possible things to test:
     * 
     *  downloading a new map
     *  downloading a update to an existing map (existing map has a older version)
     *  downloading a map when the existing map is missing a file
     * 
     */



    public MapUpdateServiceTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of init method, of class MapUpdateService.
     */
    public void testInit() throws Exception {
        System.out.println("init");

        RiskUIUtil.mapsdir = new File("./game/Domination/maps").toURI().toURL();
        
        List mapsUIDs = new Vector();

        // ALL THESE MAPS NEED TO ACTUALLY BE IN THE MAPS DIR
        mapsUIDs.add("RiskEurope.map");
        mapsUIDs.add("ameroki.map");
        mapsUIDs.add("eurasien.map");
        mapsUIDs.add("geoscape.map");
        mapsUIDs.add("lotr.map");
        mapsUIDs.add("luca.map");
        mapsUIDs.add("risk.map");
        mapsUIDs.add("roman_empire.map");
        mapsUIDs.add("sersom.map");
        mapsUIDs.add("teg.map");
        mapsUIDs.add("tube.map");
        mapsUIDs.add("uk.map");
        mapsUIDs.add("world.map");
        
        MapUpdateService instance = MapUpdateService.getInstance();
        instance.init(mapsUIDs,"http://mapsqa.yura.net/maps?format=xml");
        
        List result = instance.mapsToUpdate;
        
        Vector check = new Vector();
        check.add("ameroki.map");
        
        testResultContains(result,check);
        
    }

    private void testResultContains(List result, List check) {
        assertEquals( check.size(), result.size() );
        
        for (int c=0;c<check.size();c++) {
            
            String name = (String)check.get(c);
            boolean found=false;
            
            for (int a=0;a<result.size();a++) {
                Map map = (Map)result.get(a);
                
                if (map.getMapUrl().endsWith("/"+name)) {
                    found = true;
                }
            }
            
            if (!found) {
                fail("not found: "+name);
            }
            
        }
        
    }

}
