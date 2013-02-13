package net.yura.domination.mobile;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import android.graphics.ColorMatrix;
import collisionphysics.BallWorld;
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import java.util.ArrayList;
import java.util.List;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.MapPanel;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.mapstore.MapChooser;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.Font;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.KeyEvent;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.border.Border;
import net.yura.mobile.gui.components.ImageView;
import net.yura.mobile.gui.plaf.Style;

/**
 * <p> Picture Panel </p>
 * @author Yura Mamyrin
 */

public class PicturePanel extends ImageView implements MapPanel {

        public final static int NO_COUNTRY = 255;

        public final static int PP_X = 677;
        public final static int PP_Y = 425;

        public final static int VIEW_CONTINENTS       = 0;
        public final static int VIEW_OWNERSHIP        = 1;
        public final static int VIEW_BORDER_THREAT    = 2;
        public final static int VIEW_CARD_OWNERSHIP   = 3;
        public final static int VIEW_TROOP_STRENGTH   = 4;
        public final static int VIEW_CONNECTED_EMPIRE = 5;

        private Risk myrisk;
        private int c1,c2,cc;
        private Font font;
        private String strCountry;

        // all the image data when the map is loaded
        private Image img;
        private Image tempimg;
        private byte[][] map;
        private CountryImage[] countryImages;

        private static final ColorMatrix HighLight;
        public static final ColorMatrix gray;
        static {

                // YURA YURA YURA MAYBE CHANGE 1.0F SO THAT FLAT COLORS HIGHLIGHT TOO
                                // 0-2  0-255
                float scale = 1.5f;
                float offset = 1.0f;
                HighLight = RescaleOp(scale, offset); // 1.5f, 1.0f, null
            
                gray = new ColorMatrix();
                gray.setSaturation(0);
        }

        /**
         * Creates an Picture Panel
         */
        public PicturePanel(Risk r) {

                getDesktopPane().IPHONE_SCROLL = true;

                myrisk=r;

                this.strCountry = TranslationBundle.getBundle().getString( "picturepanel.country");

                img = null;
                map = null;
                
                //setupSize(PicturePanel.PP_X , PicturePanel.PP_Y);

                setName("PicturePanel");
        }

        MouseListener ml;
        int x=-1000,y=-1000;
        public void processMouseEvent(int type, int x, int y, KeyEvent keys) {
            if (type == DesktopPane.PRESSED) {
                this.x=x;
                this.y=y;
            }
            else {
                DesktopPane dp = DesktopPane.getDesktopPane();
                if (DesktopPane.isAccurate(this.x, x, dp.inaccuracy) && DesktopPane.isAccurate(this.y, y, dp.inaccuracy)) {
                    if (type == DesktopPane.RELEASED) {
                        if (ml!=null) {
                            ml.click(x,y);
                        }

                        if (myrisk.getGame().getState() == RiskGame.STATE_GAME_OVER ) {
                            // toggle the animation
                            if (ballWorld==null) {
                                startAni();
                            }
                            else {
                                stopAni();
                            }
                        }

                    }
                }
                else {
                    x=-1000;
                    y=-1000;
                }
            }
        }

        public void addMouseListener(MouseListener ml) {
            this.ml = ml;
        }

        /**
         * Adds the images related to the game to the picture panel
         */
        public void load() throws IOException {

                RiskGame game = myrisk.getGame();

                // clean up before we load new images
                //original = null;
                countryImages = null;

                //System.out.print("loading: "+(game.getImagePic()).getAbsolutePath()+" "+(game.getImageMap()).getAbsolutePath() +" "+((Vector)game.getCountries()).size()+"\n");
                
                memoryLoad(
                        MapChooser.createImage(RiskUtil.openMapStream(game.getImageMap()) ),
                        MapChooser.createImage(RiskUtil.openMapStream(game.getImagePic()) )
                        );

        }

        public int getMapWidth() {
            return map.length;
        }
        public int getMapHeight() {
            return map[0].length;
        }
        
