package com.cross.gfw.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.json.JSONArray;
import org.json.JSONObject;

import com.cross.gfw.entity.Config;
import com.cross.gfw.entity.Language;
import com.cross.gfw.task.AsyncTask;
import com.cross.gfw.task.BaseTask.onTaskExecuteListener;
import com.cross.gfw.util.FileUtils;

public class MainWindow extends BaseWindow implements ActionListener {

	public MainWindow(Language language, boolean firstUse) {
		this.language = language;
		this.firstUse = firstUse;
		
		initialize();
		
		pullManifest();
		
//		pullHostsLine();
	}
	
	private void initialize() {
		
		JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        // top
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 0));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));        
        
        // main topic
        mTopicText = new JLabel(language.mainTopicText);
        mTopicText.setFont(new Font(Config.FONT, Font.ROMAN_BASELINE, 15));
        mTopicText.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 0));
        leftPanel.add(mTopicText);
        
        // sub topic
        mUpdateText = new JLabel();
        mUpdateText.setFont(new Font(Config.FONT, Font.ROMAN_BASELINE, 13));
        mUpdateText.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 0));
        if (!firstUse) {
        	mUpdateText.setText(String.format(language.updateDateStr, Config.LOCAL_DATE));
        }
        leftPanel.add(mUpdateText);
        
        mTipsText = new JLabel();
        mTipsText.setFont(new Font(Config.FONT, Font.ROMAN_BASELINE, 13));
        mTipsText.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 0));
        
        if (firstUse) {
        	mTipsText.setText("歡迎使用");
        }
        
        leftPanel.add(mTipsText);
        
        topPanel.add(leftPanel);

        ImageIcon temp = new ImageIcon("res/images/java.png");
        Image image = temp.getImage().getScaledInstance(50, 94, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 25));
        topPanel.add(label, BorderLayout.EAST);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);

        topPanel.add(separator, BorderLayout.SOUTH);

        basic.add(topPanel);

        // middle        
        mTextArea = new JTextArea2();
        mTextArea.setFont(new Font(Config.FONT, Font.ROMAN_BASELINE, 15));
        mTextArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(mTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25));
        basic.add(scrollPane);

        // bottom
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setMaximumSize(new Dimension(450, 100));
        
        mButton = new JButton();
        mButton.setFont(new Font(Config.FONT, Font.ROMAN_BASELINE, 13));
        mButton.setAlignmentX(CENTER_ALIGNMENT);
        mButton.setMnemonic(KeyEvent.VK_C);
        mButton.addActionListener(this);
        mButton.setEnabled(false);
        bottom.add(mButton);

        JLabel hLabel = new JLabel();
        hLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        bottom.add(hLabel);
        
        basic.add(bottom);

        setTitle(language.mainWindowTitle);
        setSize(new Dimension(450, 500));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String btnText = mButton.getText();
		if(language.btnOk.equals(btnText)) {
			Config.USR = mTextArea.getText().trim();
			if(Config.isGranted(Config.USR)) {
				saveConfig();
				pullHostsLine();
			}
		} else if(language.btnCheck2Update.equals(btnText)) {
			checkForUpdate();
		} else if(language.btnUpdate.equals(btnText)) {
			pullHostsLine();
		}
	}
	
	private void saveConfig() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				JSONObject jo = new JSONObject();
				jo.put("usr", Config.USR);
				jo.put("date", Config.REMOT_DATE);
				FileUtils.str2File(Config.FILE, jo.toString());
			}
		}).start();
	}
	
	private void pullManifest() {
		AsyncTask pullManifest = new AsyncTask(Config.MANIFEST_URL);
		pullManifest.setOnTaskExecuteListener(new onTaskExecuteListener<String>() {
			
			@Override
			public void onPreExecute() {
				mTextArea.setText("");
				mButton.setText(language.btnUpdating);
			}
			
			@Override
			public void onConnectError() {
				System.out.println("Internet is not OK!");
			}
			
			@Override
			public void onReadLine(String line) {}
			
			@Override
			public void onPostExecute(String data) {
				JSONObject jo = new JSONObject(data);
				
				Config.REMOT_DATE = jo.optString("updateDate");
				
				JSONArray grantList = jo.optJSONArray("grant");
				for (int i=0; i<grantList.length(); ++i) {
					Config.grant(grantList.optString(i));
				}
				
				if(firstUse) {
					mTipsText.setText(language.typeInGrantStr);
					mTextArea.setEditable(true);
					mButton.setText(language.btnOk);
				} else {
					mButton.setText(language.btnCheck2Update);
				}
				mButton.setEnabled(true);
			}
		});
		pullManifest.start();
	}
	
	private void pullHostsLine() {
		AsyncTask pullHostLine = new AsyncTask(Config.HOSTS_URL);
		pullHostLine.setOnTaskExecuteListener(new onTaskExecuteListener<String>() {

			@Override
			public void onPreExecute() {
				mButton.setText(language.btnUpdating);
				mButton.setEnabled(false);
			}

			@Override
			public void onConnectError() {}

			@Override
			public void onReadLine(String line) {
				mTextArea.append(line);
			}

			@Override
			public void onPostExecute(String result) {
				mButton.setText(language.btnRepleaceHost);
				mButton.setEnabled(true);
			}
		});
		pullHostLine.start();
	}
	
	private void checkForUpdate() {
		AsyncTask checkForUpdate = new AsyncTask(Config.MANIFEST_URL);
		checkForUpdate.setOnTaskExecuteListener(new onTaskExecuteListener<String>() {

			@Override
			public void onPreExecute() {
				mButton.setText(language.btnUpdating);
				mButton.setEnabled(false);
			}

			@Override
			public void onConnectError() {}

			@Override
			public void onReadLine(String line) {}

			@Override
			public void onPostExecute(String data) {
				JSONObject jo = new JSONObject(data);
				Config.REMOT_DATE = "20160929";//jo.optString("updateDate");
				
				if(Config.isNewVersionAvailable()) {
					mTipsText.setText(language.foundNewVerStr);
					mButton.setText(language.btnUpdate);
				} else {
					mTipsText.setText(language.notFoundNewVerStr);
					mButton.setText(language.btnCheck2Update);
				}
				mButton.setEnabled(true);
			}
		});
		checkForUpdate.start();
	}
	
	private JLabel mTopicText;
	private JLabel mUpdateText;
	private JLabel mTipsText;
	
	private JTextArea2 mTextArea;
	private JButton mButton;
	
	private boolean firstUse;
	
	private static final long serialVersionUID = 2511447919095026800L;
}
