package net.yura.domination.mobile.flashgui;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.mapstore.MapRenderer;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.KeyEvent;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.CheckBox;
import net.yura.mobile.gui.components.Frame;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.layout.XULLoader;
import net.yura.mobile.util.Properties;

/**
 * @author Yura Mamyrin
 */
public class BattleDialog extends Frame implements ActionListener {

    Properties resb = GameActivity.resb;
    
    Risk myrisk;
    Sprite red_dice,blue_dice;
    Random r = new Random();
    Button rollButton,retreat,kill;

    public BattleDialog(Risk a) {
        myrisk = a;

        red_dice = MapRenderer.getSprite("/red_dice.png",3,3);
        blue_dice = MapRenderer.getSprite("/blue_dice.png",3,3);

        setName("TransparentDialog");
        setForeground(0xFF000000);
        setBackground(0xAA000000);

        rollButton = new Button(resb.getProperty("battle.roll"));
        rollButton.addActionListener(this);
        rollButton.setActionCommand("fight");

        kill = new CheckBox( resb.getProperty("battle.annihilate") );
        kill.setName( rollButton.getName() );
        kill.addActionListener(this);
        kill.setActionCommand("kill");

        retreat = new Button( resb.getProperty("battle.retreat") );
        retreat.addActionListener(this);
        retreat.setActionCommand("retreat");
        retreat.setMnemonic( KeyEvent.KEY_SOFTKEY2 );

        Panel controls = new Panel();
        controls.add(rollButton);
        controls.add(kill);
        controls.add(retreat);

        Panel contentPane = getContentPane();
        contentPane.setLayout( new MoveDialog.DialogLayout( getImageAreaHeight() ) );
        contentPane.add(controls);

        setMaximum(true);
    }
    
    private int getImageAreaHeight() {
        
        // mdpi
        // 90 for countries
        // 90 for dice
        // full height 180
        // 180 * 0.75 = 135
        
        return XULLoader.adjustSizeToDensity(135);
    }

    @Override
    public void actionPerformed(String actionCommand) {
        if ("fight".equals( actionCommand )) {
            go("roll "+nod);
        }
        else if ("kill".equals( actionCommand )) {
            if (kill.isSelected()) {
                go("roll "+max);
            }
        }
        else if ("retreat".equals( actionCommand )) {
            if (canRetreat) {
                go("retreat");
            }
            else {
                GameActivity.showClosePrompt(myrisk);
            }
        }
    }

    int[] att,def; // these are the dice results
    int noda,nodd; // these are the number of spinning dice
    int c1num,c2num;
    Image c1img,c2img;
    boolean ani,canRetreat;
    int nod,max;
    @Override
    public void run() throws InterruptedException {

        while(ani) {
            repaint();
            wait(200);
        }

    }


	/**
	 * Sets number of attacking dice
	 * @param n number of dice
	 */
	public void setNODAttacker(int n) {

		att=null;
		def=null;

		noda = n;

		repaint();

                ani = true;
		getDesktopPane().animateComponent(this);

	}

	/**
	 * Sets number of defending dice
	 * @param n number of dice
	 */
	public void setNODDefender(int n) {

		nodd = n;

	}

    public void showDiceResults(int[] atti, int[] defi) {

            ani = false;

            noda=0;
            nodd=0;

            att=atti;
            def=defi;

            repaint();

    }

    void needInput(int n, boolean c) {

        rollButton.setFocusable(true);
        max=n;
        nod=max;
        canRetreat=c;

        att=null;
        def=null;

        setTitle(resb.getProperty(canRetreat?"battle.select.attack":"battle.select.defend"));
        retreat.setVisible(canRetreat);
        kill.setVisible(canRetreat);

        revalidate();
        repaint();
        
        if (canRetreat && kill.isSelected()) {
            go("roll "+max);
        }
    }

