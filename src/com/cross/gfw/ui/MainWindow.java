package com.cross.gfw.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

import com.cross.gfw.entity.Language;
import com.cross.gfw.task.PullManifest;
import com.cross.gfw.task.PullManifest.OnPullManifestListener;

public class MainWindow extends BaseWindow implements OnPullManifestListener {

	public MainWindow(Language language) {
		this.language = language;
		
		PullManifest pullManifest = new PullManifest();
		pullManifest.setOnPullManifestListener(this);
		new Thread(pullManifest).start();
		
		initialize();
	}
	
	private void initialize() {
		
		JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        // top
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 0));
        JLabel hint = new JLabel("JDeveloper Productivity Hints");
        hint.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 0));
        topPanel.add(hint);

        ImageIcon icon = new ImageIcon("res/images/java.png");
        
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(label, BorderLayout.EAST);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);

        topPanel.add(separator, BorderLayout.SOUTH);

        basic.add(topPanel);

        // middle        
        mTextPane = new JTextPane();
        mTextPane.setContentType("text/html");
        mTextPane.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(mTextPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        basic.add(scrollPane);

        // bottom
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setMaximumSize(new Dimension(450, 0));
        
        mButton = new JButton(language.getBtnCheck2UpdateText());
        mButton.setMnemonic(KeyEvent.VK_C);

        bottom.add(mButton);
        basic.add(bottom);

        setTitle(language.getMainWindowTitle());
        setSize(new Dimension(450, 500));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	@Override
	public void onPreExecute() {
		System.out.println("Start Pull manifest file");
	}
	
	@Override
	public void onPostExecute(String data) {
		System.out.println(data);
	}
	
	private JTextPane mTextPane;
	private JButton mButton;
	
	private static final long serialVersionUID = 2511447919095026800L;
}
