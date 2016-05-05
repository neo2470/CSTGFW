package com.cross.gfw.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class PullManifest implements Runnable {
	
	public interface OnPullManifestListener {
		public void onPreExecute();
		public void onPostExecute(String data);
	}

	@Override
	public void run() {
		if(checkListenerIsNotNull()) {
			mListener.onPreExecute();
		}
		
		String data = getWebContent(MANIFEST_URL, null, "utf-8");
		
		if(checkListenerIsNotNull()) {
			mListener.onPostExecute(data);
		}
	}
	
	public void setOnPullManifestListener(OnPullManifestListener mListener) {
		this.mListener = mListener;
	}

	private String getWebContent(String webUrl, HashMap<String, String> header, String charset) {
        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            if(null != header) {
                for(String key : header.keySet()) {
                    urlConnection.setRequestProperty(key, header.get(key));
                }
            }

            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

            String line;
            while (null != (line=bufferedReader.readLine())) {
                builder.append(line);
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return builder.toString();
    }
	
	private boolean checkListenerIsNotNull() {
		if(null != mListener) {
			return true;
		} else {
			throw new NullPointerException();
		}
	}
	
	private OnPullManifestListener mListener;
	
	private static final String MANIFEST_URL = "https://code.csdn.net/zxfhacker/zxdstore/blob/master/CSTGFW/manifest.json";
}
