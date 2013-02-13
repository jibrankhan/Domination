// Yura Mamyrin, Group D

package net.yura.domination.engine.guishared;

import collisionphysics.BallWorld;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.JPanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.translation.TranslationBundle;


/**
 * <p> Picture Panel </p>
 * @author Yura Mamyrin
 */

public class PicturePanel extends JPanel implements MapPanel {

	public final static int NO_COUNTRY = 255;

	public final static int PP_X = 677;
	public final static int PP_Y = 425;

	public final static int VIEW_CONTINENTS       = 0;
	public final static int VIEW_OWNERSHIP        = 1;
	public final static int VIEW_BORDER_THREAT    = 2;
	public final static int VIEW_CARD_OWNERSHIP   = 3;
	public final static int VIEW_TROOP_STRENGTH   = 4;
	public final static int VIEW_CONNECTED_EMPIRE = 5;

	private CountryImage[] countryImages;
	private Risk myrisk;
	private BufferedImage original;
	private BufferedImage img;
	private BufferedImage tempimg;
	private byte[][] map;
	private int c1,c2,cc;

	private String strCountry;
	private RescaleOp HighLight;

	/**
	 * Creates an Picture Panel
	 */
	public PicturePanel(Risk r) {

		myrisk=r;

		this.strCountry = TranslationBundle.getBundle().getString( "picturepanel.country");

		img = null;
		map = null;

		// YURA YURA YURA MAYBE CHANGE 1.0F SO THAT FLAT COLORS HIGHLIGHT TOO
		 			 // 0-2  0-255
		HighLight = new RescaleOp(1.5f, 1.0f, null);

		//setupSize(PicturePanel.PP_X , PicturePanel.PP_Y);

	}

	private void setupSize(int x,int y) {

	    if (map==null || map.length!=x || map[0].length!=y) {

		//System.out.println("MAKING NEW SIZE!!!!");

		Dimension size = new Dimension(x,y);

		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

                // clear out old values
                img = null;
                tempimg = null;
                map = null;
                
		img = new BufferedImage(x,y, java.awt.image.BufferedImage.TYPE_INT_RGB );
		tempimg = new BufferedImage(x,y, java.awt.image.BufferedImage.TYPE_INT_RGB );
		map = new byte[x][y];
	    }

	}

	/**
	 * Adds the images related to the game to the picture panel
	 */
	public void load() throws IOException {

		RiskGame game = myrisk.getGame();

                // clean up before we load new images
                original = null;
                countryImages = null;
                
                //System.out.print("loading: "+(game.getImagePic()).getAbsolutePath()+" "+(game.getImageMap()).getAbsolutePath() +" "+((Vector)game.getCountries()).size()+"\n");
                
		memoryLoad(
                        RiskUIUtil.read(RiskUtil.openMapStream(game.getImageMap()) ),
                        RiskUIUtil.read(RiskUtil.openMapStream(game.getImagePic()) )
                        );

	}

