package com.zws.wifiboarddetect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WifiBoardDetect extends Activity {

	private static final String TAG = "zws";
	private ListView mWifiListView ;
	private ArrayList<MyListItem> list;
	ListAdapter mAdapter;

	private Button mSettings = null;
	private WifiManager mWifiManager = null;
	private WifiInfo mWifiInfo = null;
	private String MAC_NULL = "XX:XX:XX:XX:XX:XX";
	private Handler mHandler;
	private TextView mWifiBoardMacAddress;
	private TextView mUser_Settings_Explain;
	private boolean isConfiged = false;
    private long firstTime = 0;

	private SharedPreferences mSettingsData = null;
	private SharedPreferences.Editor mSettingsEd = null;
	private String mGetMacAddressRange = null;
	private String mSsid_1_Name = null;
	private int mSsid_1_Level = 0 ;
	private String mSsid_2_Name = null;
	private int mSsid_2_Level = 0 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_board_detect_layout);

		Log.d(TAG, "OnCreat()");
		
		mSettingsData = getSharedPreferences("wifiDetectData", MODE_PRIVATE);
		if(mSettingsData != null){
			Log.d(TAG, "creat xml");
			mGetMacAddressRange = mSettingsData.getString("mac_range", MAC_NULL);
			mSsid_1_Name = mSettingsData.getString("ssid_1_name", null);
			mSsid_1_Level = mSettingsData.getInt("ssid_1_level", 0);
			mSsid_2_Name = mSettingsData.getString("ssid_2_name", null);
			mSsid_2_Level = mSettingsData.getInt("ssid_2_level", 0);
		}else{
			Log.d(TAG, "xml is null");
		}
		
		mWifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
		mUser_Settings_Explain = (TextView)findViewById(R.id.user_settings_explain);

		Log.d(TAG, "mGetMacAddressRange = " + mGetMacAddressRange);
		Log.d(TAG, "mSsid_1_Name = " + mSsid_1_Name);
		Log.d(TAG, "mSsid_1_Level = " + mSsid_1_Level);
		Log.d(TAG, "mSsid_2_Name = " + mSsid_2_Name);
		Log.d(TAG, "mSsid_2_Level = " + mSsid_2_Level);
		
		if(mGetMacAddressRange.equals(MAC_NULL)){
			mUser_Settings_Explain.setText("<------请先在设置中配置MAC地址范围----->");
		}else if(mSsid_1_Name.equals(null) || mSsid_1_Level == 0){
			mUser_Settings_Explain.setText("<------请先在设置中配置路由名称和标准强度----->");
		}else{
			Log.d(TAG, "config is ok---->");
			isConfiged = true;
		}
		
		mWifiBoardMacAddress = (TextView)findViewById(R.id.wifiBoardMacAddress);
		mWifiListView = (ListView)findViewById(android.R.id.list);
		list = new ArrayList<MyListItem>();
		mAdapter = new ListAdapter(this, list);
		mWifiListView.setAdapter(mAdapter);
//		mWifiListView.setFastScrollEnabled(true);
		
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
					ScanResult _result1 = searchApScanResult(mSsid_1_Name);
					if(_result1 != null)
					{
						Log.d(TAG, "--------find------------");
						Log.d(TAG, "ssid =" + _result1.SSID + "level =" + _result1.level);
						//question
						for(int i = 0 ; i < list.size(); i++){
							if(_result1.SSID.equals(list.get(i).ssid))
								return ;
						}
						list.add(new MyListItem(_result1.SSID, _result1.level));
						for(int i = 0;i < list.size(); i++){
							Log.d(TAG, "list " + i + " = " + list.get(i));
						}
						mAdapter.notifyDataSetChanged();	
//						mWifiListView.setSelection(list.size()-1);
					}
					else
						Log.d(TAG, "not find ssid1");
					ScanResult _result2 = searchApScanResult(mSsid_2_Name);
//					if(_result2 != null)
//					{
//						Log.d(TAG, "--------find 2------------");
//						Log.d(TAG, "ssid 2=" + _result2.SSID + "level 2=" + _result2.level);
//						//question
//						for(int i = 0 ; i < list.size(); i++){
//							if(_result2.SSID.equals(list.get(i).ssid))
//								return ;
//						}
//						list.add(new MyListItem(_result2.SSID, _result2.level));
//						mAdapter.notifyDataSetChanged();
//						mWifiListView.setSelection(list.size() - 1);
//					}
//					else
//						Log.d(TAG, "not find ssid2");
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
	
	public ScanResult searchApScanResult(String pssid){
		ScanResult _scanResult = null;
		List<ScanResult> _results = mWifiManager.getScanResults();
		int _size = mWifiManager.getScanResults().size();
		Log.d(TAG, "search ssid = " + pssid);
		if(_size > 0)
			for(int i = 0;i < _size;i++ ){
				Log.d(TAG, "ssid " + i + " = " + _results.get(i).SSID);
				if(_results.get(i).SSID.equals(pssid))
					{
						_scanResult = _results.get(i);
						break;
					}
			}
		return _scanResult;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	   /**
		 *程序优化，点击两次退出程序
		 * */
		public boolean onKeyUp(int keyCode, KeyEvent event) { 
	        if (keyCode == KeyEvent.KEYCODE_BACK) { 
	            long secondTime = System.currentTimeMillis(); 
	            if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出 
	                Toast.makeText(this, "再按一次退出程序...", 
	                        Toast.LENGTH_SHORT).show(); 
	                firstTime = secondTime;//更新firstTime 
	                return true; 
	            } else { 
	                System.exit(0);//否则退出程序 
	            } 
	        } 
	        return super.onKeyUp(keyCode, event); 
	    }   
}
