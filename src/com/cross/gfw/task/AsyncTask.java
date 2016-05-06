package com.cross.gfw.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask extends BaseTask {
	
	public AsyncTask(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		
		mListener.onPreExecute();
		
		InputStream is = null;
		HttpURLConnection mConnection = null;
		
		StringBuilder builder = new StringBuilder();
		try {
			
			URL url = new URL(this.url);
			mConnection = (HttpURLConnection) url.openConnection();
			mConnection.setConnectTimeout(CONN_TIME_OUT);
			mConnection.setReadTimeout(READ_TIME_OUT);
			if(NET_OK == mConnection.getResponseCode()) {
				is = mConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, CHARSET));
				String line = null;
				while (null != (line = reader.readLine())) {
					builder.append(line);
					builder.append("\n");
					mListener.onReadLine(line);
				}
				reader.close();
			} else {
				throw new Exception();
			}
			
		} catch (Exception e) {
			mListener.onConnectError();
		} finally {
			mConnection.disconnect();
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(0 < builder.length()) {
			mListener.onPostExecute(builder.toString());
		}
	}
	
	public void setOnTaskExecuteListener(onTaskExecuteListener<String> mListener) {
		this.mListener = mListener;
	}
	
	private onTaskExecuteListener<String> mListener;
	
	private String url;
}
