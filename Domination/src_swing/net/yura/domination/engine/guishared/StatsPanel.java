// Yura Mamyrin

package net.yura.domination.engine.guishared;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.List;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;

/**
 * Statistics Graphs Panel
 * @author Yura Mamyrin
 */
public class StatsPanel extends JPanel {

    //private int spX;
    //private int spY;
    private Risk risk;
    private BufferedImage graph;
    private boolean tooBig;

    public StatsPanel(Risk r) {

	//spX=x;
	//spY=y;

	risk=r;

	//Dimension size = new Dimension(spX , spY);

	//setPreferredSize(size);
	//setMinimumSize(size);
	//setMaximumSize(size);

    }

    public void paintComponent(Graphics g) {

	//super.paintComponent(g); 

	if (graph != null) {
	    g.drawImage(graph, 0, 0, this);
	}
	else {
	    g.fillRect(0,0,getWidth(),getHeight());
	}

    }

    public void repaintStats(int a) {

	BufferedImage tempgraph = new BufferedImage(getWidth(),getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB ); // spX, spY

	List players = ((RiskGame)risk.getGame()).getPlayersStats();

	int graphScale = 0;
        int maxTurns = 0;
	for (int i = 0; i < players.size(); i++) {

	    Player p = (Player)players.get(i);

	    int[] pstats= p.getStatistics(a);

            int max = pstats.length;
            if ( max > maxTurns) {
                maxTurns = max;
	    }

	    int temp=0;

	    for (int j = 0; j < pstats.length; j++) {

	      if (a==1 || a==2 || a==0 || a==6 || a==7) {
		if (pstats[j] > graphScale) {
		    graphScale = pstats[j];
		}
	      }
	      else {
		  temp += pstats[j];
	      }


	    }


		if (temp > graphScale) {
		    graphScale = temp;
		}
		temp=0;

        }

	// adds a space at the top and to the right of the graph
	graphScale++;
	maxTurns++;

	Graphics2D g2 = tempgraph.createGraphics();

	int xOffset = 30; // offset from the left
	int yOffset = 30; // offset from the bottom

	// size of devision
	gridSizeX = ((int)getSize().getWidth()-xOffset-20) /maxTurns; // the 20 is the right offset
	gridSizeY = ((int)getSize().getHeight()-yOffset-20) /graphScale; // the 20 is the top offset

	// the co-ords of the Zero Zero
	ZeroX = xOffset;
	ZeroY = (int)getSize().getHeight()-yOffset;

	if (gridSizeY==0) { gridSizeY=1; tooBig=true; graphScale /= 2; }
	else { tooBig=false; }

	int bob = Math.round(15f/gridSizeY);

	// draw - lines and numbers
	for (int i = 0; i <= graphScale ; i++) {

	    if ( i == graphScale || bob == 0 || ( i % bob )==0 ) {

		g2.setColor(Color.gray);   
		g2.drawLine(ZeroX,(int)(ZeroY-(i*gridSizeY)),(maxTurns*gridSizeX)+ZeroX,(int)(ZeroY-(i*gridSizeY)));

		g2.setColor(Color.white);

		String label = tooBig ? String.valueOf(i*2) : String.valueOf(i);

		g2.drawString(label, ZeroX-(6 + ( label.length()*7 )), (int)(ZeroY-(i*gridSizeY)+5));

	    }
	}

	int fred = Math.round(20f/gridSizeX);

	// draw | lines and numbers
	for (int i = 0; i <= maxTurns ; i++) {

	    g2.setColor(Color.gray);      
	    g2.drawLine((int)(ZeroX + (i*gridSizeX)),ZeroY,(int)(ZeroX +i*gridSizeX), (int)( ZeroY-( graphScale *gridSizeY) ) );

	    if ( i == maxTurns || fred == 0 || ( i % fred )==0 ) {

		g2.setColor(Color.white);
		g2.drawString(""+i,(int)(i*gridSizeX + ZeroX-3),ZeroY+20);

	    }
        }

	g2.setColor(Color.white);
	g2.drawLine(ZeroX, ZeroY, ZeroX+(maxTurns*gridSizeX), ZeroY); // -
	g2.drawLine(ZeroX, ZeroY, ZeroX, ZeroY-( graphScale *gridSizeY) ); // |

	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	// set hints       
	BasicStroke bs = new BasicStroke( 2.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER );
	g2.setStroke(bs);

	//draw each player graph.
	for (int i = 0; i < players.size(); i++) {
	    drawPlayerGraph(a, (Player)players.get(i) , g2);
	}

	g2.dispose();

	graph = tempgraph;

    }

    private int ZeroX;
    private int ZeroY;
    private int gridSizeX;
    private int gridSizeY;

    public void drawPlayerGraph(int a, Player p, Graphics2D g) {

	int[] PointToDraw = p.getStatistics(a);
	g.setColor(new Color( p.getColor() ) );

	int oldPoint = 0;
	int newPoint = 0;
	int i = 0;

	for (i = 0; i < PointToDraw.length; i++) {

	    if (a==1 || a==2 || a==0 || a==6 || a==7) {
                newPoint = tooBig ? PointToDraw[i]/2 : PointToDraw[i] ;
	    }
	    else {
                newPoint += tooBig ? PointToDraw[i]/2 : PointToDraw[i] ;
	    }

            int x1 = (int)(ZeroX + i*gridSizeX);
            int y1 = (int)(ZeroY-(oldPoint*gridSizeY));
            int x2 = (int)(ZeroX +(i+1)*gridSizeX);
            int y2 = (int)(ZeroY-(newPoint*gridSizeY));

            Color color = g.getColor();
            Stroke stroke = g.getStroke();

            if (Color.BLACK.equals( color )) {
                g.setColor(Color.WHITE);
                g.setStroke( new BasicStroke(3) );
                g.drawLine(x1,y1,x2,y2);
                g.setColor(color);
                g.setStroke(stroke);
            }

	    g.drawLine(x1,y1,x2,y2);
	    oldPoint = newPoint;
	}

        int x = (int)(ZeroX + i*gridSizeX);
        int y = (int)(ZeroY - (oldPoint*gridSizeY)+11);

        Color color = g.getColor();

        if (Color.BLACK.equals( color )) {
            g.setColor(Color.WHITE);
            g.drawString(p.getName(),x+1,y+1);
            g.drawString(p.getName(),x-1,y-1);
            g.drawString(p.getName(),x+1,y-1);
            g.drawString(p.getName(),x-1,y+1);
            g.setColor(color);
        }

	g.drawString(p.getName(),x,y);

    }

}
