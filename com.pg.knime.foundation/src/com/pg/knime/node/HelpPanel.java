package com.pg.knime.node;

import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class HelpPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextArea txtLabel = new JTextArea();
	
	public HelpPanel ( String iconLabel, String textLabel ) {
		
		setBorder(new EmptyBorder(0, 0, 0, 0) );
		
		URL imgURL = getClass().getResource("support-icon.png");
		ImageIcon icon = new ImageIcon(imgURL, iconLabel );
	
		txtLabel.setBackground(this.getBackground());
		txtLabel.setEditable(false);
		txtLabel.setLineWrap(true);
		txtLabel.setWrapStyleWord(true);
		txtLabel.setText(textLabel);
		txtLabel.setFont(getFont());
		
		setLayout(new GridBagLayout());
        add(new JLabel(icon), StandardNodeDialogPane.getGBC(0, 0, 0, 0));
        add(txtLabel, StandardNodeDialogPane.getGBC(1, 0, 100, 0));
        
	}
	
	public JTextArea getTextArea() {
		return this.txtLabel;
	}
	
	
}
