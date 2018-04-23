package com.zws.wifiboarddetect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
	private TextView mDetectResult;
	private boolean isConfiged = false;
    private long firstTime = 0;

	private SharedPreferences mSettingsData = null;
	private SharedPreferences.Editor mSettingsEd = null;
	private String mGetMacAddressRangeBegin = null;
	private String mGetMacAddressRangeEnd = null;
	private String mSsid_1_Name = null;
	private int mSsid_1_Level = 0 ;
	private String mSsid_2_Name = null;
	private int mSsid_2_Level = 0 ;
	private String mSsid_3_Name = null;
	private int mSsid_3_Level = 0 ;
	private String mSsid_4_Name = null;
	private int mSsid_4_Level = 0 ;
	private String mSsid_5_Name = null;
	private int mSsid_5_Level = 0 ;
	
	private float percentage1 = 0;
	private float percentage2 = 0;
	private float percentage3 = 0;
	private float percentage4 = 0;
	private float percentage5 = 0;
	
	Calculate calculateCommonMac = null;;
    private String getWifiMacRangeCommon = null;
    private String getWifiBoardMacCommon = null;
    
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_board_detect_layout);
        
		Log.d(TAG, "OnCreat()");
		
		mSettingsData = getSharedPreferences("wifiDetectData", MODE_PRIVATE);
		if(mSettingsData != null){
			Log.d(TAG, "creat xml");
			mGetMacAddressRangeBegin= mSettingsData.getString("mac_range_begin", MAC_NULL);
			mGetMacAddressRangeEnd = mSettingsData.getString("mac_range_end", MAC_NULL);
			mSsid_1_Name = mSettingsData.getString("ssid_1_name", null);
			mSsid_1_Level = mSettingsData.getInt("ssid_1_level", -120);
			mSsid_2_Name = mSettingsData.getString("ssid_2_name", null);
			mSsid_2_Level = mSettingsData.getInt("ssid_2_level", -120);
			mSsid_3_Name = mSettingsData.getString("ssid_3_name", null);
			mSsid_3_Level = mSettingsData.getInt("ssid_3_level", -120);
			mSsid_4_Name = mSettingsData.getString("ssid_4_name", null);
			mSsid_4_Level = mSettingsData.getInt("ssid_4_level", -120);
			mSsid_5_Name = mSettingsData.getString("ssid_5_name", null);
			mSsid_5_Level = mSettingsData.getInt("ssid_5_level", -120);
		}else{
			Log.d(TAG, "xml is null");
		}
		
		mWifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
		mUser_Settings_Explain = (TextView)findViewById(R.id.user_settings_explain);

		Log.d(TAG, "mGetMacAddressRangeBegin = " + mGetMacAddressRangeBegin);
		Log.d(TAG, "mGetMacAddressRangeEnd = " + mGetMacAddressRangeEnd);
		Log.d(TAG, "mSsid_1_Name = " + mSsid_1_Name);
		Log.d(TAG, "mSsid_1_Level = " + mSsid_1_Level);
		Log.d(TAG, "mSsid_2_Name = " + mSsid_2_Name);
		Log.d(TAG, "mSsid_2_Level = " + mSsid_2_Level);
		Log.d(TAG, "mSsid_3_Name = " + mSsid_3_Name);
		Log.d(TAG, "mSsid_3_Level = " + mSsid_3_Level);
		Log.d(TAG, "mSsid_4_Name = " + mSsid_4_Name);
		Log.d(TAG, "mSsid_4_Level = " + mSsid_4_Level);
		Log.d(TAG, "mSsid_5_Name = " + mSsid_5_Name);
		Log.d(TAG, "mSsid_5_Level = " + mSsid_5_Level);
		
		if(mGetMacAddressRangeBegin.equals(MAC_NULL) || mGetMacAddressRangeEnd.equals(MAC_NULL)){
			mUser_Settings_Explain.setText("<------请先在设置中配置MAC地址范围----->");
		}else if(mSsid_1_Name.equals(null) || mSsid_1_Level == 0){
			mUser_Settings_Explain.setText("<------请先在设置中配置路由名称和标准强度----->");
		}else{
			Log.d(TAG, "config is ok---->");
			isConfiged = true;
		}
		
		calculateCommonMac = new Calculate();
		getWifiMacRangeCommon = calculateCommonMac.CalculateCommonMac(mGetMacAddressRangeBegin, mGetMacAddressRangeEnd);
		Log.d(TAG, "commonMac = " + getWifiMacRangeCommon);
		
		mDetectResult = (TextView)findViewById(R.id.DetectResult);
		mWifiBoardMacAddress = (TextView)findViewById(R.id.wifiBoardMacAddress);
		mWifiListView = (ListView)findViewById(android.R.id.list);
		list = new ArrayList<MyListItem>();
		mAdapter = new ListAdapter(this, list);
		mWifiListView.setAdapter(mAdapter);
		
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
					mDetectResult.setText("<-----未发现WiFi模块----->");
					list.clear();
					mAdapter.notifyDataSetChanged();
					break;
				case 1:
					mWifiBoardMacAddress.setText(getMac());
					getWifiBoardMacCommon = calculateCommonMac.CalculateWifiBoardMacCommon(getMac());
					Log.d(TAG, "WifiBoardMacCommon = " + getWifiBoardMacCommon);
					ScanResult _result1 = searchApScanResult(mSsid_1_Name);
					updateWifiList(_result1);
					ScanResult _result2 = searchApScanResult(mSsid_2_Name);
					updateWifiList(_result2);
					ScanResult _result3 = searchApScanResult(mSsid_3_Name);
					updateWifiList(_result3);
					ScanResult _result4 = searchApScanResult(mSsid_4_Name);
					updateWifiList(_result4);
					ScanResult _result5 = searchApScanResult(mSsid_5_Name);
					updateWifiList(_result5);
					
					if(getWifiBoardMacCommon.equalsIgnoreCase(getWifiMacRangeCommon) &&
							calculateCommonMac.isInMacRange(mGetMacAddressRangeBegin, mGetMacAddressRangeEnd, getMac())){
						Log.d(TAG, "wifi board mac is in range ok !");
						if(percentage1 > 50.0 && percentage2 > 50.0 && percentage3 > 50.0 
								&& percentage4 > 50.0 && percentage5 > 50.0){
							Log.d(TAG, "percentage is ok !");
							mDetectResult.setText(" <-----PASS----->");
						}else{
							mDetectResult.setText("<-----Fail !!! Wifi信号强度不合格----->");
						}
					}else{
						mDetectResult.setText("<-----Fail !!! Mac地址不在指定范围内----->");
					}
					
					break;
				default:
					break;
				}
			}
		};
		
