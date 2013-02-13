// Yura Mamyrin

package net.yura.domination.engine.guishared;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * About Dialog
 * @author Yura Mamyrin
 */
public class AboutDialog extends JDialog {

	private JPanel contentPane = new JPanel();
	private JLabel prodLabel = new JLabel();
	private JLabel verLabel = new JLabel();
	private JLabel authLabel = new JLabel();
	private JLabel copLabel = new JLabel();
	private JTextArea commentField = new JTextArea();
	private JPanel btnPanel = new JPanel();
	private JButton okButton = new JButton();

	private GridBagLayout contentPaneLayout = new GridBagLayout();
	private FlowLayout btnPaneLayout = new FlowLayout();

	private JPanel infoPanel = new JPanel();
	private JTextArea info1 = new JTextArea();
	private JTextArea info2 = new JTextArea();

	/**
	 * Creates a new AboutDialog
	 * @param parent decides the parent of the frame
	 * @param modal
	 * @param p contains the product
	 * @param v contains the GUI version
	 */

	public AboutDialog(Frame parent, boolean modal, String product, String v) {
		super(parent, modal);

		java.util.ResourceBundle resb = TranslationBundle.getBundle();

		String version=resb.getString("about.version")+" " + v;
		String author = " Yura Mamyrin (yura@yura.net)";
		String title		= resb.getString("about.title");
                
                int year = Calendar.getInstance().get(Calendar.YEAR);
                
		String copyright	= resb.getString("about.copyright").replaceAll("\\{0\\}", String.valueOf(year) );
		String comments	= " " + resb.getString("about.comments");

		// setSize(280,250); // (214,180)

		contentPane.setLayout(contentPaneLayout);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		prodLabel.setText(product);
		contentPane.add(prodLabel,
				new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1,
						0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

		verLabel.setText(version);
		contentPane.add(verLabel,
				new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1,
						0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

		authLabel.setText(resb.getString("about.author") + author);
		contentPane.add(authLabel,
				new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1,
						0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

		copLabel.setText(copyright);
		contentPane.add(copLabel,
				new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1,
						0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));

		// commentField.setWrapStyleWord(true);
		// commentField.setLineWrap(true);

		commentField.setBackground(copLabel.getBackground());
		commentField.setForeground(copLabel.getForeground());
		commentField.setFont(copLabel.getFont());
		commentField.setText(comments);
		commentField.setEditable(false);
		commentField.setOpaque(false);

		JLabel image = new JLabel(new javax.swing.ImageIcon( AboutDialog.class.getResource("logo.png") ), JLabel.CENTER);

		//Dimension size = new Dimension(120,120);

		//image.setPreferredSize(size);
		//image.setMinimumSize(size);

		image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		//image.setBorder( new javax.swing.border.EtchedBorder() );

		btnPanel.setLayout(btnPaneLayout);
		okButton.setText(" " + resb.getString("about.okbutton") + " ");
		okButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeDialog();
					}
				});
		btnPanel.add(okButton);
		okButton.setDefaultCapable( true);

		infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.X_AXIS));
		info1.setText(" " + resb.getString("about.infopanel"));

		info2.setText(

			RiskUIUtil.getSystemInfoText() + " " + resb.getString("about.compiledfor")

		);

		info1.setBackground(copLabel.getBackground());
		info1.setForeground(copLabel.getForeground());
		info1.setFont(copLabel.getFont());
		info1.setEditable(false);
		info1.setOpaque(false);

		info2.setBackground(copLabel.getBackground());
		info2.setForeground(copLabel.getForeground());
		info2.setFont(copLabel.getFont());
		info2.setEditable(false);
		info2.setOpaque(false);

		infoPanel.add(info1);
		infoPanel.add(info2);
		infoPanel.setOpaque(false);

		//info1.setFocusable( false);
		//info2.setFocusable( false);
		//commentField.setFocusable( false);




		JEditorPane editorPane1 = new JEditorPane();
		JEditorPane editorPane2 = new JEditorPane();
		JEditorPane editorPane3 = new JEditorPane();

		editorPane1.setEditable(false);
		editorPane2.setEditable(false);
		editorPane3.setEditable(false);

		JTabbedPane tabbedpane = new JTabbedPane();
		tabbedpane.addTab( resb.getString("about.tab.sysinfo") , infoPanel);
		tabbedpane.addTab( resb.getString("about.tab.credits"), new JScrollPane(editorPane1) );
		tabbedpane.addTab( resb.getString("about.tab.license"), new JScrollPane(editorPane2) );
		tabbedpane.addTab( resb.getString("about.tab.changelog"), new JScrollPane(editorPane3) );

		JPanel allcontent = new JPanel();

		// allcontent.setLayout(formLayout);
		allcontent.setLayout(new java.awt.GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new java.awt.Insets(3, 3, 3, 3);


		c.gridx = 0; // col
		c.gridy = 0; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		allcontent.add(image, c);


		c.gridx = 1; // col
		c.gridy = 0; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		allcontent.add(contentPane, c);


		c.gridx = 0; // col
		c.gridy = 1; // row
		c.gridwidth = 2; // width
		c.gridheight = 1; // height
		allcontent.add(commentField, c);



		getContentPane().add(allcontent, BorderLayout.NORTH);

		getContentPane().add( tabbedpane );

		getContentPane().add(btnPanel, BorderLayout.SOUTH);



		setTitle(title);
		//setResizable(false);
		okButton.requestFocus();


		addWindowListener(
				new java.awt.event.WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						closeDialog();
					}
				});


		pack();
		Dimension size = getPreferredSize();

		try {

			setMinimumSize(size);

		}
		catch(NoSuchMethodError ex) {

			// must me java 1.4
			setResizable(false);

		}


		addTextToTextBox(editorPane1,"help/game_credits.htm");
		addTextToTextBox(editorPane2,"gpl.txt");
		addTextToTextBox(editorPane3,"ChangeLog.txt");

		//setSize(size);

	}

	private static void addTextToTextBox(JEditorPane a,String n) {

		try {

			if (n.endsWith(".htm") || n.endsWith(".html")) {

				a.setContentType( "text/html" );
			}

			a.read(RiskUtil.openStream(n) , null );

		}
		catch(Exception e) {

			a.setText( e.toString() );

		}

	}


	/** Closes the dialog */
	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