	public void memoryLoad(BufferedImage m, BufferedImage O) {

                int mWidth = m.getWidth();
                int mHeight = m.getHeight();



		RiskGame game = myrisk.getGame();
                BALL_SIZE = game.getCircleSize();
                setFont( new java.awt.Font("Arial", java.awt.Font.PLAIN, (BALL_SIZE+2)/2 ) );
		original = O;
		cc=NO_COUNTRY;
		c1=NO_COUNTRY;
		c2=NO_COUNTRY;
                int noc = game.getCountries().length;



                int[] pixels = m.getRGB(0,0,mWidth,mHeight,null,0,mWidth );
                m=null;



		setupSize(mWidth,mHeight); // creates a 2D byte array and double paint buffer
		{ Graphics zg = img.getGraphics(); zg.drawImage(original, 0, 0, this); zg.dispose(); }



		countryImages = new CountryImage[noc];
		for (int c=0; c < noc; c++) {
			countryImages[c] = new CountryImage();
		}


		CountryImage cci;
		// create a very big 2d array with all the data from the image map
		for(int x=0; x < mWidth; x++) {

			for(int y=0; y < mHeight; y++) {

				int num = pixels[ (mWidth*y) + x ] & 0xff; // (m.getRGB(x,y))&0xff;

				// if ( num > noc && num !=NO_COUNTRY ) System.out.print("map error: "+x+" "+y+"\n"); // testing map

				map[x][y]= (byte) (num - 128); // as byte is signed we have to use this

				if ( num != NO_COUNTRY ) {

					cci = countryImages[num-1];

					if (x < cci.getX1() ) { cci.setX1(x); }
					if (x > cci.getX2() ) { cci.setX2(x); }

					if (y < cci.getY1() ) { cci.setY1(y); }
					if (y > cci.getY2() ) { cci.setY2(y); }
				}

			}
		}

                pixels = null;
                
		//ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		//ColorConvertOp Gray = new ColorConvertOp( cs , null);

		// create the bufferd image for each country
		for (int c=0; c < countryImages.length ; c++) {

			cci = countryImages[c];

			int x1=cci.getX1();
//			int x2=cci.getX2();
			int y1=cci.getY1();
//			int y2=cci.getY2();
			int w=cci.getWidth();
			int h=cci.getHeight();

			// System.out.print( "Country: "+ (c+1) +" X1: "+ x1 +" Y1: "+y1 +" Width: "+ w +" Height: "+ h +"\n");

			BufferedImage source = original.getSubimage(x1, y1, w, h);

			BufferedImage gray = new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_BYTE_GRAY );

			//Gray.filter(source , gray);
                        { Graphics zg = gray.getGraphics(); zg.drawImage(source, 0, 0, this); zg.dispose(); }

			cci.setSourceImage(source);
			cci.setGrayImage(gray);

			cci.setNormalImage( new BufferedImage( w ,h, java.awt.image.BufferedImage.TYPE_INT_ARGB ) );
			cci.setHighLightImage( new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB ) );