        public void memoryLoad(Image m, Image original) {

                cc=NO_COUNTRY;
                c1=NO_COUNTRY;
                c2=NO_COUNTRY;
            
                // ImageView vars
                imgW = original.getWidth();
                imgH = original.getHeight();
                int mW = m.getWidth();
                int mH = m.getHeight();


                RiskGame game = myrisk.getGame();
                int noc = game.getCountries().length;

                BALL_SIZE = game.getCircleSize();
                String density = System.getProperty("display.scaledDensity"); // use scaledDensity, as in the FontManager scaledDensity is also used
                float d = (density!=null)?Float.parseFloat(density):1.0F;
                if (Midlet.getPlatform() == Midlet.PLATFORM_ANDROID) {
                    font = new Font(javax.microedition.lcdui.Font.FACE_PROPORTIONAL,javax.microedition.lcdui.Font.STYLE_PLAIN, (int) -( (BALL_SIZE*0.75) /d +0.5) );
                }
                else {
                    font = theme.getFont( Style.ALL ); // TODO HACK any size fonts do not work on me4se!!
                }

                
                

                Image newImg;
                Image newTempimg;
                byte[][] newMap;

                if (map==null || map.length!=mW || map[0].length!=mH) {

                    setPreferredSize(mW,mH);

                    // clear out old values
                    img = null;
                    tempimg = null;
                    map = null;
                    
                    newImg = Image.createImage(mW, mH);
                    newTempimg = Image.createImage(mW, mH);
                    newMap = new byte[mW][mH];
                }
                else {
                    newImg = img;
                    newTempimg = tempimg;
                    newMap = map;
                    
                    img = null;
                    tempimg = null;
                    map = null;
                }
                
                
                
                { Graphics zg = newImg.getGraphics(); zg.drawImage(original, 0, 0, 0); }



                CountryImage[] newCountryImages = new CountryImage[noc];
                for (int c=0; c < noc; c++) {
                    newCountryImages[c] = new CountryImage();
                }

 

                int[] pixels = new int[m.getWidth()];

                CountryImage cci;
                // create a very big 2d array with all the data from the image map
                for(int y=0; y < m.getHeight(); y++) {

                        // load line by line to not use up too much mem
                        m.getRGB(pixels,0,m.getWidth(),0,y,m.getWidth(),1);

                        for(int x=0; x < m.getWidth(); x++) {

                                int num = pixels[ x ] & 0xff; // (m.getRGB(x,y))&0xff;

                                // sometimes pixels[ x ] gives alpha values other then 255,
                                // only on Android and only for some maps, (mapsqa Error.map)
                                // so white areas come out as alpha=0 r=0 g=0 b=0
                                // if this happens, set it to NO_COUNTRY
                                if (num==0) { num=NO_COUNTRY; }

                                // if ( num > noc && num !=NO_COUNTRY ) System.out.print("map error: "+x+" "+y+"\n"); // testing map

                                newMap[x][y]= (byte) (num - 128); // as byte is signed we have to use this

                                if ( num != NO_COUNTRY ) {

                                    	// avoid arrayOutOfBounds, throw a real error with more info
                                    	if (num>noc) { throw new CountryNotFoundException("Strange color found: "+num+" ("+Integer.toHexString(pixels[x])+") countries: "+noc+" at: "+x+","+y ); }

                                        cci = newCountryImages[num-1];

                                        if (x < cci.getX1() ) { cci.setX1(x); }
                                        if (x > cci.getX2() ) { cci.setX2(x); }

                                        if (y < cci.getY1() ) { cci.setY1(y); }
                                        if (y > cci.getY2() ) { cci.setY2(y); }
                                }

                        }
                }

                pixels = null;
                m=null;

                // create the bufferd image for each country
                for (int c=0; c < newCountryImages.length ; c++) {

                    cci = newCountryImages[c];

                    try {
                        int x1=cci.getX1();
//                      int x2=cci.getX2();
                        int y1=cci.getY1();
                        int y2=cci.getY2();
                        int w=cci.getWidth();
                        int h=cci.getHeight();

                        Image cimg = Image.createImage(w, h);
                        Graphics g = cimg.getGraphics();
                        g.drawRegion(original, x1, y1, w, h, 0, 0, 0, 0);
                        cci.setSourceImage( cimg );
                        
                        for(int y=y1; y <= y2; y++) {
                                for(int x=0; x < w; x++) {
                                        if (newMap[x+x1][y] + 128 != (c+1) ) {
                                                cimg.setRGB( x, (y-y1), 0); // clear the un-needed area!
                                        }
                                }
                        }
                    }
                    catch (RuntimeException ex) {
                        if ( cci.getWidth() < 0 || cci.getHeight() < 0) {
                            throw new CountryNotFoundException("c=" + c + " " + cci);
                        }
                        else {
                            // this wraps Caused by: java.lang.RuntimeException: Bitmap.createBitmap returned null for w=103 h=67 config=ARGB_8888 error=java.lang.OutOfMemoryError: Bitmap.createBitmap returned null
                            throw new RuntimeException( "Error creating CountryImages: c=" + c + " " + cci , ex);
                        }
                    }
                }

                // assign everything at the end
                countryImages = newCountryImages;
                img = newImg;
                tempimg = newTempimg;
                map = newMap;
        }
        
