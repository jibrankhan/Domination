package net.yura.domination.mobile.flashgui;

import android.graphics.ColorMatrix;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.mapstore.MapChooser;
import net.yura.domination.mobile.PicturePanel;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.Font;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.Frame;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.layout.BoxLayout;
import net.yura.mobile.gui.layout.XULLoader;
import net.yura.mobile.util.Properties;

/**
 * @author Yura
 */
public class CardsDialog extends Frame implements ActionListener {

	private Risk myrisk;
	private PicturePanel pp;

	private Panel myCardsPanel;
	private CardPanel extraArmiesCard;
        private Component NumArmies;

	private Image Infantry;
	private Image Cavalry;
	private Image Artillery;
	private Image Wildcard;

	private Button tradeButton;
	private boolean canTrade;

	private Properties resb = GameActivity.resb;

	/**
	 * Creates a new CardsDialog
	 * @param parent decides the parent of the frame
	 * @param modal
	 * @param r the risk main program
	 */

	public CardsDialog(Risk r, PicturePanel p) {
		myrisk = r;
		pp=p;

                Image cards = Midlet.createImage("/cards.png");
                        
                int w = cards.getWidth()/4;
                int h = cards.getHeight();

		
		Cavalry = Image.createImage(cards, 0, 0, w, h, 0);
		Infantry = Image.createImage(cards, w, 0, w, h, 0);
		Artillery = Image.createImage(cards, w*2, 0, w, h, 0);
                Wildcard = Image.createImage(cards, w*3, 0, w, h, 0);

                
                XULLoader loader;
                try {
                    loader = XULLoader.load( Midlet.getResourceAsStream("/cards.xml") , this, resb);
                }
                catch(Exception ex) {
                    throw new RuntimeException(ex);
                }
                
                setContentPane( (Panel)loader.getRoot() );
                
		setTitle(resb.getProperty("cards.title"));
		setMaximum(true);

                NumArmies = ((Component)loader.find("NumArmies"));

                myCardsPanel = (Panel)loader.find("myCardsPanel");
		myCardsPanel.setLayout( new BoxLayout( Graphics.HCENTER ) );


		tradeButton = (Button)loader.find("tradeButton");

	}
        
        public void setupNumArmies() {
            
            String text;
            int cardsMode = myrisk.getGame().getCardMode();
            
            // return resb.getString("cards.nexttrade").replaceAll( "\\{0\\}", "" + resb.getString("cards.fixed"));
            if(cardsMode==RiskGame.CARD_FIXED_SET || cardsMode==RiskGame.CARD_ITALIANLIKE_SET) {
                
                List<CardPanel> cards = getSelectedCards();

                int trade = 0;
                if (cards.size() == 3) {
                    trade = myrisk.getGame().getTradeAbsValue(cards.get(0).card.getName(), cards.get(1).card.getName(), cards.get(2).card.getName(), cardsMode);
                }
                
                if (trade > 0) {
                    text= RiskUtil.replaceAll(resb.getString("cards.nexttrade"), "{0}", String.valueOf( trade ) );
                }
                else if(cardsMode==RiskGame.CARD_FIXED_SET) {
                    text= resb.getString("cards.fixed");
                }
                else { // if(cardsMode==RiskGame.CARD_ITALIANLIKE_SET)
	            text= resb.getString("cards.italianlike");
	        }
            }
            else {
		 text= RiskUtil.replaceAll(resb.getString("cards.nexttrade"), "{0}", String.valueOf( myrisk.getNewCardState() ) );
            }
            
            NumArmies.setValue( text );
	}
        
        List<CardPanel> getSelectedCards() {
            List<CardPanel> selected = new ArrayList();
            List<CardPanel> all = myCardsPanel.getComponents();
            for (CardPanel cp :all) {
                if (cp.isSelected()) {
                    selected.add(cp);
                }
            }
            
            // if we have a extra armies card, put it at the start
            if (extraArmiesCard!=null) {
                assert selected.contains(extraArmiesCard);
                selected.remove(extraArmiesCard);
                selected.add(0, extraArmiesCard);
            }
            
            return selected;
        }

    public void actionPerformed(String actionCommand) {
        if ("done".equals(actionCommand)) {
            setVisible(false);
        }
        else if ("trade".equals(actionCommand)) {
            
            List<CardPanel> cards2 = getSelectedCards();

            if (cards2.size()==3) {

                myrisk.parser("trade "+cards2.get(0).getCardName() + " " + cards2.get(1).getCardName() + " " + cards2.get(2).getCardName() );

                for (CardPanel cp:cards2) {
                    myCardsPanel.remove(cp);
                }
                extraArmiesCard = null;

                tradeButton.setFocusable(false);

                setupNumArmies();
                
                revalidate();
                repaint();

            }
            
        }
        else {
            throw new RuntimeException("unknown command "+actionCommand);
        }
    }
        
        

