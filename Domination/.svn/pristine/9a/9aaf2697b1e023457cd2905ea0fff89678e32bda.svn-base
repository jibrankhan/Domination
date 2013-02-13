package net.yura.domination.mobile.simplegui;

import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.mobile.MiniUtil;
import net.yura.domination.mobile.PicturePanel;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.border.LineBorder;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.ComboBox;
import net.yura.mobile.gui.components.Label;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.layout.FlowLayout;

/**
 * @author Yura Mamyrin
 */
public class GamePanel extends Panel {

    private ComboBox mapViewComboBox;
    private Risk risk;

    public GamePanel(Risk trisk,final PicturePanel pp) {
        risk = trisk;

                    setLayout(new FlowLayout());

                    Label mapLookLabel = new Label("Map Look:");

                    mapViewComboBox = new ComboBox();
                    Button closegame = new Button("closegame");
                    Button about = new Button("About");

                    //JButton leave = new JButton("leave");

                    mapViewComboBox.setPreferredSize(150 , 20);

                    mapViewComboBox.addItem("Continents");
                    mapViewComboBox.addItem("Ownership");
                    mapViewComboBox.addItem("Border Threat");
                    mapViewComboBox.addItem("Risk Card Ownership");
                    mapViewComboBox.addItem("Troop Strength");
                    mapViewComboBox.addItem("Connected Empire");

                    mapViewComboBox.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(String a) {
                                pp.repaintCountries( getMapView() );
                                pp.repaint();
                            }
                        }
                    );

                    Label playersLabel = new Label("Players:");

                    Panel players = new PlayersPanel();

                    players.setBorder(new LineBorder(0xFF000000,1));

                    players.setPreferredSize(120 , 20);

                    closegame.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(String a) {
                                risk.parser("closegame");
                            }
                        }
                    );

                    //leave.addActionListener(
                    //              new ActionListener() {
                    //                      public void actionPerformed(ActionEvent a) {
                    //                              go("leave");
                    //                      }
                    //              }
                    //);

                    about.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(String a) {
                                MiniUtil.showAbout();
                            }
                        }
                    );

                    setPreferredSize(PicturePanel.PP_X , 30);

                    add(mapLookLabel);
                    add(mapViewComboBox);
                    add(playersLabel);
                    add(players);
                    add(closegame);
                    add(about);
    }

    public int getMapView() {

            int tmp = mapViewComboBox.getSelectedIndex();
            int newview = -1;

            switch (tmp) {
                case 0: newview=PicturePanel.VIEW_CONTINENTS; break;
                case 1: newview=PicturePanel.VIEW_OWNERSHIP; break;
                case 2: newview=PicturePanel.VIEW_BORDER_THREAT; break;
                case 3: newview=PicturePanel.VIEW_CARD_OWNERSHIP; break;
                case 4: newview=PicturePanel.VIEW_TROOP_STRENGTH; break;
                case 5: newview=PicturePanel.VIEW_CONNECTED_EMPIRE; break;
                default: throw new RuntimeException();
            }

            return newview;

    }

    public void resetMapView() {
        mapViewComboBox.setSelectedIndex(0);
    }

    class PlayersPanel extends Panel {

            public void paintComponent(Graphics2D g) {

                    int[] colors = risk.getPlayerColors();

                    for (int c=0; c < colors.length ; c++) {
                            g.setColor( colors[c] );
                            g.fillRect( ((120/colors.length) * c) , 0 , (120/colors.length) , 20);
                    }

                    g.setColor( ColorUtil.getTextColorFor( colors[0] ) );

                    g.drawRect( 2 , 2 , (120/colors.length)-5 , 15);

                    g.setColor( 0xFF000000 );
                    g.drawLine( (120/colors.length)-1 , 0, (120/colors.length)-1, 19);

            }
    }

}