        public static class CountryNotFoundException extends RuntimeException {
            public CountryNotFoundException(String msg) { super(msg); }
        }

        protected void paintBorder(Graphics2D g) {

                Border b = getBorder();
                if (b != null) {
                    
                    double s = getScale();
                    int x = getImgX(s);
                    int y = getImgY(s);

                    int w = (int) (imgW * s);
                    int h = (int) (imgH * s);
                    
                    g.translate(x,y);
                    b.paintBorder(this, g,w,h);
                    g.translate(-x,-y);
                }
        }
        
        /**
         * Paints the components
         * @param g a Graphics object.
         */
        public void paintComponent(Graphics2D g) {

            super.paintComponent(g);

            try {

                if (img != null) {

                        //System.out.print("#################################################### Repainted\n");

                        //super.paintComponent(g);

                        Graphics g2 = g.getGraphics();

                        double s = getScale();
                        int x = getImgX(s);
                        int y = getImgY(s);

                        //System.out.println("scale: "+s);

                        g.translate(x,y);
                        g2.scale(s,s);

                        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g.drawImage(img,0,0);

                        int c1=this.c1,c2=this.c2,cc=this.cc; // take local copy to be thread safe
                        
                        if (c1 != NO_COUNTRY) {
                                drawHighLightImage(g, countryImages[c1-1]);
                        }

                        if (c2 != NO_COUNTRY) {
                                drawHighLightImage(g,countryImages[c2-1]);
                        }

                        if (cc != NO_COUNTRY) {
                                drawHighLightImage(g,countryImages[cc-1]);
                        }

                        g.setFont(font);
                        
                        drawArmies(g);

                        if (cc != NO_COUNTRY) {

                                String text = this.strCountry + " "+ myrisk.getCountryName( cc );
                                int w = font.getWidth(text);
                                int h = font.getHeight();

                                g.setColor( 0x96FFFFFF );
                                g.fillRect( 5 , 5, w+3, h+1 );

                                g.setColor( 0xFF000000 );
                                g.drawString(text, 6, 15);
                        }

                        g2.scale(1/s,1/s);
                        g.translate(-x,-y);
                }

            }
            catch(Exception e) { // an excpetion here really does not matter
                RiskUtil.printStackTrace(e);
            }

        }

        public int BALL_SIZE=20;

