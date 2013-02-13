package net.yura.domination.ui.swinggui;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import net.yura.domination.engine.guishared.ErdeAnsicht;
import net.yura.domination.engine.guishared.PicturePanel;

/**
 * @author Yura Mamyrin
 */

public class FX3DPanel extends JPanel implements ActionListener, SwingGUITab, MouseListener, MouseMotionListener {

	private JToolBar toolbar;

	private PicturePanel pp;
	private ErdeAnsicht fx3d;
	private boolean fxdone;

	public FX3DPanel(PicturePanel p) {

		pp = p;

		setName( "3D View" );

		setLayout( new GridBagLayout() ); // new FlowLayout(FlowLayout.CENTER, 30, 30)

		//setOpaque(false);

		toolbar = new JToolBar();

		toolbar.setRollover(true);
		toolbar.setFloatable(false);

		JButton spin = new JButton("Spin/Stop");
		spin.setActionCommand("spin");
		spin.addActionListener(this);
		toolbar.add(spin);

		JButton wire = new JButton("Wire On/Off");
		wire.setActionCommand("wire");
		wire.addActionListener(this);
		toolbar.add(wire);

		JButton info = new JButton("Info On/Off");
		info.setActionCommand("info");
		info.addActionListener(this);
		toolbar.add(info);

		fx3d = new ErdeAnsicht();

		Dimension mapSize = new Dimension(480,480);

		fx3d.setPreferredSize(mapSize);
		fx3d.setMinimumSize(mapSize);
		fx3d.setMaximumSize(mapSize);

		add(fx3d);

		fx3d.addMouseListener(this);
		fx3d.addMouseMotionListener(this);

		setBorder( BorderFactory.createLoweredBevelBorder() );

		//setBackground( Color.BLACK );

	}

	public void paintComponent(java.awt.Graphics g) {

		g.setColor( Color.BLACK );
		g.fillRect(0,0,getWidth(),getHeight());
	}

	public void setVisible(boolean v) {

		super.setVisible(v);

		if (v) {

			// sets things up
			if (!fxdone) { fx3d.init(); fxdone=true; }

			// gives it an image to use
                        Image img = pp.getImage();
                        if (img!=null) {
                            fx3d.settext( img );
                        }

			// does a repaint
			fx3d.zeichnen();
		}
		else {

			stopflag = true;

		}

	}

	private Thread timer;
	private boolean stopflag;

	public void actionPerformed(ActionEvent a) {

		if (a.getActionCommand().equals("spin")) {

			if (timer==null) {

				timer = new Thread() {

					public void run() {

						while (!stopflag) {

							fx3d.rotate(-3,0);

							try { Thread.sleep(100); }
							catch(InterruptedException e) {}

						}

						timer = null;

					}

				};

				stopflag = false;

				timer.start();

			}
			else {

				stopflag = true;

			}
		}
		else if (a.getActionCommand().equals("wire")) {

			fx3d.toggleWireframe();

		}
		else if (a.getActionCommand().equals("info")) {

			fx3d.toggleInfo();

		}
	}

	public JToolBar getToolBar() {

		return toolbar;

	}
	public JMenu getMenu() {

		return null;

	}

	// ###################################################################
	// #############    MouseListener, MouseMotionListener   #############
	// ###################################################################

//double phigrad0=0;
//double psigrad0=0;
private int xgedrueckt=0;
private int ygedrueckt=0;
//boolean gedrueckt=false;

public void mousePressed(MouseEvent e){
	//phigrad0=phigrad;
	//psigrad0=psigrad;
	xgedrueckt=e.getX();
	ygedrueckt=e.getY();
	//gedrueckt=true;
}

public void mouseReleased(MouseEvent e){
	//phigrad=phigrad0+(xgedrueckt-e.getX())/2;
	//psigrad=psigrad0-(ygedrueckt-e.getY())/2;
	//setrot(phigrad,psigrad);
	//zeichnen(true);
	//gedrueckt=false;
}

public void mouseDragged(MouseEvent e){	
	//if(gedrueckt==true){
		//phigrad=phigrad0+(xgedrueckt-e.getX())/2;
		//psigrad=psigrad0-(ygedrueckt-e.getY())/2;
		//setrot(phigrad,psigrad);
		//zeichnen(texturdreh);

		stopflag = true;
		fx3d.rotate( (xgedrueckt-e.getX())/2 , -(ygedrueckt-e.getY())/2 );


		xgedrueckt=e.getX();
		ygedrueckt=e.getY();

	//}
}

public void mouseMoved(MouseEvent e){}
public void mouseExited(MouseEvent e){}   
public void mouseEntered(MouseEvent e){}
public void mouseClicked(MouseEvent e){}



}