    void setup(int c1num, int c2num,Image c1img,Image c2img) {
        this.c1num = c1num;
        this.c2num = c2num;
        this.c1img = c1img;
        this.c2img = c2img;
        
        att=null;
        def=null;

        noda=0;
        nodd=0;

        kill.setSelected(false);
        
        reset();
    }
    
    private void go(String input) {
        
        int gameState = myrisk.getGame().getState();
        
        if (gameState==RiskGame.STATE_ROLLING || gameState==RiskGame.STATE_DEFEND_YOURSELF) {

                //this does not close it, just resets its params
                reset();
        }
        
        myrisk.parser(input);
        
    }
    
    public void reset() {
        rollButton.setFocusable(false);
        retreat.setVisible(false);
        kill.setVisible(false);
        canRetreat=false;
        max=0;
        setTitle(resb.getProperty("battle.title"));
        
        revalidate();
        repaint();
    }

    private static final int DICE_NORMAL = 0;
    private static final int DICE_DARK = 1;
    private static final int COLOR_BLUE = 0xFF0000FF;
    private static final int COLOR_RED = 0xFFFF0000;

    @Override
    public void paintComponent(Graphics2D g) {

        int csrc = myrisk.hasArmiesInt( c1num );
        int cdes = myrisk.hasArmiesInt( c2num );
        int color1 = myrisk.getColorOfOwner( c1num );
        int color2 = myrisk.getColorOfOwner( c2num );

        int imageAreaHeight = getImageAreaHeight();
        int heightOfComponents = ((MoveDialog.DialogLayout)getContentPane().getLayout()).getHeightOfComponents(getContentPane());
        // this is the MIDDLE of the images area
        int xOffset = getContentPane().getWidth() / 2;
        int yOffset = (getContentPane().getHeight()-heightOfComponents)/2 + imageAreaHeight/4 + getContentPane().getY();

        //g.setColor(0xFFFF0000);
        //g.drawRect( (getContentPane().getWidth()-imageAreaHeight)/2 , (getContentPane().getHeight()-heightOfComponents)/2 + getContentPane().getY(), imageAreaHeight, imageAreaHeight);
        
        MoveDialog.paintMove(g,
                xOffset,yOffset,
                c1img,c2img,
                color1,color2,
                myrisk.getCountryName(c1num),myrisk.getCountryName(c2num),
                csrc,cdes,0);

        // #####################################################
        // ################## drawing DICE!!!!! ################

        int y1 = yOffset + imageAreaHeight/4; // top of dice
        
        // just in case in the middle of the draw the att and def get set to null
        int[] atti=att;
        int[] defi=def;
        
        // this is the max defend dice allowed for this battle
        int deadDice = myrisk.hasArmiesInt(c2num);
        if (deadDice > myrisk.getGame().getMaxDefendDice()) {
            deadDice = myrisk.getGame().getMaxDefendDice();
        }

        int w = getWidth();
        int diceWidth = red_dice.getWidth();
        
        int ax = w/2 - MoveDialog.distanceFromCenter - diceWidth/2;
        int dx = w/2 + MoveDialog.distanceFromCenter - diceWidth/2;

        int y2 = y1 + red_dice.getHeight() + XULLoader.adjustSizeToDensity(1);
        int y3 = y2 + red_dice.getHeight() + XULLoader.adjustSizeToDensity(1);

        // selecting the number of attacking dice
        if (max != 0 && canRetreat) {

                g.drawSprite(red_dice, DICE_NORMAL, ax, y1);

                if (nod > 1) {
                    g.drawSprite( red_dice , DICE_NORMAL , ax, y2);
                }
                else if (max > 1) {
                    g.drawSprite( red_dice , DICE_DARK , ax, y2 );
                }

                if (nod > 2) {
                    g.drawSprite( red_dice, DICE_NORMAL , ax, y3 );
                }
                else if (max > 2) {
                    g.drawSprite( red_dice , DICE_DARK , ax, y3 );
                }

                // draw the dead dice

                g.drawSprite( blue_dice , DICE_DARK , dx, y1 );

                if (deadDice > 1) {
                    g.drawSprite( blue_dice , DICE_DARK , dx, y2 );
                }

                if (deadDice > 2) {
                    g.drawSprite( blue_dice , DICE_DARK , dx, y3 );
                }

        }
        // selecting the number of dice to defend
        else if (max != 0 ) {

                g.drawSprite( blue_dice , DICE_NORMAL , dx, y1 );

                if (nod > 1) {
                    g.drawSprite( blue_dice , DICE_NORMAL , dx, y2 );
                }
                else if (max > 1) {
                    g.drawSprite( blue_dice , DICE_DARK , dx, y2 );
                }


                if (nod > 2) {
                    g.drawSprite( blue_dice , DICE_NORMAL , dx, y3 );
                }
                else if (max > 2) {
                    g.drawSprite( blue_dice , DICE_DARK , dx, y3 );
                }

        }
        // battle open and waiting for the attacker to select there number of dice
        else if (max == 0 && nodd == 0 && atti == null && defi == null ) {

                // draw the dead dice
                g.drawSprite( blue_dice , DICE_DARK , dx, y1 );

                if (deadDice > 1) {
                    g.drawSprite( blue_dice , DICE_DARK , dx, y2 );
                }

                if (deadDice > 2) {
                    g.drawSprite( blue_dice , DICE_DARK , dx, y3 );
                }

                if (noda == 0) {

                        // draw dead dice for attacker
                        int AdeadDice = myrisk.hasArmiesInt(c1num)-1;
                        // we assume that the attacker can attack with max of 3 dice

                        g.drawSprite( red_dice , DICE_DARK , ax, y1 );

                        if (AdeadDice > 1) {
                                g.drawSprite( red_dice , DICE_DARK , ax, y2 );
                        }
                        if (AdeadDice > 2) {
                                g.drawSprite( red_dice , DICE_DARK , ax, y3 );
                        }

                }

        }

        // #####################################################
        // ##################### END DICE ######################

        final int SPINS_OFFSET = 3;

        if (noda != 0) {

                g.drawSprite( red_dice, SPINS_OFFSET+r.nextInt( 6 ) , ax, y1);

                if (noda > 1) {
                        g.drawSprite( red_dice, SPINS_OFFSET+r.nextInt( 6 ) , ax, y2);
                }
                if (noda > 2) {
                        g.drawSprite( red_dice, SPINS_OFFSET+r.nextInt( 6 ) , ax, y3);
                }

                //g.drawString("ROLLING ATTACKER " + noda +"    " + Math.random() , 50, 100);

        }

        if (nodd != 0) {

                g.drawSprite( blue_dice, SPINS_OFFSET+r.nextInt( 6 ) , dx, y1);

                if (nodd > 1) {
                        g.drawSprite( blue_dice, SPINS_OFFSET+r.nextInt( 6 ) , dx, y2);
                }
                if (nodd > 2) {
                    g.drawSprite( blue_dice, SPINS_OFFSET+r.nextInt( 6 ) , dx, y3);
                }

                //g.drawString("ROLLING DEFENDER " + nodd +"    " + Math.random(), 300, 100);

        }

        DirectGraphics g2 = DirectUtils.getDirectGraphics(g.getGraphics());
        
        int offset = (int)( red_dice.getWidth() / (29D/4D) +0.5 );
        int bottom = red_dice.getHeight() - offset   -1; // TODO not sure why -1??
        int halfDice = red_dice.getHeight()/2;
        
        if (atti != null && defi != null ) {
                {
                        int yCoords[] = {y1+offset, y1+bottom, y1+halfDice};
                        if (defi[0] >= atti[0]) {
                                int xCoords[] = {dx+offset, dx+offset, ax+bottom};
                                g2.fillPolygon(xCoords, 0,yCoords,0, xCoords.length, COLOR_BLUE);
                        }
                        else {
                                int xCoords[] = {ax+bottom, ax+bottom, dx+offset};
                                g2.fillPolygon(xCoords, 0, yCoords,0, xCoords.length, COLOR_RED);
                        }
                }
                if (atti.length > 1 && defi.length > 1) {
                        int yCoords[] = {y2+offset, y2+bottom, y2+halfDice};
                        if (defi[1] >= atti[1]) {
                                int xCoords[] = {dx+offset, dx+offset, ax+bottom};
                                g2.fillPolygon(xCoords, 0,yCoords, 0,xCoords.length,COLOR_BLUE);
                        }
                        else {
                                int xCoords[] = {ax+bottom, ax+bottom, dx+offset};
                                g2.fillPolygon(xCoords, 0,yCoords, 0,xCoords.length,COLOR_RED);
                        }
                }
                if (atti.length > 2 && defi.length > 2) {
                    int yCoords[] = {y3+offset, y3+bottom, y3+halfDice};
                    if (defi[2] >= atti[2]) {
                            int xCoords[] = {dx+offset, dx+offset, ax+bottom};
                            g2.fillPolygon(xCoords, 0,yCoords, 0,xCoords.length,COLOR_BLUE);
                    }
                    else {
                            int xCoords[] = {ax+bottom, ax+bottom, dx+offset};
                            g2.fillPolygon(xCoords,0, yCoords, 0,xCoords.length,COLOR_RED);
                    }
                }

                // draw attacker dice
                drawDice(true, atti[0] , ax, y1, g );

                if (atti.length > 1) {
                        drawDice(true, atti[1] , ax, y2, g );
                }
                if (atti.length > 2) {
                        drawDice(true, atti[2] , ax, y3, g );
                }

                // draw defender dice
                drawDice(false, defi[0] , dx, y1, g );

                if (defi.length > 1) {
                        drawDice(false, defi[1] , dx, y2, g );
                }

                if (defi.length > 2) {
                    drawDice(false, defi[2] , dx, y3, g );
                }
        }

    }