//		this.registerReceiver(new BroadcastReceiver() {
//			
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				// TODO Auto-generated method stub
//			}
//		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
	
	public void  updateWifiList(ScanResult presult) {
		
		if(presult != null)
		{
			Log.d(TAG, "--------find ------------");
			Log.d(TAG, "ssid =" + presult.SSID + "level =" + presult.level);
			int flag = -1;
			for(int i = 0 ; i < list.size(); i++){
				if(presult.SSID.equals(list.get(i).ssid))
					{
						flag = i; 
						break;	
					}
				}
			if(flag == -1){
				Log.d(TAG, " ---list add---");
				if(presult.SSID.equals(mSsid_1_Name)){
					percentage1 = (((float)Math.abs(presult.level))/Math.abs(mSsid_1_Level))*100;
					Log.d(TAG, "level1 " + Math.abs(presult.level));
					Log.d(TAG, "stand1 " + Math.abs(mSsid_1_Level));
					Log.d(TAG, "percent1 " + percentage1);
					list.add(new MyListItem(presult.SSID, percentage1));
					mAdapter.notifyDataSetChanged();
				}
				if(presult.SSID.equals(mSsid_2_Name)){
					percentage2 = (((float)Math.abs(presult.level))/Math.abs(mSsid_2_Level))*100;
					Log.d(TAG, "level2 " + Math.abs(presult.level));
					Log.d(TAG, "stand2 " + Math.abs(mSsid_2_Level));
					Log.d(TAG, "percent2 " + percentage2);
					list.add(new MyListItem(presult.SSID, percentage2));
					mAdapter.notifyDataSetChanged();
				}
				if(presult.SSID.equals(mSsid_3_Name)){
					percentage3 = (((float)Math.abs(presult.level))/Math.abs(mSsid_3_Level))*100;
					Log.d(TAG, "level3 " + Math.abs(presult.level));
					Log.d(TAG, "stand3 " + Math.abs(mSsid_3_Level));
					Log.d(TAG, "percent3 " + percentage3);
					list.add(new MyListItem(presult.SSID, percentage3));
					mAdapter.notifyDataSetChanged();
				}
				if(presult.SSID.equals(mSsid_4_Name)){
					percentage4 = (((float)Math.abs(presult.level))/Math.abs(mSsid_4_Level))*100;
					Log.d(TAG, "level4 " + Math.abs(presult.level));
					Log.d(TAG, "stand4 " + Math.abs(mSsid_4_Level));
					Log.d(TAG, "percent4 " + percentage4);
					list.add(new MyListItem(presult.SSID, percentage4));
					mAdapter.notifyDataSetChanged();
				}
				if(presult.SSID.equals(mSsid_5_Name)){
					percentage5 = (((float)Math.abs(presult.level))/Math.abs(mSsid_5_Level))*100;
					Log.d(TAG, "level5 " + Math.abs(presult.level));
					Log.d(TAG, "stand5 " + Math.abs(mSsid_5_Level));
					Log.d(TAG, "percent5 " + percentage5);
					list.add(new MyListItem(presult.SSID, percentage5));
					mAdapter.notifyDataSetChanged();
				}
			}
		}
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