        /**
         * Paints the army components
         * @param g2 a 2D Graphics object.
         */
        public void drawArmies(Graphics2D g2) {

                DirectGraphics g = DirectUtils.getDirectGraphics(g2.getGraphics());

                RiskGame game = myrisk.getGame();

                Country[] v = game.getCountries();
                
                int state = game.getState();

                int r = BALL_SIZE/2;
                
                if (state==RiskGame.STATE_ROLLING || state==RiskGame.STATE_BATTLE_WON || state==RiskGame.STATE_DEFEND_YOURSELF) {

                        Country attacker = game.getAttacker();
                        Country defender = game.getDefender();

                        if (attacker!=null && defender!=null) {
                        
                            int a=attacker.getColor();
                            int b=defender.getColor();

                            drawHighLightImage(g2,countryImages[a-1]);
                            drawHighLightImage(g2,countryImages[b-1]);

                            int ac = attacker.getOwner().getColor();

                            int argb = colorWithAlpha( ac, 150 );

                            if ( Math.abs( attacker.getX() - defender.getX() ) > (map.length / 2) ) {

                                    if ( attacker.getX() > (map.length / 2) ) { // ie the attacker is on the right

                                        Polygon pol1 = makeArrow( attacker.getX(), attacker.getY(), defender.getX()+map.length, defender.getY(), BALL_SIZE );
                                        Polygon pol2 = makeArrow( attacker.getX()-map.length, attacker.getY(), defender.getX(), defender.getY(), BALL_SIZE );

                                        g.fillPolygon(pol1.xpoints,0, pol1.ypoints, 0, pol1.npoints, argb );
                                        g.fillPolygon(pol2.xpoints,0, pol2.ypoints, 0, pol2.npoints, argb );

                                    }
                                    else { // the attacker is on the left

                                        Polygon pol1 = makeArrow( attacker.getX(), attacker.getY(), defender.getX()-map.length, defender.getY(), BALL_SIZE );
                                        Polygon pol2 = makeArrow( attacker.getX()+map.length, attacker.getY(), defender.getX(), defender.getY(), BALL_SIZE );

                                        g.fillPolygon(pol1.xpoints,0, pol1.ypoints, 0, pol1.npoints, argb );
                                        g.fillPolygon(pol2.xpoints,0, pol2.ypoints, 0, pol2.npoints, argb );
                                    }

                            }
                            else {

                                Polygon pol1 = makeArrow( attacker.getX(), attacker.getY(), defender.getX(), defender.getY(), BALL_SIZE );

                                g.fillPolygon(pol1.xpoints,0, pol1.ypoints, 0, pol1.npoints, argb );

                            }

                        }

                }

                
                if (oldState != state) { // if the state has changed!!!
                    oldState = state;
                    if (state == RiskGame.STATE_GAME_OVER) {
                        startAni();
                    }
                    else {
                        stopAni();
                    }
                }

                Country t;
                for (int c=0; c< v.length ; c++) {

                        t = v[c];

                        if ( t.getOwner() != null ) {

                                int x,y;
                                if (ballWorld==null) {
                                    x = t.getX();
                                    y = t.getY();
                                }
                                else {
                                    x = (int)ballWorld.balls[c].x;
                                    y = (int)ballWorld.balls[c].y;
                                }
                            
                                g.setARGBColor( ((Player)t.getOwner()).getColor() );

                                g2.fillOval( x-r , y-r , BALL_SIZE, BALL_SIZE );

                                g.setARGBColor( ColorUtil.getTextColorFor( ((Player)t.getOwner()).getColor() ) );

                                int h = y -(font.getHeight()/2 -1);
                                String noa=String.valueOf(t.getArmies());

                                g2.drawString( noa, x - (font.getWidth(noa)/2) , h );

                        }

                }

                if (game.getGameMode() == RiskGame.MODE_CAPITAL && game.getSetup() && state != RiskGame.STATE_SELECT_CAPITAL ) {

                        int stroke = BALL_SIZE / 10;
                    
                        int old = g2.getGraphics().getStrokeWidth();
                        g2.getGraphics().setStrokeWidth( stroke );
                        List players = game.getPlayers();

                        for (int c=0; c< players.size() ; c++) {

                                Country capital = ((Player)players.get(c)).getCapital();

				if ( capital !=null ) {
					
                                        int pos = capital.getColor()-1;

                                        int x,y;
                                        if (ballWorld==null) {
                                            x = v[pos].getX();
                                            y = v[pos].getY();
                                        }
                                        else {
                                            x = (int)ballWorld.balls[pos].x;
                                            y = (int)ballWorld.balls[pos].y;
                                        }
                                        
                                        
                                        g.setARGBColor( ColorUtil.getTextColorFor( ((Player)capital.getOwner()).getColor() ) );

                                        g2.drawArc( x-r , y-r , BALL_SIZE, BALL_SIZE , 0, 360);

                                        g.setARGBColor( ((Player)players.get(c)).getColor() );

                                        int size = BALL_SIZE + (stroke*2);
                                        g2.drawArc( x-(size/2) , y-(size/2) , size, size, 0, 360);

                                }

                        }
                        g2.getGraphics().setStrokeWidth(old);
                }

        }