    public void drawDice(boolean isAttacker, int result,int dx,int dy,Graphics2D g) {
                g.translate(dx, dy);

		if (isAttacker) {
			g.drawSprite(red_dice, DICE_NORMAL , 0, 0 );
		}
		else {
			g.drawSprite(blue_dice, DICE_NORMAL , 0, 0 );
		}

                int w = red_dice.getWidth();
                
		int size= (int)(w / (29D/3D) +0.5);
                int close = (int)(w / (29D/7D) +0.5);
                int middle = (w-size)/2;
                int far = w-close-size;

                g.setColor( 0xC8FFFFFF );
                
		if (result==0) {
			g.fillOval(middle, middle, size, size);
		}
		else if (result==1) {
			g.fillOval(close, close, size, size);
			g.fillOval(far, far, size, size);
		}
		else if (result==2) {
			g.fillOval(close, close, size, size);
			g.fillOval(middle, middle, size, size);
			g.fillOval(far, far, size, size);
		}
		else if (result==3) {
			g.fillOval(close, close, size, size);
			g.fillOval(far, close, size, size);
			g.fillOval(far, far, size, size);
			g.fillOval(close, far, size, size);
		}
		else if (result==4) {
			g.fillOval(close, close, size, size);
			g.fillOval(far, close, size, size);
			g.fillOval(far, far, size, size);
			g.fillOval(close, far, size, size);
			g.fillOval(middle, middle, size, size);
		}
		else if (result==5) {
			g.fillOval(close, close, size, size);
			g.fillOval(far, close, size, size);
			g.fillOval(far, far, size, size);
			g.fillOval(close, far, size, size);
			g.fillOval(middle, close, size, size);
			g.fillOval(middle, far, size, size);
		}

		g.translate(-dx, -dy);
    }

}

