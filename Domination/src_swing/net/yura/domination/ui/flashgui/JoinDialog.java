// Yura Mamyrin, Group D

package net.yura.domination.ui.flashgui;

import java.awt.event.MouseEvent;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.event.MouseInputListener;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * Join Game for FlashGUI
 * @author Yura Mamyrin
 */
public class JoinDialog extends JDialog implements MouseInputListener
{

	private Risk myrisk;
	private BufferedImage joingame;
	private JTextField serverField;
	private ResourceBundle resb;

	/**
	 * Constructor for the Dialogue
	 * @param parent Parent of the frame
	 * @param modal
	 * @param r The risk game
	 */
	public JoinDialog(Frame parent, boolean modal, Risk r)
	{
		super(parent, modal);

		serverField = new JTextField( r.getRiskConfig("default.host") );

		resb = TranslationBundle.getBundle();

		myrisk=r;

		joingame = RiskUIUtil.getUIImage(this.getClass(),"joingame.jpg");

		initGUI();

		setResizable(false);

		pack();

	}

	/** This method is called from within the constructor to initialize the form. */

	/**
	 * Initialises the GUI
	 */
	private void initGUI()
	{

		// set title
		setTitle(resb.getString("join.title"));

		serverField.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent a) {

						myrisk.parser("join "+serverField.getText() );

					}
				}
		);


		Dimension d = new Dimension(350, 190);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(d);
		layeredPane.setMinimumSize(d);
		layeredPane.setMaximumSize(d);

		joinPanel join = new joinPanel();
		join.setBounds(0, 0, (int)d.getWidth() , (int)d.getHeight() );
		join.addMouseListener(this);
		join.addMouseMotionListener(this);

		serverField.setBounds(149, 49, 150 , 25 );
		serverField.setBorder(null);
		serverField.selectAll();
		serverField.setOpaque(false);

		layeredPane.add(serverField, 0);
		layeredPane.add(join, 1);

		getContentPane().add(layeredPane);

		addWindowListener(
				new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent evt) {
						exitForm();
					}
				}
		);

	}

	/** Exit the Application */

	/**
	 * Closes the GUI
	 * @param evt Close button was pressed
	 */
	public void exitForm() {

		setVisible(false);
		dispose();

	}

	class joinPanel extends JPanel {

		/**
		 * paints the panel
		 * @param g The graphics
		 */
		public void paintComponent(Graphics g) {

//			  destination		source
			g.drawImage(joingame,0,0,350,190,     0,0,350,190,this); // top

			Graphics2D g2 = (Graphics2D)g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			FontRenderContext frc = g2.getFontRenderContext();
			Font font = g2.getFont();
			g2.setColor( Color.black );
			TextLayout tl;


			if (highlightButton==1) {
				g.drawImage( joingame ,43 ,93 ,43+120 ,93+55	,350 ,0 ,470 ,55 ,this );
			}
			else if (highlightButton==2) {
				g.drawImage( joingame ,187 ,93 ,187+120 ,93+55	,350 ,110 ,470 ,165 ,this );
			}

			else if (button==1) {
				g.drawImage( joingame ,43 ,93 ,43+120 ,93+55	,350 ,55 ,470 ,110 ,this );
			}
			else if (button==2) {
				g.drawImage( joingame ,187 ,93 ,187+120 ,93+55	,350 ,165 ,470 ,220 ,this );
			}

			tl = new TextLayout( resb.getString("join.servername") , font, frc);
			tl.draw( g2, (float) (100-tl.getBounds().getWidth()/2), (float)65 );

			tl = new TextLayout( resb.getString("join.cancel") , font, frc);
			tl.draw( g2, (float) (103-tl.getBounds().getWidth()/2), (float)125 );

			tl = new TextLayout( resb.getString("join.connect") , font, frc);
			tl.draw( g2, (float) (247-tl.getBounds().getWidth()/2), (float)125 );


		}

	}

	/**
	 * Works out what button needs to be pressed
	 * @param x x co-ordinate
	 * @param y y cp-ordinate
	 * @return int The type of the button
	 */
	public int insideButton(int x, int y) {

		int B=0;

		if (x >= 51 && x < 156 && y >=101 && y < 141 ) {
			B=1;
		}
		else if (x >= 195 && x < 300 && y >=101 && y < 141 ) {
			B=2;
		}

		return B;

	}

	private int button;
	private int currentButton;
	private int pressedButton;
	private int highlightButton;

	//**********************************************************************
	//                     MouseListener Interface
	//**********************************************************************

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Works out what to do when a mouse has been pressed
	 * @param e A Mouse event
	 */
	public void mousePressed(MouseEvent e) {

		highlightButton = 0;
		currentButton=insideButton(e.getX(),e.getY());

		if (currentButton != 0) {
			pressedButton = currentButton;
			button = currentButton;
			repaint();
		}

	}

	/**
	 * Works out what to do when a mouse has been released
	 * @param e A Mouse event
	 */
	public void mouseReleased(MouseEvent e) {

		if (pressedButton == currentButton) {

			if (button == 1) {
				exitForm();
			}
			else if (button == 2) {
				myrisk.parser("join "+serverField.getText() );
			}
		}

		if (button != 0) {
			button=0;
		}

		highlightButton=currentButton;
		repaint();

	}

	/**
	 * Works out what to do when a mouse has been moved
	 * @param e A Mouse event
	 */
	public void mouseMoved(MouseEvent e) {

		int oldhighlightButton = highlightButton;
		int newhighlightButton = insideButton(e.getX(),e.getY());

		if (oldhighlightButton != newhighlightButton) {
			highlightButton = newhighlightButton;
			repaint();
		}

	}

	/**
	 * Works out what to do when a mouse has been dragged
	 * @param e A Mouse event
	 */
	public void mouseDragged(MouseEvent e) {

		currentButton = insideButton(e.getX(),e.getY());

		if (pressedButton == currentButton ) {
			if (button!=pressedButton) {
				button=pressedButton;
				repaint();
			}
		}
		else {
			if (button !=0) {
				button = 0;
				repaint();
			}
		}

	}

}
