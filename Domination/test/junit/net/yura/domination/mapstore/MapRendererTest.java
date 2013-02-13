package net.yura.domination.mapstore;

import junit.framework.TestCase;

/**
 * @author Yura Mamyrin
 */
public class MapRendererTest extends TestCase {
    
    public MapRendererTest(String testName) {
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
     * Test of getFirstLines method, of class MapRenderer.
     */
    public void testGetFirstLines() {
        System.out.println("getFirstLines");

        String test1 = "hello string";
        String test2 = "hello string\nbob the builder";
        String test3 = "hello string\nbob the builder\n";
        String test4 = "hello string\nbob\nthe\nbuilder";
        String test5 = "hello string\n\n\nbob\na";

        assertEquals(test1,MapRenderer.getFirstLines(test1, 1));
        assertEquals(test1,MapRenderer.getFirstLines(test2, 1));
        assertEquals(test1,MapRenderer.getFirstLines(test3, 1));
        assertEquals(test1,MapRenderer.getFirstLines(test4, 1));
        assertEquals(test1,MapRenderer.getFirstLines(test5, 1));

        assertEquals(test1,MapRenderer.getFirstLines(test2, 1));
        assertEquals(test2,MapRenderer.getFirstLines(test2, 2));
        assertEquals(test2,MapRenderer.getFirstLines(test2, 3));
        assertEquals(test2,MapRenderer.getFirstLines(test2, 4));
        assertEquals(test2,MapRenderer.getFirstLines(test2, 5));
        
        assertEquals(test1,MapRenderer.getFirstLines(test3, 1));
        assertEquals(test2,MapRenderer.getFirstLines(test3, 2));
        assertEquals(test3,MapRenderer.getFirstLines(test3, 3));
        assertEquals(test3,MapRenderer.getFirstLines(test3, 4));
        assertEquals(test3,MapRenderer.getFirstLines(test3, 5));
        
        assertEquals(test1,MapRenderer.getFirstLines(test1, 5));
        assertEquals(test2,MapRenderer.getFirstLines(test2, 5));
        assertEquals(test3,MapRenderer.getFirstLines(test3, 5));
        assertEquals(test4,MapRenderer.getFirstLines(test4, 5));
        assertEquals(test5,MapRenderer.getFirstLines(test5, 5));
        
    }
}
