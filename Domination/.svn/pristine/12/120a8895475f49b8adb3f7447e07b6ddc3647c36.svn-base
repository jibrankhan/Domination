// Yura Mamyrin, Group D

package net.yura.domination.ui.swinggui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.PicturePanel;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * <p> Cards Dialog for Swing GUI </p>
 * @author Yura Mamyrin
 */

public class CardsDialog extends JDialog {

    private Risk myrisk;
    private JPanel CardsPanel;
    private JPanel TradePanel;
    private JScrollPane CardsPlane;
    private BufferedImage Infantry;
    private BufferedImage Cavalry;
    private BufferedImage Artillery;
    private BufferedImage Wildcard;
    private JButton tradeButton;
    private boolean canTrade;
    private JLabel getNum;
    private PicturePanel pp;

    java.util.ResourceBundle resb = null;

    /**
     * Creates a new CardsDialog
     * @param parent decides the parent of the frame
     * @param modal
     * @param r the risk main program
     */

    public CardsDialog(Frame parent, PicturePanel p, boolean modal, Risk r, boolean ct) {
        super(parent, modal);
	myrisk = r;
	canTrade=ct;
	pp=p;

	resb = TranslationBundle.getBundle();

	tradeButton = new JButton(resb.getString("cards.trade"));

	// Toolkit.getDefaultToolkit().getImage( "" );

        Infantry = RiskUIUtil.getUIImage( this.getClass(),"infantry.gif" );
        Cavalry = RiskUIUtil.getUIImage( this.getClass(),"cavalry.gif" );
        Artillery = RiskUIUtil.getUIImage( this.getClass(),"artillery.gif" );
        Wildcard = RiskUIUtil.getUIImage( this.getClass(),"wildcard.gif" );

	CardsPanel = new JPanel();
	CardsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

	TradePanel = new JPanel();
	TradePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        initGUI();
        pack();

    }

    public void populate(Vector cards) {

        tradeButton.setEnabled(false);

	for (int c=0; c < cards.size(); c++) {
	    JPanel cp = new CardPanel( (Card)cards.elementAt(c) );
	    CardsPanel.add(cp);
	}

    }