        BallWorld ballWorld;
        int oldState;

        public void startAni() {
            if (ballWorld==null) {
                ballWorld = new BallWorld(myrisk, this, BALL_SIZE/2); // start the ball world!!
            }
        }
        /**
         * stop all animations
         */
        public void stopAni() {
            if (ballWorld!=null) {
                ballWorld.stop();
                ballWorld = null;
                repaint();
            }
        }
        
        private void drawHighLightImage(Graphics2D g, CountryImage countryImage) {
            
            int val = countryImage.color;
            Graphics g2 = g.getGraphics();
            ColorMatrix m;
            
            if (val == 0) {
                m = HighLight;
            }
            else {
                m = getMatrix(val);
                m.preConcat(gray);
                m.postConcat( HighLight );
            }
            
            g2.setColorMarix(m);
            g.drawImage(countryImage.getSourceImage(), countryImage.getX1(), countryImage.getY1());
            g2.setColorMarix(null);
        }
        
        /**
         * Paints the arrows for the game, ie - when attacking
         * @param x1i x point of the attacker's co-ordinates.
         * @param y1i y point of the attacker's co-ordinates.
         * @param x2i x point of the defender's co-ordinates.
         * @param y2i y point of the defender's co-ordinates.
         * @param ri the radius of the circle
         */
        public Polygon makeArrow(int x1i, int y1i, int x2i, int y2i, int d) {

                Polygon arrow;

                double x1 = x1i;
                double y1 = y1i;
                double x2 = x2i;
                double y2 = y2i;

                double xd = x2-x1;
                double yd = y1-y2;

                double r = d/2;
                double l = Math.sqrt( Math.pow(xd, 2d) + Math.pow(yd, 2d) );

                double a = Math.acos( (r/l) );
                double b = Math.atan( (yd/xd) );
                double c = Math.atan( (xd/yd) );

                double x3 = r * Math.cos( a - b );
                double y3 = r * Math.sin( a - b );

                double x4 = r * Math.sin( a - c );
                double y4 = r * Math.cos( a - c );

                //System.out.print("x3="+x3+" y3="+y3+" x4="+x4+" y4="+y4+"\n");

/*

              3
             /|\
    2--       |       --3
    |\        |        /|
       \      |      /
         \    |    /
           \  -  /
  /         / | \         \
2----------|--+--|----------3
  \         \ | /         /
           /  -  \
         /    |    \
       /      |      \
    |/        |        \|
    4--       |       --1
             \|/
              1

*/

                if (x2 >= x1 && y2 <= y1) {

                        //System.out.print("3\n");

                        int xCoords[] = { (int)x1, (int)Math.round(x1+x3) , (int)x2 , (int)Math.round(x1-x4) };
                        int yCoords[] = { (int)y1, (int)Math.round(y1+y3) , (int)y2 , (int)Math.round(y1-y4) };
                        arrow = new Polygon(xCoords, yCoords, xCoords.length);

                }
                else if (x2 >= x1 && y2 >= y1) {

                        //System.out.print("1\n");

                        int xCoords[] = { (int)x1, (int)Math.round(x1+x3) , (int)x2 , (int)Math.round(x1+x4) };
                        int yCoords[] = { (int)y1, (int)Math.round(y1+y3) , (int)y2 , (int)Math.round(y1+y4) };
                        arrow = new Polygon(xCoords, yCoords, xCoords.length);

                }
                else if (x2 <= x1 && y2 <= y1) {

                        //System.out.print("2\n");

                        int xCoords[] = { (int)x1, (int)Math.round(x1-x3) , (int)x2 , (int)Math.round(x1-x4) };
                        int yCoords[] = { (int)y1, (int)Math.round(y1-y3) , (int)y2 , (int)Math.round(y1-y4) };
                        arrow = new Polygon(xCoords, yCoords, xCoords.length);

                }

                else  { // if (x2 < x1 && y2 > y1)

                        //System.out.print("4\n");

                        int xCoords[] = { (int)x1, (int)Math.round(x1-x3) , (int)x2 , (int)Math.round(x1+x4) };
                        int yCoords[] = { (int)y1, (int)Math.round(y1-y3) , (int)y2 , (int)Math.round(y1+y4) };
                        arrow = new Polygon(xCoords, yCoords, xCoords.length);

                }


                return arrow;

        }