			cci.setTemp1( new BufferedImage( w ,h, java.awt.image.BufferedImage.TYPE_INT_RGB ) );
			cci.setTemp2( new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB ) );
		}



	}

	/**
	 * Paints the components
	 * @param g a Graphics object.
	 */
	public void paintComponent(Graphics g) {

	    super.paintComponent(g);

	    try {

		if (img != null) {

			//System.out.print("#################################################### Repainted\n");

			//super.paintComponent(g);

			Graphics2D g2 = (Graphics2D)g;

			double s = getScale();

			//System.out.println("scale: "+s);

			g2.translate(getDrawImageX(s),getDrawImageY(s));
			g2.scale(s,s);

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2.drawImage(img,0,0,this);

			if (c1 != NO_COUNTRY) {
				g2.drawImage( countryImages[c1-1].getHighLightImage() ,countryImages[c1-1].getX1() ,countryImages[c1-1].getY1() ,this);
			}

			if (c2 != NO_COUNTRY) {
				g2.drawImage(countryImages[c2-1].getHighLightImage() ,countryImages[c2-1].getX1() ,countryImages[c2-1].getY1() ,this);
			}

			if (cc != NO_COUNTRY) {
				g2.drawImage( countryImages[cc-1].getHighLightImage() ,countryImages[cc-1].getX1() ,countryImages[cc-1].getY1() ,this);
			}

			drawArmies(g2);

			if (cc != NO_COUNTRY) {

                                int offset = 5;
                            
				TextLayout tl = new TextLayout( this.strCountry + " "+ myrisk.getCountryName( cc ) , g2.getFont() , g2.getFontRenderContext() );
				int w = (int)tl.getAdvance();
				int h = (int)tl.getAscent() + (int)tl.getDescent();

				g2.setColor( new Color(255,255,255, 150) );
				g2.fill(new Rectangle2D.Float( offset , offset, w+3, h+1 ));

				g2.setColor( Color.black );
				tl.draw( g2, offset + (float)1, offset + tl.getAscent() );
			}


		}

	    }
	    catch(Exception e) { } // an excpetion here really does not matter

	}

	private int getDrawImageX(double ratio) {
		return (int) (getWidth()-( getMapWidth() *ratio) )/2;
	}

	private int getDrawImageY(double ratio) {
		return (int) (getHeight()-( getMapHeight() *ratio) )/2;
	}

	private double getScale() {
		return Math.min(getHeight()/(double)getMapHeight() ,getWidth()/(double)getMapWidth() );
	}
        
        public int getMapWidth() {
            return map.length;
        }
        public int getMapHeight() {
            return map[0].length;
        }

        public int BALL_SIZE=20;
        
	/**
	 * Paints the army components
	 * @param g2 a 2D Graphics object.
	 */
	public void drawArmies(Graphics2D g2) {

		RiskGame game = myrisk.getGame();

		Country[] v = game.getCountries();

                int state = game.getState();

                int r = BALL_SIZE/2;
                
		if (state==RiskGame.STATE_ROLLING || state==RiskGame.STATE_BATTLE_WON || state==RiskGame.STATE_DEFEND_YOURSELF) {

			int a=game.getAttacker().getColor();
			int b=game.getDefender().getColor();

			g2.drawImage(countryImages[a-1].getHighLightImage() ,countryImages[a-1].getX1() ,countryImages[a-1].getY1() ,this);
			g2.drawImage(countryImages[b-1].getHighLightImage() ,countryImages[b-1].getX1() ,countryImages[b-1].getY1() ,this);

			Color ac = new Color( game.getAttacker().getOwner().getColor() );
			g2.setColor( new Color(ac.getRed(),ac.getGreen(), ac.getBlue(), 150) );
			//g2.setStroke(new BasicStroke(3));

			if ( Math.abs( game.getAttacker().getX() - game.getDefender().getX() ) > (map.length / 2) ) {

				if ( ((Country)game.getAttacker()).getX() > (map.length / 2) ) { // ie the attacker is on the right
					g2.fillPolygon( makeArrow( game.getAttacker().getX(), ((Country)game.getAttacker()).getY(), ((Country)game.getDefender()).getX()+map.length, ((Country)game.getDefender()).getY(), BALL_SIZE ));
					g2.fillPolygon( makeArrow( game.getAttacker().getX()-map.length, ((Country)game.getAttacker()).getY(), ((Country)game.getDefender()).getX(), ((Country)game.getDefender()).getY(), BALL_SIZE ));

				}
				else { // the attacker is on the left
					g2.fillPolygon( makeArrow( game.getAttacker().getX(), ((Country)game.getAttacker()).getY(), ((Country)game.getDefender()).getX()-map.length, ((Country)game.getDefender()).getY(), BALL_SIZE ));
					g2.fillPolygon( makeArrow( game.getAttacker().getX()+map.length, ((Country)game.getAttacker()).getY(), ((Country)game.getDefender()).getX(), ((Country)game.getDefender()).getY(), BALL_SIZE ));
				}

			}
			else {

				g2.fillPolygon( makeArrow( ((Country)game.getAttacker()).getX(), ((Country)game.getAttacker()).getY(), ((Country)game.getDefender()).getX(), ((Country)game.getDefender()).getY(), BALL_SIZE ));

			}

			//g2.setStroke(new BasicStroke(1));

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

                                g2.setColor( new Color( t.getOwner().getColor() ) );

                                Ellipse2D ellipse = new Ellipse2D.Double();
                                ellipse.setFrame( x-r , y-r , BALL_SIZE, BALL_SIZE );
                                g2.fill(ellipse);

                                //g.fillOval( t.getX()-r , t.getY()-r, (r*2), (r*2) );

                                g2.setColor( new Color( ColorUtil.getTextColorFor( t.getOwner().getColor() ) ) );

                                g2.setFont( getFont() );
                                
                                String noa= String.valueOf( t.getArmies() );
                                
                                int w2 = g2.getFontMetrics().stringWidth(noa) / 2;
                                int h2 = g2.getFontMetrics().getAscent()*2/5 ;

                                g2.drawString( String.valueOf( noa ) , x-w2, y+h2 );

                        }

                }

		if (game.getGameMode() == RiskGame.MODE_CAPITAL && game.getSetup() && state !=RiskGame.STATE_SELECT_CAPITAL ) {

                        int stroke = BALL_SIZE / 10;
                    
                        Stroke old = g2.getStroke();
			g2.setStroke(new BasicStroke( stroke ));
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

					g2.setColor( new Color( ColorUtil.getTextColorFor( capital.getOwner().getColor() ) ) );

					Ellipse2D ellipse = new Ellipse2D.Double();
					ellipse.setFrame( x-r , y-r , BALL_SIZE-1, BALL_SIZE-1);
					g2.draw(ellipse);

					g2.setColor( new Color( ((Player)players.get(c)).getColor() ) );

					Ellipse2D ellipse2 = new Ellipse2D.Double();
                                        int size = BALL_SIZE + (stroke*2);
					ellipse2.setFrame( x-(size/2) , y-(size/2) , size-1, size-1);
					g2.draw(ellipse2);

				}

			}
			g2.setStroke(old);
		}

	}
        
        BallWorld ballWorld;
        int oldState;

        public void startAni() {
            if (ballWorld==null) {
                ballWorld = new BallWorld(myrisk, this, BALL_SIZE/2 ); // start the ball world!!
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

        protected void processMouseEvent(MouseEvent e) {
            super.processMouseEvent(e);
            if (e.getID() == MouseEvent.MOUSE_CLICKED && myrisk.getGame().getState() == RiskGame.STATE_GAME_OVER ) {
                // toggle the animation
                if (ballWorld==null) {
                    startAni();
                }
                else {
                    stopAni();
                }
            }
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

		{ Graphics zg = tempimg.getGraphics(); zg.drawImage(original ,0 ,0 ,this); zg.dispose(); }

		List allConnectedEmpires=null;

		if (view == VIEW_CONNECTED_EMPIRE) {

			List players = game.getPlayers();

			allConnectedEmpires = new ArrayList();

			for (int c=0; c<players.size(); c++) {
				allConnectedEmpires.addAll( game.getConnectedEmpire( (Player)players.get(c) ) );
			}
		}

		for (int c=0; c < countryImages.length ; c++) {

		    Color val=null;

		    if (view == VIEW_CONTINENTS) {

				val = new Color(0,true);

		    }
		    else if (view == VIEW_OWNERSHIP) {


				if ( ((Country)game.getCountryInt( c+1 )).getOwner() != null ) {
					val = new Color( ((Player)((Country)game.getCountryInt( c+1 )).getOwner()).getColor() );
				}
				else {
					val = Color.GRAY;
				}

				val = new Color(val.getRed(), val.getGreen(), val.getBlue(), 100);

		    }
		    else if (view == VIEW_BORDER_THREAT) {

				Player player = ((Country)game.getCountryInt( c+1 )).getOwner();

				if (player != game.getCurrentPlayer() ) {
					val = Color.gray;
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

					val = (new Color( threat, 0, 0));
				}

				val = new Color(val.getRed(), val.getGreen(), val.getBlue(), 200);


		    }
		    else if (view == VIEW_CARD_OWNERSHIP) {

				if (  game.getCurrentPlayer()==null  || ((Country)game.getCountryInt(c+1)).getOwner() != (Player)game.getCurrentPlayer()) {
					val = Color.lightGray;
				}
				else {
					List cards = ((Player)game.getCurrentPlayer()).getCards();

					for (int j = 0; j < cards.size() ; j++) {

						if ( ((Card)cards.get(j)).getCountry() == (Country)game.getCountryInt(c+1) ) {
							val = Color.blue;
						}

					}

					if (val == null) val = Color.darkGray;
				}

				val = new Color(val.getRed(), val.getGreen(), val.getBlue(), 100);

		    }
		    else if (view == VIEW_TROOP_STRENGTH) {

				if (((Country)game.getCountryInt(c+1)).getOwner() != (Player)game.getCurrentPlayer()) {
					val = Color.gray;
				}
				else {
					int armies = ((Country)game.getCountryInt(c+1)).getArmies();

					armies=armies*25;

					if (armies > 255) { armies=255; }

					val = (new Color( 0 , armies, 0));
				}

				val = new Color(val.getRed(), val.getGreen(), val.getBlue(), 200);

		    }
		    else if (view == VIEW_CONNECTED_EMPIRE) {

				Country thecountry = game.getCountryInt( c+1 );

				if ( thecountry.getOwner() == null ) {

					val = Color.LIGHT_GRAY;

				}
				else if ( allConnectedEmpires.contains( thecountry ) ) {

					val = new Color( ((Player)thecountry.getOwner()).getColor() );

				}
				else {
					val = Color.DARK_GRAY;
				}

				val = new Color(val.getRed(), val.getGreen(), val.getBlue(), 100);

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
		    BufferedImage normalB = ci.getNormalImage(); // new BufferedImage( w ,h, java.awt.image.BufferedImage.TYPE_INT_ARGB );

		    if ( ci.checkChange(val) ) {

			int y2=ci.getY2();
			int w=ci.getWidth();
			int h=ci.getHeight();

			BufferedImage normalA = ci.getTemp1(); // new BufferedImage( w ,h, java.awt.image.BufferedImage.TYPE_INT_RGB );
			BufferedImage highlightA = ci.getTemp2(); // new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB );
			BufferedImage highlightB = ci.getHighLightImage(); // new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB );

			Graphics tempg = normalA.getGraphics();

			if (view == VIEW_CONTINENTS) {

				tempg.drawImage( ci.getSourceImage() ,0,0,this );

			}
			else {

				tempg.drawImage( ci.getGrayImage(), 0, 0, this);
				tempg.setColor( val );
				tempg.fillRect(0,0,w,h);

			}

			tempg.dispose();

			HighLight.filter( normalA , highlightA );

			if (view != VIEW_CONTINENTS) { Graphics zg = normalB.getGraphics(); zg.drawImage(normalA,0,0,this); zg.dispose(); }

			{ Graphics zg = highlightB.getGraphics(); zg.drawImage(highlightA,0,0,this); zg.dispose(); }

			for(int y=y1; y <= y2; y++) {
				for(int x=0; x < w; x++) {
					if (map[x+x1][y] + 128 != (c+1) ) {
						normalB.setRGB( x, (y-y1), 0); // clear the un-needed area!
						highlightB.setRGB( x, (y-y1), 0); // clear the un-needed area!
					}
				}
			}

		    }

		    if (view != VIEW_CONTINENTS) { Graphics zg = tempimg.getGraphics(); zg.drawImage(normalB ,x1 ,y1 ,this); zg.dispose(); }

		}

		BufferedImage newback = img;

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

		x = x - getDrawImageX(s);
		y = y - getDrawImageY(s);

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

	// Subclass countryImage - holds all the image information

	class CountryImage {

		private int x1;
		private int y1;
		private int x2;
		private int y2;
		private BufferedImage SourceImage;
		private BufferedImage GrayImage;
		private BufferedImage normalImage;
		private BufferedImage HighLightImage;

		private BufferedImage temp1;
		private BufferedImage temp2;

		private Color color;

		public CountryImage() {
			x1=Integer.MAX_VALUE;
			y1=Integer.MAX_VALUE;
			x2=0;
			y2=0;
			SourceImage=null;
			GrayImage=null;
			HighLightImage=null;
			normalImage=null;

		}

		public boolean checkChange(Color b) {

			if (!b.equals(color) ) {

				color = b;
				return true;
			}

			return false;

		}

		public void setTemp1(BufferedImage a) {
			temp1=a;
		}
		public void setTemp2(BufferedImage a) {
			temp2=a;
		}
		public BufferedImage getTemp1() {
			return temp1;
		}
		public BufferedImage getTemp2() {
			return temp2;
		}

		/**
		 * Sets the source image
		 * @param a Image buffered
		 */
		public void setSourceImage(BufferedImage a) {
			SourceImage=a;
		}

		/**
		 * Sets the gray image
		 * @param a Image buffered
		 */
		public void setGrayImage(BufferedImage a) {
			GrayImage=a;
		}

		/**
		 * Sets the hilighted image
		 * @param a Image buffered
		 */
		public void setHighLightImage(BufferedImage a) {
			HighLightImage=a;
		}

		public void setNormalImage(BufferedImage a) {
			normalImage=a;
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
		public BufferedImage getSourceImage() {
			return SourceImage;
		}

		/**
		 * Gets the gray image
		 * @return BufferedImage Returns the gray image
		 */
		public BufferedImage getGrayImage() {
			return GrayImage;
		}

		/**
		 * Gets the hilighted image
		 * @return BufferedImage Returns the hilighted image
		 */
		public BufferedImage getHighLightImage() {
			return HighLightImage;
		}

		public BufferedImage getNormalImage() {
			return normalImage;
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


	}

	/**
	 * Gets the image of a country
	 * @param num the index of a country
	 * @param incolor whether the image of a country is in colour or greyscale
	 * @return BufferedImage Image buffered of a country
	 */
	public BufferedImage getCountryImage(int num, boolean incolor) {

		int i = num-1;

		CountryImage ci = countryImages[i];

		int x1=ci.getX1();
//		int x2=ci.getX2();
		int y1=ci.getY1();
		int y2=ci.getY2();
		int w=ci.getWidth();
		int h=ci.getHeight();

		BufferedImage pictureA = new BufferedImage( w ,h, ci.getGrayImage().getType() );

		RescaleOp HighLight = new RescaleOp( 0.5f, -1.0f, null);
                // we have to filter to the same type of image as the source image
		HighLight.filter( ci.getGrayImage() , pictureA );


		BufferedImage pictureB = new BufferedImage( w ,h, java.awt.image.BufferedImage.TYPE_INT_ARGB );

		Graphics g = pictureB.getGraphics();

		g.drawImage( pictureA ,0 ,0 ,this);

		if (incolor) {

			Color ownerColor = new Color( ((Player) ((Country) ((RiskGame)myrisk.getGame()) .getCountryInt( num )) .getOwner()).getColor() );

			g.setColor( new Color(ownerColor.getRed(), ownerColor.getGreen(), ownerColor.getBlue(), 100) );
			g.fillRect(0,0,w,h);

		}

		for(int y=y1; y <= y2; y++) {
			for(int x=0; x <= w-1; x++) {
				if (map[x+x1][y] + 128 != (i+1) ) {
					pictureB.setRGB( x, (y-y1), 0); // clear the un-needed area!
				}
			}
		}

		g.dispose();

		return pictureB;

	}

        public final static int PREVIEW_WIDTH=203;
        public final static int PREVIEW_HEIGHT=127;
        
	public static Image getImage(RiskGame game) throws IOException {
		// attempt to get the preview as its smaller
		String previewName = game.getPreviewPic();
                String name = game.getMapName();
                Image img=null;
                int width=-1,height=-1;
                boolean error = false;

		if (previewName!=null) {
                    try {
                        BufferedImage s = RiskUIUtil.read(RiskUtil.openMapStream("preview/"+previewName) );
			img = s;
                        width = s.getWidth();
                        height = s.getHeight();
                    }
                    catch (IOException ex) { // if we fail to load the preview its not the end of the world
                        error = true;
                    }
		}

                if (img==null) {
                        BufferedImage s = RiskUIUtil.read(RiskUtil.openMapStream( game.getImagePic() ) );
                        img = s.getScaledInstance(PREVIEW_WIDTH,PREVIEW_HEIGHT, java.awt.Image.SCALE_SMOOTH );
                        width = PREVIEW_WIDTH;
                        height = PREVIEW_HEIGHT;
		}

                if (name!=null || width!=PREVIEW_WIDTH || height!=PREVIEW_HEIGHT || error) {

                        //return Toolkit.getDefaultToolkit().getImage( new URL(RiskUIUtil.mapsdir,game.getImagePic() ) ).getScaledInstance(203,127, java.awt.Image.SCALE_SMOOTH );
			//g.drawImage(s.getScaledInstance(203,127, java.awt.Image.SCALE_SMOOTH ),0,0,null );
			//AffineTransform at = AffineTransform.getScaleInstance((double)203/s.getWidth(),(double)127/s.getHeight());
			//g.drawRenderedImage(s,at);

                        
                        BufferedImage tmpimg = new BufferedImage( PREVIEW_WIDTH,PREVIEW_HEIGHT, java.awt.image.BufferedImage.TYPE_INT_RGB );
			Graphics2D g = tmpimg.createGraphics();

			g.drawImage(img,0,0,PREVIEW_WIDTH,PREVIEW_HEIGHT,0,0,width,height,null);

			if (name!=null) {

				g.setColor( new Color(255,255,255, 150) );
				g.fillRect(0,0,PREVIEW_WIDTH,20);
				g.setColor( Color.BLACK );
				g.drawString(name,5,15);

			}

                        if (error) {
                            g.setColor( Color.RED );
                            g.fillOval(PREVIEW_WIDTH-20, 5, 10, 10);
                        }

			g.dispose();

			return tmpimg;
                }
                return img;
                
	}

	public Image getImage() {

		return img;

	}

}
