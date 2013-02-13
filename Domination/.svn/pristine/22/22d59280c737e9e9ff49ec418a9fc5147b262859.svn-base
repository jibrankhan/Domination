// Yura Mamyrin

package net.yura.domination.ui.flashgui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.guishared.StatsPanel;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * <p> Statistics Dialog for FlashGUI </p>
 * @author Yura Mamyrin
 */

public class StatsDialog extends JDialog implements ActionListener {

	private BufferedImage Back;
	private Risk myrisk;
	private StatsPanel graph;
	private java.util.ResourceBundle resb;
        private ButtonGroup group;

	public StatsDialog(Frame parent, boolean modal, Risk r) {

		super(parent, modal);

		myrisk = r;

		Back = RiskUIUtil.getUIImage(this.getClass(),"graph.jpg");

		initGUI();

		setResizable(false);

		pack();

	}

	/** This method is called from within the constructor to initialize the form. */

	/**
	 * Initialises the GUI
	 */
	private void initGUI() {

		resb = TranslationBundle.getBundle();

		setTitle( resb.getString("swing.tab.statistics") );

		JPanel thisgraph = new JPanel();
                thisgraph.setBorder( new FlashBorder(
                        Back.getSubimage(100, 0, 740, 50),
                        Back.getSubimage(0, 0, 50, 400),
                        Back.getSubimage(100, 182, 740, 150),
                        Back.getSubimage(50, 0, 50, 400)
                        ) );

		Dimension d = new Dimension(740, 600);
		thisgraph.setPreferredSize(d);
		thisgraph.setMinimumSize(d);
		thisgraph.setMaximumSize(d);

		thisgraph.setLayout(null);


                group = new ButtonGroup();


		int x=49;
		int y=483;

		int w=107;
		int h=33;

		int s=1;

                AbstractButton button1 = makeButton("countries",x,y,w,h,s);
                button1.setSelected(true);
		thisgraph.add(button1);

		x=x+w;
		s++;

		thisgraph.add(makeButton("armies",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("kills",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("casualties",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("reinforcements",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("continents",x,y,w,h,s));

		x=49;
		y=y+h;
		s++;

		thisgraph.add(makeButton("empire",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("attacks",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("retreats",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("victories",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("defeats",x,y,w,h,s));

		x=x+w;
		s++;

		thisgraph.add(makeButton("attacked",x,y,w,h,s));



		graph = new StatsPanel(myrisk);
		graph.setBounds(50,50,640,400);

		thisgraph.add(graph);

		getContentPane().add(thisgraph);

		addWindowListener(
                    new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent evt) {
                            exitForm();
                        }
                    }
		);

	}

	public void actionPerformed(ActionEvent a) {
            showGraph( Integer.parseInt( a.getActionCommand() ) );
	}

        public void setVisible(boolean b) {
            super.setVisible(b);
            
            if (b) {
                showGraph( Integer.parseInt( group.getSelection().getActionCommand() ) );
            }
        }
        
        public void showGraph(int type) {
		graph.repaintStats( type );
		graph.repaint();            
        }

	/**
	 * Closes the GUI
	 */
	private void exitForm() {
		((GameFrame)getParent()).displayGraph();
	}
        
        private AbstractButton makeButton(String a, int x,int y,int w,int h,int s) {

                AbstractButton statbutton = new JToggleButton(resb.getString("swing.toolbar."+a));
                statbutton.setActionCommand(s+"");
                statbutton.addActionListener( this );
                statbutton.setBounds(x, y, w , h );
                group.add(statbutton);

                NewGameFrame.sortOutButton( statbutton, Back.getSubimage(x+100,y-433+165,w,h), Back.getSubimage(x+100,y-433,w,h), Back.getSubimage(x+100,y-433+66,w,h) );

                return statbutton;
        }

}