    /** This method is called from within the constructor to initialize the dialog. */
    private void initGUI() {

        setTitle(resb.getString("cards.title"));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

	getNum = new JLabel();
	getNum.setText( getNumArmies() );

	JTextArea note = new JTextArea(resb.getString("cards.note"));

        note.setLineWrap(true);
        note.setWrapStyleWord(true);

        //note.setBackground(getNum.getBackground());
        note.setForeground(getNum.getForeground());
        note.setFont((new JLabel()).getFont());
        note.setEditable(false);
	note.setOpaque(false);

	Dimension noteSize = new Dimension(180, 120);

	note.setPreferredSize( noteSize );
	note.setMinimumSize( noteSize );
	note.setMaximumSize( noteSize );


	JButton okButton = new JButton(resb.getString("cards.done"));
	okButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            }
	);

	tradeButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {

		    myrisk.parser("trade "+((CardPanel)TradePanel.getComponent(0)).getCardName() + " " + ((CardPanel)TradePanel.getComponent(1)).getCardName() + " " + ((CardPanel)TradePanel.getComponent(2)).getCardName() );

		    TradePanel.remove(TradePanel.getComponent(2));
		    TradePanel.remove(TradePanel.getComponent(1));
		    TradePanel.remove(TradePanel.getComponent(0));

		    TradePanel.repaint();
		    TradePanel.validate();

		    getNum.setText( getNumArmies() );
		    tradeButton.setEnabled(false);


                }
            }
	);


	CardsPlane = new JScrollPane();
        CardsPlane.setOpaque(false); // this is needed as without it, the colors looks wrong on os x

	CardsPlane.getViewport().add(CardsPanel);

	Dimension CardsPlaneSize = new Dimension(550, 230);
	CardsPlane.setBorder(javax.swing.BorderFactory.createTitledBorder(resb.getString("cards.yourcards")));
	CardsPlane.setPreferredSize( CardsPlaneSize );
	CardsPlane.setMinimumSize( CardsPlaneSize );
	CardsPlane.setMaximumSize( CardsPlaneSize );

	Dimension TradePlaneSize = new Dimension(340, 210);
	TradePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resb.getString("cards.trade")));
	TradePanel.setPreferredSize( TradePlaneSize );
	TradePanel.setMinimumSize( TradePlaneSize );
	TradePanel.setMaximumSize( TradePlaneSize );

	//CardsPlane.add(cards);

	Dimension otherSize = new Dimension(200, 180);

	JPanel other = new JPanel();
	other.setPreferredSize( otherSize );
	other.setMinimumSize( otherSize );
	other.setMaximumSize( otherSize );

	other.add(note);
	other.add(getNum);
	other.add(tradeButton);
	other.add(okButton);


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new java.awt.Insets(3, 3, 3, 3);
        //c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        c.gridx = 0; // col
        c.gridy = 0; // row
        c.gridwidth = 2; // width
        c.gridheight = 1; // height
        getContentPane().add(CardsPlane, c);

        c.gridx = 0; // col
        c.gridy = 1; // row
        c.gridwidth = 1; // width
        c.gridheight = 1; // height
        getContentPane().add(TradePanel, c);

        c.gridx = 1; // col
        c.gridy = 1; // row
        c.gridwidth = 1; // width
        c.gridheight = 1; // height
        getContentPane().add(other, c);

        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    closeDialog(evt);
                }
            }
	);

    }

    public String getNumArmies() {
	// return " Next trade recieve " + myrisk.getNewCardState() + " troops";
	if (myrisk.getGame().getCardMode()==RiskGame.CARD_FIXED_SET) {
            return resb.getString("cards.fixed");
	}
        else if (myrisk.getGame().getCardMode()==RiskGame.CARD_ITALIANLIKE_SET) {
            return resb.getString("cards.italianlike");
	}
	else {
            return resb.getString("cards.nexttrade").replaceAll( "\\{0\\}", "" + myrisk.getNewCardState());
	}
    }

    class CardPanel extends JPanel implements MouseListener {

	private Card card;
	private BufferedImage grayImage;
	private BufferedImage highlightImage;
	private boolean select;

	public CardPanel (Card c) {
	    card=c;

	    this.addMouseListener(this);

	    int cardWidth=100;
	    int cardHeight=170;

	    select=false;

	    Dimension CardSize = new Dimension(cardWidth, cardHeight);
	    this.setPreferredSize( CardSize );
	    this.setMinimumSize( CardSize );
	    this.setMaximumSize( CardSize );

	    this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 1));

	    grayImage = new BufferedImage(cardWidth, cardHeight, java.awt.image.BufferedImage.TYPE_INT_RGB );
	    Graphics2D g2 = grayImage.createGraphics();

	    g2.setColor( Color.lightGray );
	    g2.fillRect(0, 0, cardWidth, cardHeight);

            FontRenderContext frc = g2.getFontRenderContext();
            Font font = g2.getFont();

	    if (!(card.getName().equals("wildcard"))) {

		g2.setColor( Color.black );
		TextLayout tl = new TextLayout( ((Country)card.getCountry()).getName() , font, frc); // Display
		tl.draw( g2, (float) (100/2-tl.getBounds().getWidth()/2), (float)15 );



		BufferedImage pictureB = pp.getCountryImage( ((Country)card.getCountry()).getColor() , false);

		int width = pictureB.getWidth();
		int height = pictureB.getHeight();

		if (width > 50) { width=50; }
		if (height > 50) { height=50; }

		Image i = pictureB.getScaledInstance(width,height, java.awt.Image.SCALE_SMOOTH );



		g2.drawImage( i , 25+ (25-(i.getWidth(this)/2)) ,25+ (25-(i.getHeight(this)/2)) ,null );

		if (card.getName().equals("Infantry")) {
		    g2.drawImage( Infantry ,15 ,85 ,null );
		}
		else if (card.getName().equals("Cavalry")) {
		    g2.drawImage( Cavalry ,15 ,85 ,null );
		}
		else if (card.getName().equals("Cannon")) {
		    g2.drawImage( Artillery ,15 ,85 ,null );
		}

	    }
	    else {
		g2.drawImage( Wildcard ,20 ,8 ,null );
	    }

	    g2.setColor( Color.black );
            TextLayout tl = new TextLayout( card.getName() , font, frc);
            tl.draw( g2, (float) (100/2-tl.getBounds().getWidth()/2), (float)160 );

	    highlightImage = new BufferedImage(cardWidth, cardHeight, java.awt.image.BufferedImage.TYPE_INT_RGB );

	    RescaleOp HighLight = new RescaleOp(1.5f, 1.0f, null);
	    HighLight.filter( grayImage , highlightImage );

	    g2.dispose();

	}

	public void paintComponent(Graphics g) {

	    super.paintComponent(g);

	    if (select) { g.drawImage( highlightImage ,0 ,0 ,this ); }
	    else { g.drawImage( grayImage ,0 ,0 ,this ); }

	}

	public String getCardName() {

	    if (!(card.getName().equals( Card.WILDCARD ))) { return ((Country)card.getCountry()).getColor()+""; }
	    else { return card.getName(); }

	}

	//**********************************************************************
	//                     MouseListener Interface
	//**********************************************************************

	public void mouseClicked(MouseEvent e) {

	    if ( this.getParent() == CardsPanel ) {
		if (TradePanel.getComponentCount() < 3) { CardsPanel.remove(this); select=false; TradePanel.add(this); }
		if (TradePanel.getComponentCount() == 3 && canTrade && myrisk.canTrade( ((CardPanel)TradePanel.getComponent(0)).getCardName() , ((CardPanel)TradePanel.getComponent(1)).getCardName(), ((CardPanel)TradePanel.getComponent(2)).getCardName() ) ) { tradeButton.setEnabled(true); }
	    }
	    else if ( this.getParent() == TradePanel ) {
		TradePanel.remove(this); select=false; CardsPanel.add(this);
		tradeButton.setEnabled(false);
	    }

	    CardsPanel.repaint();
	    TradePanel.repaint();

	    CardsPanel.validate();
	    TradePanel.validate();

	    CardsPlane.validate();

	}

	public void mouseEntered(MouseEvent e) {
	    select=true;
	    this.repaint();
	}

	public void mouseExited(MouseEvent e) {
	    select=false;
	    this.repaint();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

    }

    /** Closes the dialog */
    private void closeDialog(WindowEvent evt) {
        setVisible(false);
        dispose();
    }
}
