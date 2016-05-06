package com.cross.gfw.task;

public class BaseTask extends Thread {

	public interface onTaskExecuteListener<T> {
		
		// 任務執行前被調用
		public void onPreExecute();
		
		// 網絡連接失敗
		public void onConnectError();
		
		// 讀取每一行數據
		public void onReadLine(T line);

		// 任務執行完畢
		public void onPostExecute(T result);
	}
	
	protected static final String CHARSET = "utf-8";
	protected static final int NET_OK = 200;
	protected static final int CONN_TIME_OUT = 5000;
	protected static final int READ_TIME_OUT = 5000;
}