        /**
         * Repaints the countries for each of the different views
         * @param view The name of each of the map views.
         */
        public synchronized void repaintCountries(int view) { // synchronized

                RiskGame game = myrisk.getGame();

                if (tempimg==null) return;
                
                Graphics zg = tempimg.getGraphics();
                zg.drawImage(img ,0 ,0, 0 );

                List allConnectedEmpires=null;

                if (view == VIEW_CONNECTED_EMPIRE) {

                        List players = game.getPlayers();

                        allConnectedEmpires = new ArrayList();

                        for (int c=0; c<players.size(); c++) {
                                allConnectedEmpires.addAll( game.getConnectedEmpire( (Player)players.get(c) ) );
                        }
                }

                for (int c=0; c < countryImages.length ; c++) {

                    int val=0;

                    if (view == VIEW_CONTINENTS) {

                                val = 0x00000000;

                    }
                    else if (view == VIEW_OWNERSHIP) {


                                if ( ((Country)game.getCountryInt( c+1 )).getOwner() != null ) {
                                        val = ((Player)((Country)game.getCountryInt( c+1 )).getOwner()).getColor();
                                }
                                else {
                                        val = GRAY;
                                }

                                val = colorWithAlpha(val, 100);

                    }
                    else if (view == VIEW_BORDER_THREAT) {

                                Player player = ((Country)game.getCountryInt( c+1 )).getOwner();

                                if (player != game.getCurrentPlayer() ) {
                                        val = GRAY;
                                }
                                else {
                                        List neighbours = ((Country)game.getCountryInt( c+1 )).getNeighbours();
                                        int threat=0; // max of about 6

                                        for (int j = 0; j < neighbours.size() ; j++) {

                                                if ( ((Country)neighbours.get(j)).getOwner() != player ) {
                                                        threat++;
                                                }

                                        }

                                        threat=threat*40;

                                        if (threat > 255) { threat=255; }

                                        val = newColor( threat, 0, 0);
                                }

                                val = colorWithAlpha(val, 200);


                    }
                    else if (view == VIEW_CARD_OWNERSHIP) {

                                if (  game.getCurrentPlayer()==null  || ((Country)game.getCountryInt(c+1)).getOwner() != (Player)game.getCurrentPlayer()) {
                                        val = LIGHT_GRAY;
                                }
                                else {
                                        List cards = ((Player)game.getCurrentPlayer()).getCards();

                                        for (int j = 0; j < cards.size() ; j++) {

                                                if ( ((Card)cards.get(j)).getCountry() == (Country)game.getCountryInt(c+1) ) {
                                                        val = BLUE;
                                                }

                                        }

                                        if (val == 0) val = DARK_GRAY;
                                }

                                val = colorWithAlpha(val, 100);

                    }
                    else if (view == VIEW_TROOP_STRENGTH) {

                                if (((Country)game.getCountryInt(c+1)).getOwner() != (Player)game.getCurrentPlayer()) {
                                        val = GRAY;
                                }
                                else {
                                        int armies = ((Country)game.getCountryInt(c+1)).getArmies();

                                        armies=armies*25;

                                        if (armies > 255) { armies=255; }

                                        val = newColor( 0 , armies, 0);
                                }

                                val = colorWithAlpha(val, 200);

                    }
                    else if (view == VIEW_CONNECTED_EMPIRE) {

                                Country thecountry = game.getCountryInt( c+1 );

                                if ( thecountry.getOwner() == null ) {

                                        val = LIGHT_GRAY;

                                }
                                else if ( allConnectedEmpires.contains( thecountry ) ) {

                                        val = ((Player)thecountry.getOwner()).getColor();

                                }
                                else {
                                        val = DARK_GRAY;
                                }

                                val = colorWithAlpha(val, 100);

/*

                                Country thecountry = ((Country)game.getCountryInt(c+1));

                                if ( b != null && b.contains( thecountry ) ) {
                                        val = ((Player)game.getCurrentPlayer()).getColor();
                                }
                                else if (((Country)game.getCountryInt(c+1)).getOwner() == (Player)game.getCurrentPlayer()) {
                                        val = Color.darkGray;
                                }
                                else {
                                        val = Color.lightGray;
                                }

                                val = new Color(val.getRed(), val.getGreen(), val.getBlue(), 100);
*/
                    }

                    CountryImage ci = countryImages[c];

                    int x1=ci.getX1();
                    int y1=ci.getY1();

                    if (ci.checkChange(val)) {
                        if (view != VIEW_CONTINENTS) {
                            ColorMatrix m = getMatrix(val);
                            m.preConcat(gray);
                            zg.setColorMarix(m);
                        }
                        zg.drawImage(ci.getSourceImage() ,x1 ,y1 ,0);
                    }
                }

                Image newback = img;

                img = tempimg;

                tempimg = newback;
        }

