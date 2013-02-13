package net.yura.domination.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * instead of using java.awt.Color
 * works on Desktop java and Android
 * @author Yura Mamyrin
 */
public class ColorUtil {

        public static final int BLACK       = 0xFF000000,
                                WHITE       = 0xFFFFFFFF,
                                LIGHT_GRAY  = 0xFFC0C0C0,
                                GRAY        = 0xFF808080,
                                DARK_GRAY   = 0xFF404040,
                                RED         = 0xFFFF0000,
                                PINK        = 0xFFFFAFAF,
                                ORANGE      = 0xFFFFC800,
                                YELLOW      = 0xFFFFFF00,
                                GREEN       = 0xFF00FF00,
                                MAGENTA     = 0xFFFF00FF,
                                CYAN        = 0xFF00FFFF,
                                BLUE        = 0xFF0000FF;

	static Map intToString = new HashMap();
	static Map stringToInt = new HashMap();
	static {
            add(BLACK,"black");
            add(BLUE,"blue");
            add(CYAN,"cyan");
            add(DARK_GRAY,"darkgray");
            add(GRAY,"gray");
            add(GREEN,"green");
            add(LIGHT_GRAY,"lightgray");
            add(MAGENTA,"magenta");
            add(ORANGE,"orange");
            add(PINK,"pink");
            add(RED,"red");
            add(WHITE,"white");
            add(YELLOW,"yellow");
	}

	static void add(int color,String name) {
	    Integer c = new Integer(color);
	    intToString.put(c, name);
	    stringToInt.put(name, c);
	}

	public static String getStringForColor(int c) {
	    String result = (String)intToString.get(new Integer(c));
	    if (result != null) {
	        return result;
            }
            return getHexForColor(c);
	}


	/**
         * get a color as a int from a string, alpha is set to 255
         * if we can not get the color then we return 0
	 */
	public static int getColor(String nm) {

		Integer color = (Integer)stringToInt.get(nm);
                if (color!=null) {
                    return color.intValue();
                }

		try {

                    Integer result = Integer.decode(nm);

                    // the 0xff000000 | means the alpha is 255
                    return 0xff000000 | result.intValue();
		}
		catch(Exception ex) {

			//System.out.print("Error: unable to find color "+s+".\n"); // testing
			return 0;
		}
	}

	public static int getTextColorFor(int c) {

/*
if ( c.getRed() < 100 && c.getBlue() < 100 && c.getGreen() < 100 ) {
return Color.white;
}
else {
return Color.black;
}



int r = c.getRed();
int g = c.getGreen();
int b = c.getBlue();

if (r > 240 || g > 240) {
return Color.black;
}
else {
return Color.white;
}
*/


		int r = getRed(c);
		int g = getGreen(c);
		// int b = c.getBlue();


		if ((r > 240 || g > 240) || (r > 150 && g > 150)) {
			return BLACK;
		}
		else {
			return WHITE;
		}

	}

        /**
         * Returns the red component in the range 0-255 in the default sRGB
         * space.
         * @return the red component.
         * @see #getRGB
         */
        public static int getRed(int rgb) {
            return (rgb >> 16) & 0xFF;
        }

        /**
         * Returns the green component in the range 0-255 in the default sRGB
         * space.
         * @return the green component.
         * @see #getRGB
         */
        public static int getGreen(int rgb) {
            return (rgb >> 8) & 0xFF;
        }

        /**
         * Returns the blue component in the range 0-255 in the default sRGB
         * space.
         * @return the blue component.
         * @see #getRGB
         */
        public static int getBlue(int rgb) {
            return (rgb >> 0) & 0xFF;
        }

        /**
         * Returns the alpha component in the range 0-255.
         * @return the alpha component.
         * @see #getRGB
         */
        public static int getAlpha(int rgb) {
            return (rgb >> 24) & 0xff;
        }

        public static String getHexForColor(int c) {
                return "#" + Integer.toHexString((  c & 0xffffff) | 0x1000000).substring(1);
        }

}