	public void setup(boolean ct) {
		canTrade=ct;

		myCardsPanel.removeAll();

		extraArmiesCard = null;

		List<Card> cards = myrisk.getCurrentCards();
		for (int c=0; c < cards.size(); c++) {
			Component cp = new CardPanel( (Card)cards.get(c) );
			myCardsPanel.add(cp);
		}

                tradeButton.setFocusable(false);
                
                setupNumArmies();

	}

        boolean isOwnedCurrentPlayer(CardPanel cp) {
            return cp.card.getCountry()!=null && myrisk.isOwnedCurrentPlayerInt( cp.card.getCountry().getColor() );
        }
        
	class CardPanel extends Button {

		private Card card;

		/**
		 * Constructor of for the panel
		 * @param c The card
		 */
		public CardPanel (Card c) {
			card=c;

			int cardWidth=XULLoader.adjustSizeToDensity(50);
			//int cardHeight=100;

                        // height will be set by the scrollarea height in the XML file
			setPreferredSize( cardWidth, -1 );
			
			setName("Card");
		}

		/**
		 * Paints the panel
		 * @param g The graphics
		 */
                @Override
		public void paintComponent(Graphics2D g) {

                        //g2.setColor( 0xAA000000 );
			//g2.fillRoundRect(0, 0, getWidth(), getHeight() ,5,5);
                        //g2.setColor( 0xFF000000 );
                        //g2.drawRoundRect(5, 5, getWidth()-10, getHeight()-10 ,5,5);
                    
                        int imgSize = MapChooser.adjustSizeToDensityFromMdpi(50);
                        
			if (!(card.getName().equals(Card.WILDCARD))) {

				//String text = card.getCountry().getName(); // Display

				Image i = pp.getCountryImage( card.getCountry().getColor() );
                                
                                int ownerColor=0;
                                
                                ColorMatrix m = PicturePanel.RescaleOp( 0.5f, -1.0f);
                                m.preConcat(PicturePanel.gray);
                                if ( isOwnedCurrentPlayer( this ) ) {
                                    ownerColor = card.getCountry().getOwner().getColor();
                                    m.postConcat( PicturePanel.getMatrix( PicturePanel.colorWithAlpha(ownerColor, 100) ) );
                                }
                                g.getGraphics().setColorMarix(m);
                                g.drawScaledImage(i, (getWidth()-imgSize)/2, getHeight()/2 - imgSize, imgSize, imgSize);
                                g.getGraphics().setColorMarix(null);
                                
                                if (this == extraArmiesCard) {
                                    
                                    g.setColor(ownerColor);
                                    
                                    Font f = g.getFont();
                                    int w = f.getHeight();
                                    int x = (getWidth()-w)/2;
                                    int y = getHeight()/2 - imgSize/2 - w/2;
                                    g.fillOval(x, y, w, w);
                                    g.setColor( ColorUtil.getTextColorFor(ownerColor) );
                                    g.drawString("+"+Player.noaFORcard, x, y);
                                }
                                
			}

                        Image img = getCardImage();
			g.drawImage( img , (getWidth()-img.getWidth())/2 , (getHeight()-img.getHeight())/2  );


		}

                Image getCardImage() {
                        String name = card.getName();
                        if (Card.INFANTRY.equals(name)) {
                                return Infantry;
                        }
                        if (Card.CAVALRY.equals(name)) {
                                return Cavalry;
                        }
                        if (Card.CANNON.equals(name)) {
                                return Artillery;
                        }
                        return Wildcard;
                }
                
		/**
		 * Gets the card name
		 * @return String The card name
		 */
		public String getCardName() {
			if (card.getName().equals( Card.WILDCARD )) {
                            return card.getName();
                        }
                        else {
                            return String.valueOf( card.getCountry().getColor() );
			}
		}

                @Override
                protected void toggleSelection() {
                    setSelected(!isSelected());
                }

                @Override
                public void fireActionPerformed() {
                    super.fireActionPerformed();

                    if (!isSelected() && extraArmiesCard==this) {
                        CardPanel newSelected=null;
                        for (CardPanel cp: (List<CardPanel>)myCardsPanel.getComponents() ) {
                            if ( cp.isSelected() && isOwnedCurrentPlayer(cp) ) {
                                newSelected = cp;
                                break;
                            }
                        }
                        extraArmiesCard = newSelected;
                    }
                    else if (isSelected() && extraArmiesCard==null && isOwnedCurrentPlayer(this) ) {
                        extraArmiesCard = this;
                    }

                    List<CardPanel> trades = getSelectedCards();
                    tradeButton.setFocusable( canTrade && trades.size() == 3 && myrisk.canTrade( trades.get(0).getCardName() , trades.get(1).getCardName(), trades.get(2).getCardName() ) );
                    tradeButton.repaint();
                    setupNumArmies();
                }
                
                @Override
                public String toString() {
                    return card.toString();
                }
        }
}