        /**
         * Gets the unique identifier of a country from its position on the map
         * @param x x co-ordinate on the map
         * @param y y co-ordinate on the map
         */
        public int getCountryNumber(int x, int y) {

                double s = getScale();

                x = x - getImgX(s);
                y = y - getImgY(s);

                x = (int)(x / s);
                y = (int)(y / s);

                if (x<0 || y<0 || x>=map.length || y>=map[0].length) {
                        return NO_COUNTRY;
                }

                return map[x][y] + 128;
        }

        /**
         * Sets which country to hilight
         * @param a number of the country
         */
        public void setHighLight(int a) {
                cc=a;
        }

        /**
         * Returns which country is hilighted
         * @return int Returns which country is hilighted
         */
        public int getHighLight() {
                return cc;
        }

        /**
         * Sets the attacking country
         * @param a number of the country
         */
        public void setC1(int a) {
                c1=a;
        }

        /**
         * Sets the defensive country
         * @param a number of the country
         */
        public void setC2(int a) {
                c2=a;
        }

        /**
         * Returns the attacking country
         * @return int number of the country
         */
        public int getC1() {
                return c1;
        }

        public int getC2() {
                return c2;
        }

        public static ColorMatrix RescaleOp(float a,float b) {
            ColorMatrix cm = new ColorMatrix();
            cm.set(new float[] {
                    a,0,0,0,b,
                    0,a,0,0,b,
                    0,0,a,0,b,
                    0,0,0,1,0,
                });
            return cm;
        }
        
        public static ColorMatrix getMatrix(int color) {
            
            float r = ColorUtil.getRed(color);
            float g = ColorUtil.getGreen(color);
            float b = ColorUtil.getBlue(color);
            
            float alpha = ((float)ColorUtil.getAlpha(color))/255f;
            float alpha2 = 1 - alpha;
            
            ColorMatrix cm = new ColorMatrix();
            cm.set(new float[] {
                    alpha2,0,0,0,r*alpha,
                    0,alpha2,0,0,g*alpha,
                    0,0,alpha2,0,b*alpha,
                    0,0,0,1,0,
                });
            return cm;
        }

        /**
         * Gets the image of a country
         * @param num the color of a country
         * @return BufferedImage Image buffered of a country
         */
        public Image getCountryImage(int num) {
                CountryImage ci = countryImages[num-1];
                return ci.getSourceImage();
        }

        public final static int GRAY      = newColor(128, 128, 128);
        public final static int DARK_GRAY  = newColor(64, 64, 64);
        public final static int LIGHT_GRAY = newColor(192, 192, 192);
        public final static int BLUE  = newColor(0, 0, 255);

        public static int colorWithAlpha(int color, int alpha) {
            return ((alpha & 0xFF) << 24) | (color & 0xFFFFFF);
        }
        private static int newColor(int r,int g,int b) {
            return ((255 & 0xFF) << 24) |
            ((r & 0xFF) << 16) |
            ((g & 0xFF) << 8)  |
            ((b & 0xFF) << 0);
        }
        
        
        // Subclass countryImage - holds all the image information

