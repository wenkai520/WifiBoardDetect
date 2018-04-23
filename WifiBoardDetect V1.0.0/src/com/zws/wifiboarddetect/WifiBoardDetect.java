package com.zws.wifiboarddetect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WifiBoardDetect extends Activity {

	private static final String TAG = "zws";
	private Button mSettings = null;
	private WifiManager mWifiManager = null;
	private WifiInfo mWifiInfo = null;
	private String MAC_NULL = "XX:XX:XX:XX:XX:XX";
	private Handler mHandler;
	private TextView mWifiBoardMacAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_board_detect_layout);

		mWifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
		
		mWifiBoardMacAddress = (TextView)findViewById(R.id.wifiBoardMacAddress);
		
		
		mSettings = (Button)findViewById(R.id.settings);
		mSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), WifiStandSettings.class);
				startActivity(intent);
				finish();
			}
		});
		new DetectThread().start();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					mWifiBoardMacAddress.setText("XX:XX:XX:XX:XX:XX");
					break;
				case 1:
					mWifiBoardMacAddress.setText(getMac());
					break;
				default:
					break;
				}
				
			}
		};
		
		this.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	class DetectThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
			if(!mWifiManager.isWifiEnabled())
				try {
					mWifiManager.setWifiEnabled(true);
				} catch (Exception e) {
					// TODO: handle exception
				}
			if(mWifiManager.isWifiEnabled()){
					mWifiManager.startScan();
					Log.d(TAG, "startScan");
					Message msg = new Message();
					if(mWifiManager.getScanResults().size() > 0){
						msg.what = 1;
					}else{
						msg.what = 0;
					}
					mHandler.sendMessage(msg);
				}
			        try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		   }
		}
	}	
	public String getMac(){
		String _mac = MAC_NULL;
		mWifiInfo = mWifiManager.getConnectionInfo();
		if(mWifiInfo != null){
			_mac = mWifiInfo.getMacAddress();
		}
		return _mac;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