        class CountryImage {

                private int x1;
                private int y1;
                private int x2;
                private int y2;
                private Image SourceImage;

                private int  color;

                public CountryImage() {
                        x1=Integer.MAX_VALUE;
                        y1=Integer.MAX_VALUE;
                }

                public boolean checkChange(int b) {

                        if (b != color) {

                                color = b;
                                return true;
                        }

                        return false;

                }

                /**
                 * Sets the source image
                 * @param a Image buffered
                 */
                public void setSourceImage(Image a) {
                        SourceImage=a;
                }

                /**
                 * Sets the top left corner of a country
                 * @param a coordinate
                 */
                public void setX1(int a) {
                        x1=a;
                }

                /**
                 * Sets the bottom left corner of a country
                 * @param a coordinate
                 */
                public void setY1(int a) {
                        y1=a;
                }

                /**
                 * Sets the top right corner of a country
                 * @param a coordinate
                 */
                public void setX2(int a) {
                        x2=a;
                }

                /**
                 * Sets the bottom right corner of a country
                 * @param a coordinate
                 */
                public void setY2(int a) {
                        y2=a;
                }

                /**
                 * Gets the source image
                 * @return BufferedImage Returns the source image
                 */
                public Image getSourceImage() {
                        return SourceImage;
                }

                /**
                 * Gets the top left corner of a country
                 * @return int coordinate
                 */
                public int getX1() {
                        return x1;
                }

                /**
                 * Gets the bottom left corner of a country
                 * @return int coordinate
                 */
                public int getY1() {
                        return y1;
                }

                /**
                 * Gets the top right corner of a country
                 * @return int coordinate
                 */
                public int getX2() {
                        return x2;
                }

                /**
                 * Gets the bottom right corner of a country
                 * @return int coordinate
                 */
                public int getY2() {
                        return y2;
                }

                /**
                 * Gets the width of a country
                 * @return int width of a country
                 */
                public int getWidth() {
                        return (x2-x1+1);
                }

                /**
                 * Gets the height of a country
                 * @return int height of a country
                 */
                public int getHeight() {
                        return (y2-y1+1);
                }

                @Override
                public String toString() {
                    return "CountryImage{x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", w=" + getWidth() + ", h=" + getHeight() + '}';
                }

        }
        
        
        
        class Polygon {

            public int[] xpoints;
            public int[] ypoints;
            public int npoints;

            public Polygon(int[] xCoords, int[] yCoords, int length) {
                this.xpoints = xCoords;
                this.ypoints = yCoords;
                this.npoints = length;
            }
        }

/*
        public static Image getImage(RiskGame game) throws Exception {

                // attempt to get the preview as its smaller
                String imagename = game.getPreviewPic();

                if (imagename==null) {

                        return Toolkit.getDefaultToolkit().getImage( new URL(RiskUIUtil.mapsdir,game.getImagePic() ) ).getScaledInstance(203,127, java.awt.Image.SCALE_SMOOTH );

                }
                else {

                        Image s = Image.createImage(RiskUtil.openMapStream("preview/"+imagename) );
                        String name = game.getMapName();

                        Image tmpimg = Image.createImage(203,127);
                        Graphics g = tmpimg.getGraphics();


                        //g.drawImage(s.getScaledInstance(203,127, java.awt.Image.SCALE_SMOOTH ),0,0,null );

                        g.drawImage(s,0,0,203,127,0,0,s.getWidth(),s.getHeight(),null);

                        //AffineTransform at = AffineTransform.getScaleInstance((double)203/s.getWidth(),(double)127/s.getHeight());
                        //g.drawRenderedImage(s,at);


                        if (name!=null) {

                                g.setARGBColor( new Color(255,255,255, 150).getRGB() );
                                g.fillRect(0,0,203,20);
                                g.setARGBColor( Color.BLACK.getRGB() );
                                g.drawString(name,5,15,0);

                        }

                        return tmpimg;
                }
        }
*/

}

